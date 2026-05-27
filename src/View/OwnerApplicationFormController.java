package View;

import Model.Client;
import Model.Date;
import Model.OwnerApplication;
import ViewModel.OwnerApplicationViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;

public class OwnerApplicationFormController
{
    @FXML private TextField propertyAddressField;
    @FXML private TextField registrationNumberField;
    @FXML private Label     messageLabel;

    private Client client;
    private OwnerApplicationViewModel viewModel;
    private Runnable onSuccess;

    public void setClient(Client client)        { this.client = client; }
    public void setOnSuccess(Runnable callback) { this.onSuccess = callback; }

    @FXML
    private void initialize()
    {
        viewModel = new OwnerApplicationViewModel();

        viewModel.errorMessageProperty().addListener((obs, o, msg) ->
        {
            if (!msg.isEmpty()) showError(msg);
        });

        viewModel.submittedProperty().addListener((obs, o, success) ->
        {
            if (success)
            {
                showSuccess("Application submitted! An admin will review it shortly.");
                if (onSuccess != null) onSuccess.run();
                new Thread(() ->
                {
                    try { Thread.sleep(1500); } catch (InterruptedException ignored) {}
                    javafx.application.Platform.runLater(this::closeWindow);
                }).start();
            }
        });
    }

    @FXML
    private void handleSubmit()
    {
        messageLabel.setText("");
        String address   = propertyAddressField.getText().trim();
        String regNumber = registrationNumberField.getText().trim();
        if (address.isEmpty())   { showError("Property address is required.");    return; }
        if (regNumber.isEmpty()) { showError("Registration number is required."); return; }

        LocalDate today = LocalDate.now();
        Date submissionDate = new Date(today.getDayOfMonth(), today.getMonthValue(), today.getYear());
        OwnerApplication application = new OwnerApplication(
            client.getID(), 0, submissionDate, "Pending", address, regNumber);
        viewModel.submitApplication(application);
    }

    private void showError(String msg)   { messageLabel.setText(msg); messageLabel.setStyle("-fx-text-fill: #B22222;"); }
    private void showSuccess(String msg) { messageLabel.setText(msg); messageLabel.setStyle("-fx-text-fill: #1A5F3F; -fx-font-weight: bold;"); }

    @FXML private void handleCancel() { closeWindow(); }
    private void closeWindow()
    {
        viewModel.dispose();
        ((Stage) messageLabel.getScene().getWindow()).close();
    }
}