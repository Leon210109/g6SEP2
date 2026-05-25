package View;

import Model.Client;
import Model.Date;
import Persistence.ClientDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.time.LocalDate;

public class RegistrationController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private DatePicker dobPicker;
    @FXML private ComboBox<String> genderComboBox;
    @FXML private TextField nationalityField;
    @FXML private Label errorLabel;
    @FXML private Button registerButton;
    @FXML private Button backToLoginButton;
    @FXML private VBox formContainer;

    private final ClientDAO clientDAO = new ClientDAO();

    @FXML
    private void initialize() {
        // Setup gender combo box
        genderComboBox.getItems().addAll("Male", "Female", "Other", "Prefer not to say");
        
        // Setup error label
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
        
        // Clear error when user types
        setupErrorClearListeners();
        
        // Add phone number formatting helper
        setupPhoneNumberValidation();
    }

    private void setupErrorClearListeners() {
        firstNameField.textProperty().addListener((obs, old, newVal) -> clearError());
        lastNameField.textProperty().addListener((obs, old, newVal) -> clearError());
        emailField.textProperty().addListener((obs, old, newVal) -> clearError());
        phoneField.textProperty().addListener((obs, old, newVal) -> clearError());
        usernameField.textProperty().addListener((obs, old, newVal) -> clearError());
        passwordField.textProperty().addListener((obs, old, newVal) -> clearError());
        confirmPasswordField.textProperty().addListener((obs, old, newVal) -> clearError());
    }

    private void setupPhoneNumberValidation() {
        phoneField.textProperty().addListener((obs, oldVal, newVal) -> {
            // Remove all non-numeric characters for display
            String digitsOnly = newVal.replaceAll("[^0-9]", "");
            
            // Visual feedback: show border color based on validity
            if (digitsOnly.length() == 10) {
                phoneField.setStyle("-fx-border-color: #2E7D32; -fx-border-width: 2px;"); // Green
            } else if (digitsOnly.length() > 0) {
                phoneField.setStyle("-fx-border-color: #F57C00; -fx-border-width: 2px;"); // Orange
            } else {
                phoneField.setStyle(""); // Default
            }
        });
    }

    @FXML
    private void handleRegister() {
        clearError();

        // Validate all fields
        if (!validateFields()) {
            return;
        }

        try {
            // Create Date object from DatePicker
            LocalDate localDate = dobPicker.getValue();
            Date dob = new Date(localDate.getDayOfMonth(), localDate.getMonthValue(), localDate.getYear());

            // Clean phone number - remove all non-numeric characters
            String cleanPhone = phoneField.getText().replaceAll("[^0-9]", "");

            // Create Client object
            Client newClient = new Client(
                firstNameField.getText().trim(),
                lastNameField.getText().trim(),
                emailField.getText().trim(),
                cleanPhone, // Use cleaned phone number
                usernameField.getText().trim(),
                passwordField.getText(), // Don't trim password
                dob,
                genderComboBox.getValue(),
                nationalityField.getText().trim()
            );

            // Save to database
            clientDAO.createClient(newClient);

            // Show success message
            showSuccess();

            // Navigate back to login after short delay
            new Thread(() -> {
                try {
                    Thread.sleep(2000); // 2 second delay
                    javafx.application.Platform.runLater(this::handleBackToLogin);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (Exception e) {
            showError("Registration failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean validateFields() {
        // Check required text fields
        if (isNullOrEmpty(firstNameField.getText())) {
            showError("First name is required");
            firstNameField.requestFocus();
            return false;
        }

        if (isNullOrEmpty(lastNameField.getText())) {
            showError("Last name is required");
            lastNameField.requestFocus();
            return false;
        }

        if (isNullOrEmpty(emailField.getText())) {
            showError("Email is required");
            emailField.requestFocus();
            return false;
        }

        // Basic email validation
        if (!emailField.getText().contains("@")) {
            showError("Please enter a valid email address");
            emailField.requestFocus();
            return false;
        }

        if (isNullOrEmpty(phoneField.getText())) {
            showError("Phone number is required");
            phoneField.requestFocus();
            return false;
        }

        // Validate phone number (must be exactly 10 digits)
        String cleanPhone = phoneField.getText().replaceAll("[^0-9]", ""); // Remove non-numeric
        if (cleanPhone.length() != 10) {
            showError("Phone number must be exactly 10 digits (digits only, no + or -)");
            phoneField.requestFocus();
            return false;
        }

        if (isNullOrEmpty(usernameField.getText())) {
            showError("Username is required");
            usernameField.requestFocus();
            return false;
        }

        if (usernameField.getText().trim().length() < 3) {
            showError("Username must be at least 3 characters");
            usernameField.requestFocus();
            return false;
        }

        if (isNullOrEmpty(passwordField.getText())) {
            showError("Password is required");
            passwordField.requestFocus();
            return false;
        }

        if (passwordField.getText().length() < 6) {
            showError("Password must be at least 6 characters");
            passwordField.requestFocus();
            return false;
        }

        if (isNullOrEmpty(confirmPasswordField.getText())) {
            showError("Please confirm your password");
            confirmPasswordField.requestFocus();
            return false;
        }

        if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            showError("Passwords do not match");
            confirmPasswordField.requestFocus();
            return false;
        }

        if (dobPicker.getValue() == null) {
            showError("Date of birth is required");
            dobPicker.requestFocus();
            return false;
        }

        // Check if user is at least 18 years old
        LocalDate today = LocalDate.now();
        LocalDate minDate = today.minusYears(18);
        if (dobPicker.getValue().isAfter(minDate)) {
            showError("You must be at least 18 years old to register");
            dobPicker.requestFocus();
            return false;
        }

        if (genderComboBox.getValue() == null || genderComboBox.getValue().isEmpty()) {
            showError("Please select a gender");
            genderComboBox.requestFocus();
            return false;
        }

        if (isNullOrEmpty(nationalityField.getText())) {
            showError("Nationality is required");
            nationalityField.requestFocus();
            return false;
        }

        return true;
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setStyle("-fx-text-fill: #B71C1C; -fx-font-family: 'Cambria'; -fx-font-size: 13px;");
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }

    private void showSuccess() {
        errorLabel.setText("✓ Registration successful! Redirecting to login...");
        errorLabel.setStyle("-fx-text-fill: #2E7D32; -fx-font-family: 'Cambria'; -fx-font-size: 14px; -fx-font-weight: bold;");
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
        
        // Disable form
        formContainer.setDisable(true);
    }

    private void clearError() {
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
    }

    @FXML
    private void handleBackToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/LoginView.fxml"));
            Parent loginRoot = loader.load();
            registerButton.getScene().setRoot(loginRoot);
        } catch (IOException e) {
            e.printStackTrace();
            showError("Failed to return to login");
        }
    }
}
