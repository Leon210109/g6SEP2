package View;

import Model.PropertyOwner;
import Model.TenancyApplication;
import Persistence.TenancyApplicationDAO;
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

import java.util.ArrayList;

public class TenantApplicationsController {

    @FXML private ScrollPane applicationsScrollPane;
    @FXML private TilePane applicationsTilePane;
    @FXML private Label statusLabel;

    private final TenancyApplicationDAO tenancyDAO = new TenancyApplicationDAO();
    private PropertyOwner propertyOwner;

    public void setPropertyOwner(PropertyOwner owner) {
        this.propertyOwner = owner;
        loadApplications();
    }

    @FXML
    private void initialize() {
        loadApplications();
    }

    private void loadApplications() {
        applicationsTilePane.getChildren().clear();

        if (propertyOwner == null) {
            statusLabel.setText("No owner data available.");
            return;
        }

        try {
            ArrayList<TenancyApplication> apps =
                tenancyDAO.getApplicationsByOwnerId(propertyOwner.getID());

            if (apps.isEmpty()) {
                statusLabel.setText("No tenant applications yet.");
                return;
            }

            for (TenancyApplication app : apps) {
                applicationsTilePane.getChildren().add(createApplicationCard(app));
            }
            statusLabel.setText(apps.size() + " application(s)");

        } catch (Exception e) {
            statusLabel.setText("Error loading applications: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private VBox createApplicationCard(TenancyApplication app) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.TOP_LEFT);
        card.setPadding(new Insets(18));
        card.setPrefWidth(420);
        card.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: #D4C4B0;" +
            "-fx-border-radius: 10;" +
            "-fx-border-width: 2;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);"
        );

        // Status badge
        Label statusBadge = new Label(app.getStatus().toUpperCase());
        String badgeColor = switch (app.getStatus()) {
            case "approved" -> "#1A5F3F";
            case "rejected" -> "#8B0000";
            default         -> "#8B7355";
        };
        statusBadge.setStyle(
            "-fx-background-color: " + badgeColor + ";" +
            "-fx-text-fill: white; -fx-font-family: 'Cambria'; -fx-font-size: 11px;" +
            "-fx-font-weight: bold; -fx-background-radius: 4; -fx-padding: 3 8;"
        );

        // Applicant
        Text applicantText = new Text(app.getClientName() != null ? app.getClientName() : "Unknown Applicant");
        applicantText.setStyle(
            "-fx-fill: #143D29; -fx-font-family: 'Palatino Linotype';" +
            "-fx-font-size: 18px; -fx-font-weight: bold;"
        );

        Text emailText = new Text(app.getClientEmail() != null ? app.getClientEmail() : "");
        emailText.setStyle("-fx-fill: #8B7355; -fx-font-family: 'Cambria'; -fx-font-size: 12px;");

        Text listingText = new Text("Listing: " + (app.getListingAddress() != null ? app.getListingAddress() : "N/A"));
        listingText.setStyle("-fx-fill: #1A5F3F; -fx-font-family: 'Cambria'; -fx-font-size: 12px; -fx-font-weight: bold;");

        // Details
        VBox detailsBox = new VBox(4);
        detailsBox.getChildren().addAll(
            makeDetailRow("Occupation:", app.getOccupation()),
            makeDetailRow("Monthly Income:", app.getMonthlyIncome() + " DKK"),
            makeDetailRow("Occupants:", String.valueOf(app.getNumberOfOccupants())),
            makeDetailRow("Pets:", app.isHasPets()
                ? "Yes — " + (app.getPetsDescription() != null ? app.getPetsDescription() : "")
                : "No")
        );
        if (app.getAdditionalInfo() != null && !app.getAdditionalInfo().isEmpty()) {
            Text moreInfo = new Text("Note: " + app.getAdditionalInfo());
            moreInfo.setWrappingWidth(380);
            moreInfo.setStyle("-fx-fill: #5A4A3A; -fx-font-family: 'Cambria'; -fx-font-size: 12px; -fx-font-style: italic;");
            detailsBox.getChildren().add(moreInfo);
        }

        card.getChildren().addAll(statusBadge, applicantText, emailText, listingText, detailsBox);

        // Approve/Reject buttons only for pending
        if ("pending".equals(app.getStatus())) {
            HBox btnRow = new HBox(10);
            btnRow.setAlignment(Pos.CENTER_LEFT);

            Button approveBtn = new Button("Approve");
            approveBtn.setStyle(
                "-fx-background-color: #1A5F3F; -fx-text-fill: white;" +
                "-fx-font-family: 'Cambria'; -fx-font-size: 12px;" +
                "-fx-background-radius: 5; -fx-padding: 6 18; -fx-cursor: hand;"
            );
            approveBtn.setOnAction(e -> updateStatus(app, "approved", card));

            Button rejectBtn = new Button("Reject");
            rejectBtn.setStyle(
                "-fx-background-color: #8B0000; -fx-text-fill: white;" +
                "-fx-font-family: 'Cambria'; -fx-font-size: 12px;" +
                "-fx-background-radius: 5; -fx-padding: 6 18; -fx-cursor: hand;"
            );
            rejectBtn.setOnAction(e -> updateStatus(app, "rejected", card));

            btnRow.getChildren().addAll(approveBtn, rejectBtn);
            card.getChildren().add(btnRow);
        }

        return card;
    }

    private HBox makeDetailRow(String label, String value) {
        HBox row = new HBox(8);
        Label lbl = new Label(label);
        lbl.setStyle("-fx-text-fill: #8B7355; -fx-font-family: 'Cambria'; -fx-font-size: 12px; -fx-font-weight: bold;");
        Label val = new Label(value != null ? value : "N/A");
        val.setStyle("-fx-text-fill: #143D29; -fx-font-family: 'Cambria'; -fx-font-size: 12px;");
        row.getChildren().addAll(lbl, val);
        return row;
    }

    private void updateStatus(TenancyApplication app, String status, VBox card) {
        tenancyDAO.updateStatus(app.getId(), status);
        // Refresh to show updated badge
        loadApplications();
    }
}
