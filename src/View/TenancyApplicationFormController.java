package View;

import Model.Client;
import Model.Listing;
import Model.TenancyApplication;
import ViewModel.TenancyViewModel;
import ViewModel.ViewModelFactory;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TenancyApplicationFormController {

    @FXML private Label    listingAddressLabel;
    @FXML private TextField occupationField;
    @FXML private TextField monthlyIncomeField;
    @FXML private TextField numberOfOccupantsField;
    @FXML private CheckBox  hasPetsCheckBox;
    @FXML private VBox      petsDescBox;
    @FXML private TextField petsDescriptionField;
    @FXML private TextArea  additionalInfoArea;
    @FXML private Label     messageLabel;

    private TenancyViewModel tenancyViewModel;
    private Listing listing;
    private Client  client;
    private Runnable onSuccess;

    public void setApplicationInfo(Listing listing, Client client) {
        this.listing = listing;
        this.client  = client;
        listingAddressLabel.setText(listing.getStreet() + ", " + listing.getRegion() + ", " + listing.getCountry());
        numberOfOccupantsField.setText("1");
    }

    public void setOnSuccess(Runnable callback) { this.onSuccess = callback; }

    @FXML
    private void initialize() {
        tenancyViewModel = ViewModelFactory.getInstance().getTenancyViewModel();
        monthlyIncomeField.textProperty().addListener((o, v, n) -> { if (!n.matches("\\d*")) monthlyIncomeField.setText(v); });
        numberOfOccupantsField.textProperty().addListener((o, v, n) -> { if (!n.matches("\\d*")) numberOfOccupantsField.setText(v); });
        tenancyViewModel.submissionSuccessProperty().addListener((obs, o, ok) -> {
            if (ok) {
                tenancyViewModel.resetSubmissionSuccess();
                messageLabel.setStyle("-fx-text-fill: #1A5F3F; -fx-font-weight: bold;");
                messageLabel.setText("Application submitted! The landlord will review it.");
                if (onSuccess != null) onSuccess.run();
                new Thread(() -> {
                    try { Thread.sleep(1200); } catch (InterruptedException ignored) {}
                    javafx.application.Platform.runLater(this::closeWindow);
                }).start();
            }
        });
        tenancyViewModel.errorMessageProperty().addListener((obs, o, msg) -> {
            if (msg != null && !msg.isEmpty()) messageLabel.setText(msg);
        });
    }

    @FXML private void handlePetsToggle() {
        boolean hp = hasPetsCheckBox.isSelected();
        petsDescBox.setVisible(hp); petsDescBox.setManaged(hp);
    }

    @FXML
    private void handleSubmit() {
        messageLabel.setText("");
        String occ  = occupationField.getText().trim();
        String inc  = monthlyIncomeField.getText().trim();
        String ocp  = numberOfOccupantsField.getText().trim();
        if (occ.isEmpty()) { messageLabel.setText("Occupation is required."); return; }
        if (inc.isEmpty()) { messageLabel.setText("Monthly income is required."); return; }
        if (ocp.isEmpty()) { messageLabel.setText("Number of occupants is required."); return; }
        boolean hasPets = hasPetsCheckBox.isSelected();
        String petsDesc = hasPets ? petsDescriptionField.getText().trim() : "";
        if (hasPets && petsDesc.isEmpty()) { messageLabel.setText("Please describe your pets."); return; }
        try {
            TenancyApplication app = new TenancyApplication(
                client.getID(), listing.getId(), hasPets, petsDesc,
                occ, Integer.parseInt(inc), Integer.parseInt(ocp), additionalInfoArea.getText().trim());
            tenancyViewModel.submitApplication(app);
        } catch (NumberFormatException e) { messageLabel.setText("Income and occupants must be valid numbers."); }
    }

    @FXML private void handleCancel() { closeWindow(); }
    private void closeWindow() { ((Stage) messageLabel.getScene().getWindow()).close(); }
}

