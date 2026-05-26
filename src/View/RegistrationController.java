package View;

import Model.Date;
import ViewModel.RegistrationViewModel;
import ViewModel.ViewModelFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.time.LocalDate;

public class RegistrationController {

    @FXML private TextField     firstNameField;
    @FXML private TextField     lastNameField;
    @FXML private TextField     emailField;
    @FXML private TextField     phoneField;
    @FXML private TextField     usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private DatePicker    dobPicker;
    @FXML private ComboBox<String> genderComboBox;
    @FXML private TextField     nationalityField;
    @FXML private Label         errorLabel;
    @FXML private Button        registerButton;
    @FXML private Button        backToLoginButton;
    @FXML private VBox          formContainer;

    private RegistrationViewModel viewModel;

    @FXML
    private void initialize() {
        viewModel = ViewModelFactory.getInstance().getRegistrationViewModel();

        genderComboBox.getItems().addAll("Male", "Female", "Other", "Prefer not to say");
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);

        // Bind ViewModel properties to fields
        firstNameField.textProperty().bindBidirectional(viewModel.firstNameProperty());
        lastNameField.textProperty().bindBidirectional(viewModel.lastNameProperty());
        emailField.textProperty().bindBidirectional(viewModel.emailProperty());
        phoneField.textProperty().bindBidirectional(viewModel.phoneNumberProperty());
        usernameField.textProperty().bindBidirectional(viewModel.usernameProperty());
        passwordField.textProperty().bindBidirectional(viewModel.passwordProperty());
        confirmPasswordField.textProperty().bindBidirectional(viewModel.confirmPasswordProperty());
        genderComboBox.valueProperty().bindBidirectional(viewModel.genderProperty());
        nationalityField.textProperty().bindBidirectional(viewModel.nationalityProperty());

        // Error feedback from server
        viewModel.errorMessageProperty().addListener((obs, o, msg) -> {
            if (msg != null && !msg.isEmpty()) { showError(msg); }
        });

        // Success from server
        viewModel.registrationSuccessProperty().addListener((obs, o, ok) -> {
            if (ok) {
                showSuccess();
                new Thread(() -> {
                    try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
                    javafx.application.Platform.runLater(this::handleBackToLogin);
                }).start();
            }
        });

        setupErrorClearListeners();
    }

    private void setupErrorClearListeners() {
        firstNameField.textProperty().addListener((obs, o, n) -> clearError());
        lastNameField.textProperty().addListener((obs, o, n) -> clearError());
        emailField.textProperty().addListener((obs, o, n) -> clearError());
        phoneField.textProperty().addListener((obs, o, n) -> clearError());
        usernameField.textProperty().addListener((obs, o, n) -> clearError());
        passwordField.textProperty().addListener((obs, o, n) -> clearError());
        confirmPasswordField.textProperty().addListener((obs, o, n) -> clearError());
    }

    @FXML
    private void handleRegister() {
        clearError();
        if (!validateFields()) return;

        LocalDate localDate = dobPicker.getValue();
        Date dob = new Date(localDate.getDayOfMonth(), localDate.getMonthValue(), localDate.getYear());
        viewModel.dobProperty().set(dob);

        viewModel.register();   // sends request through client-server pipeline
    }

    private boolean validateFields() {
        if (isNullOrEmpty(firstNameField.getText()))  { showError("First name is required"); firstNameField.requestFocus(); return false; }
        if (isNullOrEmpty(lastNameField.getText()))   { showError("Last name is required"); lastNameField.requestFocus(); return false; }
        if (isNullOrEmpty(emailField.getText()))      { showError("Email is required"); emailField.requestFocus(); return false; }
        if (!emailField.getText().contains("@"))      { showError("Enter a valid email"); emailField.requestFocus(); return false; }
        String phone = phoneField.getText().replaceAll("[^0-9]", "");
        if (phone.length() != 10) { showError("Phone must be 10 digits"); phoneField.requestFocus(); return false; }
        if (isNullOrEmpty(usernameField.getText()))   { showError("Username is required"); usernameField.requestFocus(); return false; }
        if (usernameField.getText().trim().length() < 3) { showError("Username ≥ 3 chars"); usernameField.requestFocus(); return false; }
        if (isNullOrEmpty(passwordField.getText()))   { showError("Password is required"); passwordField.requestFocus(); return false; }
        if (passwordField.getText().length() < 6)    { showError("Password ≥ 6 chars"); passwordField.requestFocus(); return false; }
        if (!passwordField.getText().equals(confirmPasswordField.getText())) { showError("Passwords do not match"); confirmPasswordField.requestFocus(); return false; }
        if (dobPicker.getValue() == null)            { showError("Date of birth required"); dobPicker.requestFocus(); return false; }
        if (dobPicker.getValue().isAfter(LocalDate.now().minusYears(18))) { showError("Must be at least 18 years old"); return false; }
        if (genderComboBox.getValue() == null)       { showError("Select a gender"); genderComboBox.requestFocus(); return false; }
        if (isNullOrEmpty(nationalityField.getText())){ showError("Nationality is required"); nationalityField.requestFocus(); return false; }
        return true;
    }

    private boolean isNullOrEmpty(String s) { return s == null || s.trim().isEmpty(); }

    private void showError(String msg) {
        errorLabel.setText(msg);
        errorLabel.setStyle("-fx-text-fill: #B71C1C; -fx-font-family: 'Cambria'; -fx-font-size: 13px;");
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }

    private void showSuccess() {
        errorLabel.setText("Registration successful! Redirecting…");
        errorLabel.setStyle("-fx-text-fill: #2E7D32; -fx-font-family: 'Cambria'; -fx-font-size: 14px; -fx-font-weight: bold;");
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
        formContainer.setDisable(true);
    }

    private void clearError() { errorLabel.setVisible(false); errorLabel.setManaged(false); }

    @FXML
    private void handleBackToLogin() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/View/LoginView.fxml"));
            registerButton.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
