package View;

import Model.Date;
import Model.Listing;
import Model.PropertyOwner;
import Persistence.CityDAO;
import Persistence.ListingDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class CreateListingController {

    // Location fields
    @FXML private TextField streetField;
    @FXML private TextField roomNumberField;
    @FXML private TextField regionField;
    @FXML private TextField countryField;
    @FXML private TextField postalCodeField;
    @FXML private TextField floorField;

    // Property details fields
    @FXML private TextField roomsField;
    @FXML private TextField bathroomsField;
    @FXML private TextField surfaceAreaField;
    @FXML private TextField maxPeopleField;
    @FXML private CheckBox balconyCheckBox;

    // Rental information fields
    @FXML private TextField priceField;
    @FXML private DatePicker renovatedDatePicker;
    @FXML private TextField checkInTimeField;
    @FXML private TextField checkOutTimeField;

    // Image upload
    @FXML private Button uploadImagesBtn;
    @FXML private Label imageCountLabel;
    @FXML private ScrollPane imagePreviewScrollPane;
    @FXML private FlowPane imagePreviewPane;

    // Messages and actions
    @FXML private Label messageLabel;
    @FXML private Button createButton;
    @FXML private RadioButton shortTermRadio;
    @FXML private RadioButton longTermRadio;

    private PropertyOwner propertyOwner;
    private List<File> selectedImages = new ArrayList<>();
    private ListingDAO listingDAO = new ListingDAO();
    private CityDAO cityDAO = new CityDAO();
    private Runnable onSuccess;

    public void setPropertyOwner(PropertyOwner owner) {
        this.propertyOwner = owner;
    }

    public void setOnSuccess(Runnable callback) {
        this.onSuccess = callback;
    }

    @FXML
    private void initialize() {
        // Set default values
        renovatedDatePicker.setValue(LocalDate.now());
        checkInTimeField.setText("15:00");
        checkOutTimeField.setText("11:00");
        
        // Add input validation styling
        setupValidation();
    }

    private void setupValidation() {
        // Numeric validation for rooms, bathrooms, floor, maxPeople, price
        addNumericValidation(roomsField);
        addNumericValidation(bathroomsField);
        addNumericValidation(floorField);
        addNumericValidation(maxPeopleField);
        addNumericValidation(priceField);
        
        // Decimal validation for surface area
        addDecimalValidation(surfaceAreaField);
    }

    private void addNumericValidation(TextField field) {
        field.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                field.setText(oldVal);
            }
        });
    }

    private void addDecimalValidation(TextField field) {
        field.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*\\.?\\d*")) {
                field.setText(oldVal);
            }
        });
    }

    @FXML
    private void handleImageUpload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Property Images");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", 
                "*.jpg", "*.jpeg", "*.png", "*.webp")
        );

        List<File> files = fileChooser.showOpenMultipleDialog(uploadImagesBtn.getScene().getWindow());
        
        if (files != null && !files.isEmpty()) {
            selectedImages.clear();
            selectedImages.addAll(files);
            updateImagePreview();
            imageCountLabel.setText(files.size() + " image(s) selected");
            imageCountLabel.setStyle("-fx-text-fill: #1A5F3F; -fx-font-weight: bold;");
        }
    }

    private void updateImagePreview() {
        imagePreviewPane.getChildren().clear();
        
        for (File imageFile : selectedImages) {
            try {
                Image image = new Image(imageFile.toURI().toString());
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(100);
                imageView.setFitHeight(75);
                imageView.setPreserveRatio(true);
                imageView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 0, 2);");
                
                imagePreviewPane.getChildren().add(imageView);
            } catch (Exception e) {
                System.err.println("Error previewing image: " + imageFile.getName());
            }
        }
    }

    @FXML
    private void handleCreate() {
        // Clear previous messages
        messageLabel.setText("");
        
        // Validate required fields
        if (!validateFields()) {
            return;
        }

        try {
            // Parse inputs
            String street = streetField.getText().trim();
            String roomNumber = roomNumberField.getText().trim();
            String region = regionField.getText().trim();
            String country = countryField.getText().trim();
            String postalCode = postalCodeField.getText().trim();
            int floor = floorField.getText().isEmpty() ? 0 : Integer.parseInt(floorField.getText().trim());
            
            int rooms = Integer.parseInt(roomsField.getText().trim());
            int bathrooms = Integer.parseInt(bathroomsField.getText().trim());
            float surfaceArea = Float.parseFloat(surfaceAreaField.getText().trim());
            int maxPeople = Integer.parseInt(maxPeopleField.getText().trim());
            boolean hasBalcony = balconyCheckBox.isSelected();
            
            int price = Integer.parseInt(priceField.getText().trim());
            LocalDate renovatedLocal = renovatedDatePicker.getValue();
            Date renovated = new Date(
                renovatedLocal.getDayOfMonth(),
                renovatedLocal.getMonthValue(),
                renovatedLocal.getYear()
            );

            // Parse check-in and check-out times
            LocalTime checkInTime = parseTime(checkInTimeField.getText().trim());
            LocalTime checkOutTime = parseTime(checkOutTimeField.getText().trim());

            // Ensure city/postal code exists in database
            ensureCityExists(postalCode, region);

            // Create listing object
            Listing newListing = new Listing(
                rooms, bathrooms, hasBalcony, surfaceArea, price, renovated,
                maxPeople, country, region, street, floor, roomNumber,
                postalCode, propertyOwner.getID()
            );
            
            // Set check-in and check-out times
            newListing.setCheckInTime(checkInTime);
            newListing.setCheckOutTime(checkOutTime);

            // Set listing type
            newListing.setListingType(longTermRadio != null && longTermRadio.isSelected() ? "LONG_TERM" : "SHORT_TERM");

            // Insert into database
            listingDAO.CreateListing(newListing);

            // Save images to folder
            if (!selectedImages.isEmpty()) {
                saveImagesToFolder(street);
            }

            // Show success message
            showSuccess("Listing created successfully!");
            
            // Notify parent and close
            if (onSuccess != null) {
                onSuccess.run();
            }
            closeWindow();

        } catch (NumberFormatException e) {
            showError("Please ensure all numeric fields are valid numbers.");
        } catch (Exception e) {
            showError("Error creating listing: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean validateFields() {
        List<String> errors = new ArrayList<>();

        if (streetField.getText().trim().isEmpty()) {
            errors.add("Street name is required");
        }
        if (regionField.getText().trim().isEmpty()) {
            errors.add("City/Region is required");
        }
        if (countryField.getText().trim().isEmpty()) {
            errors.add("Country is required");
        }
        if (postalCodeField.getText().trim().isEmpty()) {
            errors.add("Postal code is required");
        }
        if (roomsField.getText().trim().isEmpty()) {
            errors.add("Number of rooms is required");
        }
        if (bathroomsField.getText().trim().isEmpty()) {
            errors.add("Number of bathrooms is required");
        }
        if (surfaceAreaField.getText().trim().isEmpty()) {
            errors.add("Surface area is required");
        }
        if (maxPeopleField.getText().trim().isEmpty()) {
            errors.add("Max number of people is required");
        }
        if (priceField.getText().trim().isEmpty()) {
            errors.add("Monthly rent is required");
        }
        if (renovatedDatePicker.getValue() == null) {
            errors.add("Renovation date is required");
        }
        if (checkInTimeField.getText().trim().isEmpty()) {
            errors.add("Check-in time is required");
        } else {
            try {
                parseTime(checkInTimeField.getText().trim());
            } catch (Exception e) {
                errors.add("Check-in time must be in HH:MM format (e.g., 15:00)");
            }
        }
        if (checkOutTimeField.getText().trim().isEmpty()) {
            errors.add("Check-out time is required");
        } else {
            try {
                parseTime(checkOutTimeField.getText().trim());
            } catch (Exception e) {
                errors.add("Check-out time must be in HH:MM format (e.g., 11:00)");
            }
        }

        if (!errors.isEmpty()) {
            showError(String.join("\n", errors));
            return false;
        }

        return true;
    }

    private LocalTime parseTime(String timeStr) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
            return LocalTime.parse(timeStr, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid time format: " + timeStr);
        }
    }

    private void ensureCityExists(String postalCode, String cityName) {
        try {
            // Check if city exists, if not create it
            if (cityDAO.getCityByPostalCode(postalCode) == null) {
                cityDAO.createCity(postalCode, cityName);
            }
        } catch (Exception e) {
            System.err.println("Note: City may already exist or error checking: " + e.getMessage());
        }
    }

    private void saveImagesToFolder(String streetName) throws IOException {
        // Create folder: room_rental_img/{streetName}/
        File folder = new File("room_rental_img/" + streetName);
        
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // Copy each selected image to the folder
        for (File imageFile : selectedImages) {
            File destination = new File(folder, imageFile.getName());
            Files.copy(imageFile.toPath(), destination.toPath(), 
                      StandardCopyOption.REPLACE_EXISTING);
        }

        System.out.println("Saved " + selectedImages.size() + " images to: " + folder.getAbsolutePath());
    }

    private void showError(String message) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: #B22222; -fx-font-weight: bold;");
    }

    private void showSuccess(String message) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: #1A5F3F; -fx-font-weight: bold;");
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) createButton.getScene().getWindow();
        stage.close();
    }
}
