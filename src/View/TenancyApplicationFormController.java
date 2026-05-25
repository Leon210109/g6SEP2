package View;

import Model.Client;
import Model.Listing;
import Model.TenancyApplication;
import Persistence.TenancyApplicationDAO;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TenancyApplicationFormController {

    @FXML private Label listingAddressLabel;
    @FXML private TextField occupationField;
    @FXML private TextField monthlyIncomeField;
    @FXML private TextField numberOfOccupantsField;
    @FXML private CheckBox hasPetsCheckBox;
    @FXML private VBox petsDescBox;
    @FXML private TextField petsDescriptionField;
    @FXML private TextArea additionalInfoArea;
    @FXML private Label messageLabel;

    private final TenancyApplicationDAO tenancyDAO = new TenancyApplicationDAO();
    private Listing listing;
    private Client client;
    private Runnable onSuccess;

    public void setApplicationInfo(Listing listing, Client client) {
        this.listing = listing;
        this.client = client;
        listingAddressLabel.setText(listing.getStreet() + ", " + listing.getRegion() + ", " + listing.getCountry());
        // Pre-fill occupants with 1
        numberOfOccupantsField.setText("1");
    }

    public void setOnSuccess(Runnable callback) {
        this.onSuccess = callback;
    }

    @FXML
    private void initialize() {
        // Numeric validation
        monthlyIncomeField.textProperty().addListener((obs, o, n) -> {
            if (!n.matches("\\d*")) monthlyIncomeField.setText(o);
        });
        numberOfOccupantsField.textProperty().addListener((obs, o, n) -> {
            if (!n.matches("\\d*")) numberOfOccupantsField.setText(o);
        });
    }

    @FXML
    private void handlePetsToggle() {
        boolean hasPets = hasPetsCheckBox.isSelected();
        petsDescBox.setVisible(hasPets);
        petsDescBox.setManaged(hasPets);
    }

    @FXML
    private void handleSubmit() {
        messageLabel.setText("");

        String occupation = occupationField.getText().trim();
        String incomeStr  = monthlyIncomeField.getText().trim();
        String occupantsStr = numberOfOccupantsField.getText().trim();

        if (occupation.isEmpty()) {
            messageLabel.setText("Occupation is required.");
            return;
        }
        if (incomeStr.isEmpty()) {
            messageLabel.setText("Monthly income is required.");
            return;
        }
        if (occupantsStr.isEmpty()) {
            messageLabel.setText("Number of occupants is required.");
            return;
        }

        boolean hasPets = hasPetsCheckBox.isSelected();
        String petsDesc = hasPets ? petsDescriptionField.getText().trim() : "";
        if (hasPets && petsDesc.isEmpty()) {
            messageLabel.setText("Please describe your pets.");
            return;
        }

        int monthlyIncome;
        int numberOfOccupants;
        try {
            monthlyIncome = Integer.parseInt(incomeStr);
            numberOfOccupants = Integer.parseInt(occupantsStr);
        } catch (NumberFormatException e) {
            messageLabel.setText("Income and occupants must be valid numbers.");
            return;
        }

        String additionalInfo = additionalInfoArea.getText().trim();

        TenancyApplication app = new TenancyApplication(
            client.getID(),
            listing.getId(),
            hasPets,
            petsDesc,
            occupation,
            monthlyIncome,
            numberOfOccupants,
            additionalInfo
        );

        tenancyDAO.createApplication(app);

        messageLabel.setStyle("-fx-text-fill: #1A5F3F; -fx-font-weight: bold;");
        messageLabel.setText("Application submitted! The landlord will review it.");

        if (onSuccess != null) {
            onSuccess.run();
        }

        // Close after a brief moment
        new Thread(() -> {
            try { Thread.sleep(1200); } catch (InterruptedException ignored) {}
            javafx.application.Platform.runLater(this::closeWindow);
        }).start();
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) messageLabel.getScene().getWindow();
        stage.close();
    }
}
