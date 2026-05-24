package View;

import Model.AuthenticationService;
import Model.AuthenticationService.AuthenticationResult;
import ViewModel.LoginViewModel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button adminAccessBtn;
    @FXML
    private Label errorLabel;

    private final LoginViewModel viewModel = new LoginViewModel();
    private final AuthenticationService authService = new AuthenticationService();

    @FXML
    private void initialize() {
        // Bind text fields to view model
        usernameField.textProperty().bindBidirectional(viewModel.usernameProperty());
        passwordField.textProperty().bindBidirectional(viewModel.passwordProperty());
        
        // Create error label if not defined in FXML
        if (errorLabel == null) {
            errorLabel = new Label();
            errorLabel.setStyle("-fx-text-fill: #B71C1C; -fx-font-family: 'Cambria'; -fx-font-size: 13px;");
            errorLabel.setVisible(false);
            errorLabel.setManaged(false);
        }
        
        // Bind error message
        viewModel.errorMessageProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.isEmpty()) {
                errorLabel.setText(newVal);
                errorLabel.setVisible(true);
                errorLabel.setManaged(true);
            } else {
                errorLabel.setVisible(false);
                errorLabel.setManaged(false);
            }
        });
        
        // Clear error when user types
        usernameField.textProperty().addListener((obs, oldVal, newVal) -> viewModel.clearError());
        passwordField.textProperty().addListener((obs, oldVal, newVal) -> viewModel.clearError());
        
        // Handle Enter key in password field
        passwordField.setOnAction(e -> handleLogin());
    }

    @FXML
    private void handleLogin() {
        viewModel.clearError();
        
        String username = viewModel.getUsername();
        String password = viewModel.getPassword();
        
        // Validate input
        if (username == null || username.trim().isEmpty()) {
            viewModel.setErrorMessage("Please enter a username");
            return;
        }
        
        if (password == null || password.trim().isEmpty()) {
            viewModel.setErrorMessage("Please enter a password");
            return;
        }
        
        // Perform authentication in background to avoid UI freeze
        Platform.runLater(() -> {
            try {
                AuthenticationResult result = authService.authenticate(username, password);
                
                if (result.isSuccess()) {
                    // Clear password from memory
                    viewModel.clearFields();
                    
                    // Navigate to main view
                    navigateToMainView(result.getUserType());
                } else {
                    // Show error message
                    viewModel.setErrorMessage(result.getErrorMessage());
                }
            } catch (Exception e) {
                viewModel.setErrorMessage("An unexpected error occurred: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void handleRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("RegistrationView.fxml"));
            Parent registrationRoot = loader.load();
            
            // Get the current scene and update its root
            usernameField.getScene().setRoot(registrationRoot);
        } catch (IOException e) {
            e.printStackTrace();
            viewModel.setErrorMessage("Failed to load registration form");
        }
    }

    @FXML
    private void handleAdminAccess() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminAccessView.fxml"));
        Parent popupRoot = loader.load();
        AdminAccessController popupController = loader.getController();

        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.initOwner(adminAccessBtn.getScene().getWindow());
        popup.setTitle("Admin Access");
        popup.setResizable(false);

        Scene popupScene = new Scene(popupRoot);
        popupScene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        popup.setScene(popupScene);

        popupController.setOnEnter(userType -> {
            try {
                navigateToMainView(userType);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        popup.showAndWait();
    }
    
    /**
     * Navigate to the main application view with the specified user type
     */
    private void navigateToMainView(String userType) throws IOException {
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("MainView.fxml"));
        Parent mainRoot = mainLoader.load();
        MainController mainController = mainLoader.getController();
        mainController.init(userType);
        
        // Get the current scene and update its root
        usernameField.getScene().setRoot(mainRoot);
    }
}
