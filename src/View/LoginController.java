package View;

import Model.Admin;
import Model.Client;
import Model.PropertyOwner;
import ViewModel.LoginViewModel;
import ViewModel.ViewModelFactory;
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

    @FXML private TextField     usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button        adminAccessBtn;
    @FXML private Label         errorLabel;

    private LoginViewModel viewModel;

    @FXML
    private void initialize() {
        viewModel = ViewModelFactory.getInstance().getLoginViewModel();

        usernameField.textProperty().bindBidirectional(viewModel.usernameProperty());
        passwordField.textProperty().bindBidirectional(viewModel.passwordProperty());

        // Show/hide error label
        viewModel.errorMessageProperty().addListener((obs, o, newVal) -> {
            if (newVal != null && !newVal.isEmpty()) {
                errorLabel.setText(newVal);
                errorLabel.setVisible(true);
                errorLabel.setManaged(true);
            } else {
                errorLabel.setVisible(false);
                errorLabel.setManaged(false);
            }
        });

        // Clear error on keystroke
        usernameField.textProperty().addListener((obs, o, n) -> viewModel.clearError());
        passwordField.textProperty().addListener((obs, o, n) -> viewModel.clearError());
        passwordField.setOnAction(e -> handleLogin());

        // React to successful login (fires on JavaFX thread via Platform.runLater in ViewModel)
        viewModel.loggedInUserTypeProperty().addListener((obs, o, type) -> {
            if (type == null || type.isEmpty()) return;
            try {
                navigateToMainView(type, viewModel.loggedInUserProperty().get());
                viewModel.resetLoginState();
            } catch (IOException e) {
                viewModel.errorMessageProperty().set("Navigation error: " + e.getMessage());
            }
        });
    }

    @FXML
    private void handleLogin() {
        viewModel.login();
    }

    @FXML
    private void handleRegister() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/View/RegistrationView.fxml"));
            usernameField.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAdminAccess() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AdminAccessView.fxml"));
        Parent popupRoot = loader.load();
        AdminAccessController popupController = loader.getController();

        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.initOwner(adminAccessBtn.getScene().getWindow());
        popup.setTitle("Admin Access");
        popup.setResizable(false);

        Scene popupScene = new Scene(popupRoot);
        popupScene.getStylesheets().add(getClass().getResource("/View/styles.css").toExternalForm());
        popup.setScene(popupScene);

        // Admin bypass: navigate directly without hitting the server
        popupController.setOnEnter(userType -> {
            try {
                navigateToMainView(userType, null);
                popup.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        popup.showAndWait();
    }

    private void navigateToMainView(String userType, Object user) throws IOException {
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/View/MainView.fxml"));
        Parent mainRoot = mainLoader.load();
        MainController mainController = mainLoader.getController();
        mainController.init(userType, user);
        usernameField.getScene().setRoot(mainRoot);
    }
}
