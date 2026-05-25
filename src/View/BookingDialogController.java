package View;

import Model.Booking;
import Model.Client;
import Model.Date;
import Model.Listing;
import ViewModel.BookingViewModel;
import ViewModel.ViewModelFactory;
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
    private BookingViewModel bookingViewModel;
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
        bookingViewModel = ViewModelFactory.getInstance().getBookingViewModel();
        numberOfPeopleField.textProperty().addListener((obs, o, n) -> { if (!n.matches("\\d*")) numberOfPeopleField.setText(o); });
        startDatePicker.valueProperty().addListener((obs, o, n) -> {
            if (n != null && endDatePicker.getValue() != null && !endDatePicker.getValue().isAfter(n))
                endDatePicker.setValue(n.plusDays(1));
        });
        bookingViewModel.bookingAddedSuccessProperty().addListener((obs, o, ok) -> {
            if (ok) {
                bookingViewModel.resetBookingSuccess();
                showSuccess("Booking confirmed!");
                new Thread(() -> {
                    try { Thread.sleep(1500); } catch (InterruptedException ignored) {}
                    javafx.application.Platform.runLater(() -> {
                        if (onSuccess != null) onSuccess.run();
                        closeWindow();
                    });
                }).start();
            }
        });
        bookingViewModel.errorMessageProperty().addListener((obs, o, msg) -> {
            if (msg != null && !msg.isEmpty()) showError(msg);
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

            if (numberOfPeople > listing.getMaxNumberOfPeople()) {
                showError("Number of people exceeds maximum occupancy (" + listing.getMaxNumberOfPeople() + ")");
                return;
            }

            Date startDate = new Date(startLocal.getDayOfMonth(), startLocal.getMonthValue(), startLocal.getYear());
            Date endDate   = new Date(endLocal.getDayOfMonth(),   endLocal.getMonthValue(),   endLocal.getYear());

            Booking newBooking = new Booking(
                client.getID(), listing.getId(),
                startDate, endDate, numberOfPeople,
                listing.getCheckInTime(), listing.getCheckOutTime()
            );

            bookingViewModel.addBooking(newBooking);  // goes through client-server pipeline

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
