package View;

import Model.Booking;
import Model.Client;
import Model.Date;
import Model.Listing;
import Persistence.BookingDAO;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;

public class BookingDialogController {

    @FXML private Text propertyText;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private TextField numberOfPeopleField;
    @FXML private Text checkInTimeText;
    @FXML private Text checkOutTimeText;
    @FXML private Text maxPeopleText;
    @FXML private Label messageLabel;

    private Listing listing;
    private Client client;
    private BookingDAO bookingDAO = new BookingDAO();
    private Runnable onSuccess;

    public void setBookingInfo(Listing listing, Client client) {
        this.listing = listing;
        this.client = client;
        
        // Display property info
        propertyText.setText(listing.getStreet() + ", " + listing.getRegion());
        
        // Display check-in/out times and max people
        checkInTimeText.setText("Check-in time: " + formatTime(listing.getCheckInTime()));
        checkOutTimeText.setText("Check-out time: " + formatTime(listing.getCheckOutTime()));
        maxPeopleText.setText("Maximum occupancy: " + listing.getMaxNumberOfPeople() + " people");
        
        // Set minimum date to today
        startDatePicker.setValue(LocalDate.now());
        endDatePicker.setValue(LocalDate.now().plusDays(1));
    }

    public void setOnSuccess(Runnable callback) {
        this.onSuccess = callback;
    }

    @FXML
    private void initialize() {
        // Add numeric validation to number of people field
        numberOfPeopleField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                numberOfPeopleField.setText(oldVal);
            }
        });
        
        // Validate that end date is after start date
        startDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && endDatePicker.getValue() != null) {
                if (!endDatePicker.getValue().isAfter(newVal)) {
                    endDatePicker.setValue(newVal.plusDays(1));
                }
            }
        });
    }

    @FXML
    private void handleBook() {
        messageLabel.setText("");
        
        // Validate inputs
        if (!validateInputs()) {
            return;
        }

        try {
            LocalDate startLocal = startDatePicker.getValue();
            LocalDate endLocal = endDatePicker.getValue();
            int numberOfPeople = Integer.parseInt(numberOfPeopleField.getText().trim());

            // Check if listing is already booked (race condition guard)
            if (bookingDAO.isListingBooked(listing.getId())) {
                showError("This listing has already been booked. Please go back and choose another.");
                return;
            }

            // Check if number of people exceeds max
            if (numberOfPeople > listing.getMaxNumberOfPeople()) {
                showError("Number of people exceeds maximum occupancy (" + 
                         listing.getMaxNumberOfPeople() + ")");
                return;
            }

            // Convert LocalDate to custom Date
            Date startDate = new Date(
                startLocal.getDayOfMonth(),
                startLocal.getMonthValue(),
                startLocal.getYear()
            );
            Date endDate = new Date(
                endLocal.getDayOfMonth(),
                endLocal.getMonthValue(),
                endLocal.getYear()
            );

            // Get check-in/out times from listing
            LocalTime checkInTime = listing.getCheckInTime();
            LocalTime checkOutTime = listing.getCheckOutTime();

            // Create booking
            Booking newBooking = new Booking(
                client.getID(),
                listing.getId(),
                startDate,
                endDate,
                numberOfPeople,
                checkInTime,
                checkOutTime
            );

            // Save to database
            bookingDAO.CreateBooking(newBooking);

            showSuccess("Booking confirmed successfully!");
            
            // Wait a moment then close
            new Thread(() -> {
                try {
                    Thread.sleep(1500);
                    javafx.application.Platform.runLater(() -> {
                        if (onSuccess != null) {
                            onSuccess.run();
                        }
                        closeWindow();
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (NumberFormatException e) {
            showError("Please enter a valid number of people.");
        } catch (Exception e) {
            showError("Error creating booking: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean validateInputs() {
        if (startDatePicker.getValue() == null) {
            showError("Please select a check-in date.");
            return false;
        }
        if (endDatePicker.getValue() == null) {
            showError("Please select a check-out date.");
            return false;
        }
        if (numberOfPeopleField.getText().trim().isEmpty()) {
            showError("Please enter the number of people.");
            return false;
        }
        if (startDatePicker.getValue().isBefore(LocalDate.now())) {
            showError("Check-in date cannot be in the past.");
            return false;
        }
        if (!endDatePicker.getValue().isAfter(startDatePicker.getValue())) {
            showError("Check-out date must be after check-in date.");
            return false;
        }
        return true;
    }

    private String formatTime(LocalTime time) {
        if (time == null) return "N/A";
        int hour = time.getHour();
        int minute = time.getMinute();
        String period = hour >= 12 ? "PM" : "AM";
        int displayHour = hour % 12;
        if (displayHour == 0) displayHour = 12;
        return String.format("%d:%02d %s", displayHour, minute, period);
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
        Stage stage = (Stage) messageLabel.getScene().getWindow();
        stage.close();
    }
}
