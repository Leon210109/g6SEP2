package View;

import ViewModel.LoginViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import java.util.function.Consumer;

public class AdminAccessController {

    @FXML
    private RadioButton rbClient;
    @FXML
    private RadioButton rbOwner;
    @FXML
    private RadioButton rbAdmin;
    @FXML
    private ToggleGroup group;
    @FXML
    private Button enterBtn;

    private final LoginViewModel loginVM = new LoginViewModel();
    private Consumer<String> onEnter;

    @FXML
    private void initialize() {
        group.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == rbClient)
                loginVM.setSelectedUserType("Client");
            else if (newVal == rbOwner)
                loginVM.setSelectedUserType("Property Owner");
            else
                loginVM.setSelectedUserType("Admin");
        });
    }

    /** Called by LoginController before the popup is shown. */
    public void setOnEnter(Consumer<String> onEnter) {
        this.onEnter = onEnter;
    }

    @FXML
    private void handleEnter() {
        if (onEnter != null) {
            onEnter.accept(loginVM.getSelectedUserType());
        }
        ((Stage) enterBtn.getScene().getWindow()).close();
    }
}
