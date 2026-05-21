package View;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginController {

    @FXML
    private Button adminAccessBtn;

    @FXML
    private void handleLogin() {
        // no functionality yet
    }

    @FXML
    private void handleRegister() {
        // no functionality yet
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
                FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("MainView.fxml"));
                Parent mainRoot = mainLoader.load();
                MainController mainController = mainLoader.getController();
                mainController.init(userType);
                adminAccessBtn.getScene().setRoot(mainRoot);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        popup.showAndWait();
    }
}
