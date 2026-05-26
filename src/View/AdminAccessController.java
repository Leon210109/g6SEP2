package View;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import java.util.function.Consumer;

public class AdminAccessController {

    @FXML private RadioButton rbClient;
    @FXML private RadioButton rbOwner;
    @FXML private RadioButton rbAdmin;
    @FXML private ToggleGroup group;
    @FXML private Button enterBtn;

    private String selectedUserType = "Client";
    private Consumer<String> onEnter;

    @FXML
    private void initialize() {
        group.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == rbClient)      selectedUserType = "Client";
            else if (newVal == rbOwner)  selectedUserType = "Property Owner";
            else                         selectedUserType = "Admin";
        });
    }

    public void setOnEnter(Consumer<String> onEnter) {
        this.onEnter = onEnter;
    }

    @FXML
    private void handleEnter() {
        if (onEnter != null) onEnter.accept(selectedUserType);
        ((Stage) enterBtn.getScene().getWindow()).close();
    }
}
