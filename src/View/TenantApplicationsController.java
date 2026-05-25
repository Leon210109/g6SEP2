package View;

import Model.PropertyOwner;
import Model.TenancyApplication;
import ViewModel.TenancyViewModel;
import ViewModel.ViewModelFactory;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class TenantApplicationsController {

    @FXML private ScrollPane applicationsScrollPane;
    @FXML private TilePane   applicationsTilePane;
    @FXML private Label      statusLabel;

    private TenancyViewModel tenancyViewModel;
    private PropertyOwner propertyOwner;

    public void setPropertyOwner(PropertyOwner owner) {
        this.propertyOwner = owner;
        tenancyViewModel.loadApplicationsByOwner(owner.getID());
    }

    @FXML
    private void initialize() {
        tenancyViewModel = ViewModelFactory.getInstance().getTenancyViewModel();
        tenancyViewModel.getApplications().addListener(
            (javafx.collections.ListChangeListener<TenancyApplication>) c -> rebuildTiles());
    }

    private void rebuildTiles() {
        applicationsTilePane.getChildren().clear();
        if (tenancyViewModel.getApplications().isEmpty()) {
            statusLabel.setText("No tenant applications yet.");
            return;
        }
        statusLabel.setText(tenancyViewModel.getApplications().size() + " application(s)");
        for (TenancyApplication app : tenancyViewModel.getApplications())
            applicationsTilePane.getChildren().add(createApplicationCard(app));
    }

    private VBox createApplicationCard(TenancyApplication app) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.TOP_LEFT);
        card.setPadding(new Insets(18));
        card.setPrefWidth(420);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: #D4C4B0; -fx-border-radius: 10; -fx-border-width: 2;");

        String badgeColor = switch (app.getStatus()) {
            case "approved" -> "#1A5F3F"; case "rejected" -> "#8B0000"; default -> "#8B7355";
        };
        Label statusBadge = new Label(app.getStatus().toUpperCase());
        statusBadge.setStyle("-fx-background-color: " + badgeColor + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 4; -fx-padding: 3 8;");

        Text applicant = new Text(app.getClientName() != null ? app.getClientName() : "Unknown Applicant");
        applicant.setStyle("-fx-fill: #143D29; -fx-font-size: 18px; -fx-font-weight: bold;");
        Text email = new Text(app.getClientEmail() != null ? app.getClientEmail() : "");
        email.setStyle("-fx-fill: #8B7355; -fx-font-size: 12px;");
        Text listing = new Text("Listing: " + (app.getListingAddress() != null ? app.getListingAddress() : "N/A"));
        listing.setStyle("-fx-fill: #1A5F3F; -fx-font-size: 12px; -fx-font-weight: bold;");

        VBox details = new VBox(4,
            makeDetailRow("Occupation:", app.getOccupation()),
            makeDetailRow("Monthly Income:", app.getMonthlyIncome() + " DKK"),
            makeDetailRow("Occupants:", String.valueOf(app.getNumberOfOccupants())),
            makeDetailRow("Pets:", app.isHasPets() ? "Yes — " + (app.getPetsDescription() != null ? app.getPetsDescription() : "") : "No")
        );
        if (app.getAdditionalInfo() != null && !app.getAdditionalInfo().isEmpty()) {
            Text note = new Text("Note: " + app.getAdditionalInfo());
            note.setWrappingWidth(380);
            note.setStyle("-fx-fill: #5A4A3A; -fx-font-size: 12px; -fx-font-style: italic;");
            details.getChildren().add(note);
        }

        card.getChildren().addAll(statusBadge, applicant, email, listing, details);

        if ("pending".equals(app.getStatus())) {
            Button approveBtn = new Button("Approve");
            approveBtn.setStyle("-fx-background-color: #1A5F3F; -fx-text-fill: white; -fx-background-radius: 5; -fx-padding: 6 18; -fx-cursor: hand;");
            approveBtn.setOnAction(e -> tenancyViewModel.updateApplicationStatus(app.getId(), "approved"));
            Button rejectBtn = new Button("Reject");
            rejectBtn.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white; -fx-background-radius: 5; -fx-padding: 6 18; -fx-cursor: hand;");
            rejectBtn.setOnAction(e -> tenancyViewModel.updateApplicationStatus(app.getId(), "rejected"));
            HBox btns = new HBox(10, approveBtn, rejectBtn);
            btns.setAlignment(Pos.CENTER_LEFT);
            card.getChildren().add(btns);
        }
        return card;
    }

    private HBox makeDetailRow(String label, String value) {
        Label lbl = new Label(label);
        lbl.setStyle("-fx-text-fill: #8B7355; -fx-font-weight: bold; -fx-font-size: 12px;");
        Label val = new Label(value != null ? value : "N/A");
        val.setStyle("-fx-text-fill: #143D29; -fx-font-size: 12px;");
        HBox row = new HBox(8, lbl, val);
        return row;
    }
}
