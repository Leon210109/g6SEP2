package View;

import Model.Date;
import Model.Listing;
import Model.PropertyOwner;
import ViewModel.ListingViewModel;
import ViewModel.ViewModelFactory;
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

    @FXML private TextField  streetField;
    @FXML private TextField  roomNumberField;
    @FXML private TextField  regionField;
    @FXML private TextField  countryField;
    @FXML private TextField  postalCodeField;
    @FXML private TextField  floorField;
    @FXML private TextField  roomsField;
    @FXML private TextField  bathroomsField;
    @FXML private TextField  surfaceAreaField;
    @FXML private TextField  maxPeopleField;
    @FXML private CheckBox   balconyCheckBox;
    @FXML private TextField  priceField;
    @FXML private DatePicker renovatedDatePicker;
    @FXML private TextField  checkInTimeField;
    @FXML private TextField  checkOutTimeField;
    @FXML private Button     uploadImagesBtn;
    @FXML private Label      imageCountLabel;
    @FXML private ScrollPane imagePreviewScrollPane;
    @FXML private FlowPane   imagePreviewPane;
    @FXML private Label      messageLabel;
    @FXML private Button     createButton;
    @FXML private RadioButton shortTermRadio;
    @FXML private RadioButton longTermRadio;

    private PropertyOwner propertyOwner;
    private final List<File> selectedImages = new ArrayList<>();
    private ListingViewModel listingViewModel;
    private Runnable onSuccess;

    public void setPropertyOwner(PropertyOwner owner) { this.propertyOwner = owner; }
    public void setOnSuccess(Runnable callback)        { this.onSuccess = callback; }

    @FXML
    private void initialize() {
        listingViewModel = ViewModelFactory.getInstance().getListingViewModel();
        renovatedDatePicker.setValue(LocalDate.now());
        checkInTimeField.setText("15:00");
        checkOutTimeField.setText("11:00");
        setupNumericValidation();

        // Listen for add success
        listingViewModel.addSuccessProperty().addListener((obs, o, ok) -> {
            if (ok) {
                listingViewModel.resetAddSuccess();
                showSuccess("Listing created successfully!");
                if (onSuccess != null) onSuccess.run();
                closeWindow();
            }
        });
        listingViewModel.errorMessageProperty().addListener((obs, o, msg) -> {
            if (msg != null && !msg.isEmpty()) showError(msg);
        });
    }

    private void setupNumericValidation() {
        for (TextField f : List.of(roomsField, bathroomsField, floorField, maxPeopleField, priceField)) {
            f.textProperty().addListener((obs, o, n) -> { if (!n.matches("\\d*")) f.setText(o); });
        }
        surfaceAreaField.textProperty().addListener((obs, o, n) -> { if (!n.matches("\\d*\\.?\\d*")) surfaceAreaField.setText(o); });
    }

    @FXML
    private void handleImageUpload() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Select Property Images");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png", "*.webp"));
        List<File> files = fc.showOpenMultipleDialog(uploadImagesBtn.getScene().getWindow());
        if (files != null && !files.isEmpty()) {
            selectedImages.clear();
            selectedImages.addAll(files);
            updateImagePreview();
            imageCountLabel.setText(files.size() + " image(s) selected");
        }
    }

    private void updateImagePreview() {
        imagePreviewPane.getChildren().clear();
        for (File f : selectedImages) {
            try {
                ImageView iv = new ImageView(new Image(f.toURI().toString()));
                iv.setFitWidth(100); iv.setFitHeight(75); iv.setPreserveRatio(true);
                imagePreviewPane.getChildren().add(iv);
            } catch (Exception ignored) {}
        }
    }

    @FXML
    private void handleCreate() {
        messageLabel.setText("");
        if (!validateFields()) return;
        try {
            int floor = floorField.getText().isEmpty() ? 0 : Integer.parseInt(floorField.getText().trim());
            LocalDate renovatedLocal = renovatedDatePicker.getValue();
            Date renovated = new Date(renovatedLocal.getDayOfMonth(), renovatedLocal.getMonthValue(), renovatedLocal.getYear());
            LocalTime checkIn  = parseTime(checkInTimeField.getText().trim());
            LocalTime checkOut = parseTime(checkOutTimeField.getText().trim());

            Listing newListing = new Listing(
                Integer.parseInt(roomsField.getText().trim()),
                Integer.parseInt(bathroomsField.getText().trim()),
                balconyCheckBox.isSelected(),
                Float.parseFloat(surfaceAreaField.getText().trim()),
                Integer.parseInt(priceField.getText().trim()),
                renovated,
                Integer.parseInt(maxPeopleField.getText().trim()),
                countryField.getText().trim(),
                regionField.getText().trim(),
                streetField.getText().trim(),
                floor,
                roomNumberField.getText().trim(),
                postalCodeField.getText().trim(),
                propertyOwner.getID()
            );
            newListing.setCheckInTime(checkIn);
            newListing.setCheckOutTime(checkOut);
            newListing.setListingType(longTermRadio != null && longTermRadio.isSelected() ? "LONG_TERM" : "SHORT_TERM");

            if (!selectedImages.isEmpty()) saveImagesToFolder(streetField.getText().trim());

            listingViewModel.addListing(newListing);  // goes through client-server pipeline
        } catch (NumberFormatException e) {
            showError("Ensure all numeric fields are valid numbers.");
        } catch (Exception e) {
            showError("Error: " + e.getMessage());
        }
    }

    private boolean validateFields() {
        if (streetField.getText().trim().isEmpty())      { showError("Street is required");        return false; }
        if (regionField.getText().trim().isEmpty())      { showError("Region is required");         return false; }
        if (countryField.getText().trim().isEmpty())     { showError("Country is required");        return false; }
        if (postalCodeField.getText().trim().isEmpty())  { showError("Postal code is required");    return false; }
        if (roomsField.getText().trim().isEmpty())       { showError("Rooms is required");          return false; }
        if (bathroomsField.getText().trim().isEmpty())   { showError("Bathrooms is required");      return false; }
        if (surfaceAreaField.getText().trim().isEmpty()) { showError("Surface area is required");   return false; }
        if (maxPeopleField.getText().trim().isEmpty())   { showError("Max people is required");     return false; }
        if (priceField.getText().trim().isEmpty())       { showError("Price is required");          return false; }
        if (renovatedDatePicker.getValue() == null)      { showError("Renovation date required");   return false; }
        try { parseTime(checkInTimeField.getText().trim()); }
        catch (Exception e) { showError("Check-in time must be HH:MM"); return false; }
        try { parseTime(checkOutTimeField.getText().trim()); }
        catch (Exception e) { showError("Check-out time must be HH:MM"); return false; }
        return true;
    }

    private LocalTime parseTime(String t) {
        try { return LocalTime.parse(t, DateTimeFormatter.ofPattern("H:mm")); }
        catch (DateTimeParseException e) { throw new IllegalArgumentException("Invalid time: " + t); }
    }

    private void saveImagesToFolder(String streetName) throws IOException {
        File folder = new File("room_rental_img/" + streetName);
        if (!folder.exists()) folder.mkdirs();
        for (File f : selectedImages)
            Files.copy(f.toPath(), new File(folder, f.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    private void showError(String msg)   { messageLabel.setText(msg);   messageLabel.setStyle("-fx-text-fill: #B22222; -fx-font-weight: bold;"); }
    private void showSuccess(String msg) { messageLabel.setText(msg);   messageLabel.setStyle("-fx-text-fill: #1A5F3F; -fx-font-weight: bold;"); }

    @FXML private void handleCancel() { closeWindow(); }
    private void closeWindow() { ((Stage) createButton.getScene().getWindow()).close(); }
}
