package View;

import Model.Date;
import Model.Listing;
import Model.PropertyOwner;
import Persistence.CityDAO;
import Persistence.ListingDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class EditListingController {

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

    // Messages and actions
    @FXML private Label messageLabel;
    @FXML private Button updateButton;
    @FXML private RadioButton shortTermRadio;
    @FXML private RadioButton longTermRadio;

    private Listing listing;
    private PropertyOwner propertyOwner;
    private ListingDAO listingDAO = new ListingDAO();
    private CityDAO cityDAO = new CityDAO();
    private Runnable onSuccess;

    public void setListing(Listing listing, PropertyOwner owner) {
        this.listing = listing;
        this.propertyOwner = owner;
        populateFields();
    }

    public void setOnSuccess(Runnable callback) {
        this.onSuccess = callback;
    }

    @FXML
    private void initialize() {
        // Will be populated when setListing is called
    }

    private void populateFields() {
        if (listing == null) return;

        // Location fields
        streetField.setText(listing.getStreet());
        roomNumberField.setText(listing.getRoomNumber());
        regionField.setText(listing.getRegion());
        countryField.setText(listing.getCountry());
        postalCodeField.setText(listing.getPostalcode());
        floorField.setText(String.valueOf(listing.getFloor()));

        // Property details
        roomsField.setText(String.valueOf(listing.getNumberOfRooms()));
        bathroomsField.setText(String.valueOf(listing.getNumberOfBathrooms()));
        surfaceAreaField.setText(String.valueOf(listing.getSurfaceArea()));
        maxPeopleField.setText(String.valueOf(listing.getMaxNumberOfPeople()));
        balconyCheckBox.setSelected(listing.isBalcony());

        // Rental information
        priceField.setText(String.valueOf(listing.getPrice()));
        
        // Set renovation date
        Date renovated = listing.getLastRenovated();
        renovatedDatePicker.setValue(LocalDate.of(renovated.getYear(), renovated.getMonth(), renovated.getDay()));
        
        // Set check-in and check-out times
        checkInTimeField.setText(formatTime(listing.getCheckInTime()));
        checkOutTimeField.setText(formatTime(listing.getCheckOutTime()));

        // Set listing type radio
        boolean isLongTerm = "LONG_TERM".equals(listing.getListingType());
        if (longTermRadio != null) longTermRadio.setSelected(isLongTerm);
        if (shortTermRadio != null) shortTermRadio.setSelected(!isLongTerm);
    }

    @FXML
    private void handleUpdate() {
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

            // Update listing object
            listing.setStreet(street);
            listing.setRoomNumber(roomNumber);
            listing.setRegion(region);
            listing.setCountry(country);
            listing.setPostalcode(postalCode);
            listing.setFloor(floor);
            listing.setNumberOfRooms(rooms);
            listing.setNumberOfBathrooms(bathrooms);
            listing.setSurfaceArea(surfaceArea);
            listing.setMaxNumberOfPeople(maxPeople);
            listing.setBalcony(hasBalcony);
            listing.setPrice(price);
            listing.setLastRenovated(renovated);
            listing.setCheckInTime(checkInTime);
            listing.setCheckOutTime(checkOutTime);
            listing.setListingType(longTermRadio != null && longTermRadio.isSelected() ? "LONG_TERM" : "SHORT_TERM");

            // Update in database
            listingDAO.updateListing(listing);

            // Show success message
            showSuccess("Listing updated successfully!");
            
            // Notify parent and close
            if (onSuccess != null) {
                onSuccess.run();
            }
            
            // Wait a moment then close
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    javafx.application.Platform.runLater(this::closeWindow);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (NumberFormatException e) {
            showError("Please ensure all numeric fields are valid numbers.");
        } catch (Exception e) {
            showError("Error updating listing: " + e.getMessage());
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

    private String formatTime(LocalTime time) {
        if (time == null) return "";
        return time.format(DateTimeFormatter.ofPattern("HH:mm"));
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

    private void showError(String message) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: #B22222; -fx-font-weight: bold;");
    }

    private void showSuccess(String message) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: #2E7D32; -fx-font-weight: bold;");
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) updateButton.getScene().getWindow();
        stage.close();
    }
}
