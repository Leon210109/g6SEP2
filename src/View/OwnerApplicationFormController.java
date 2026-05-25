package View;

import Model.Client;
import Model.Date;
import Model.OwnerApplication;
import Persistence.OwnerApplicationDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;

public class OwnerApplicationFormController {

    @FXML private TextField propertyAddressField;
    @FXML private TextField registrationNumberField;
    @FXML private Label messageLabel;

    private Client client;
    private final OwnerApplicationDAO applicationDAO = new OwnerApplicationDAO();
    private Runnable onSuccess;

    public void setClient(Client client) {
        this.client = client;
    }

    public void setOnSuccess(Runnable callback) {
        this.onSuccess = callback;
    }

    @FXML
    private void handleSubmit() {
        messageLabel.setText("");

        String address = propertyAddressField.getText().trim();
        String regNumber = registrationNumberField.getText().trim();

        if (address.isEmpty()) {
            showError("Property address is required.");
            return;
        }
        if (regNumber.isEmpty()) {
            showError("Registration number is required.");
            return;
        }

        try {
            LocalDate today = LocalDate.now();
            Date submissionDate = new Date(today.getDayOfMonth(), today.getMonthValue(), today.getYear());

            // adminId = 0 means unassigned; admin will review it
            OwnerApplication application = new OwnerApplication(
                client.getID(),
                0,
                submissionDate,
                "Pending",
                address,
                regNumber
            );

            applicationDAO.createApplication(application);

            showSuccess("Application submitted! An admin will review it shortly.");

            new Thread(() -> {
                try {
                    Thread.sleep(1500);
                    javafx.application.Platform.runLater(() -> {
                        if (onSuccess != null) onSuccess.run();
                        closeWindow();
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (Exception e) {
            showError("Error submitting application: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void showError(String msg) {
        messageLabel.setText(msg);
        messageLabel.setStyle("-fx-text-fill: #B22222; -fx-font-family: 'Cambria'; -fx-font-size: 13px;");
    }

    private void showSuccess(String msg) {
        messageLabel.setText(msg);
        messageLabel.setStyle("-fx-text-fill: #1A5F3F; -fx-font-family: 'Cambria'; -fx-font-size: 13px; -fx-font-weight: bold;");
    }

    private void closeWindow() {
        ((Stage) propertyAddressField.getScene().getWindow()).close();
    }
}
