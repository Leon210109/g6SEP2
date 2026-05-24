package View;

import Model.Client;
import Model.TenancyApplication;
import Persistence.TenancyApplicationDAO;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Optional;

public class MyApartmentsController {

    @FXML private ScrollPane appsScrollPane;
    @FXML private VBox appsContainer;
    @FXML private Label statusLabel;

    private final TenancyApplicationDAO tenancyDAO = new TenancyApplicationDAO();
    private Client client;

    public void setClient(Client client) {
        this.client = client;
        loadApplications();
    }

    @FXML
    private void initialize() { }

    private void loadApplications() {
        appsContainer.getChildren().clear();

        try {
            ArrayList<TenancyApplication> apps =
                tenancyDAO.getApplicationsByClientId(client.getID());

            if (apps.isEmpty()) {
                statusLabel.setText("You have not applied for any long-term apartments yet.");
                return;
            }

            for (TenancyApplication app : apps) {
                appsContainer.getChildren().add(createApplicationCard(app));
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
        card.setPadding(new Insets(20));
        card.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: #D4C4B0;" +
            "-fx-border-radius: 10;" +
            "-fx-border-width: 2;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);"
        );

        // Top row: listing address + status badge
        HBox topRow = new HBox(12);
        topRow.setAlignment(Pos.CENTER_LEFT);

        Text addressText = new Text(app.getListingAddress() != null ? app.getListingAddress() : "Unknown Address");
        addressText.setStyle(
            "-fx-fill: #143D29; -fx-font-family: 'Palatino Linotype';" +
            "-fx-font-size: 20px; -fx-font-weight: bold;"
        );

        String status = app.getStatus();
        String badgeColor = switch (status) {
            case "approved" -> "#1A5F3F";
            case "rejected" -> "#8B0000";
            default         -> "#8B7355";
        };
        String badgeText = switch (status) {
            case "approved" -> "Approved";
            case "rejected" -> "Rejected";
            default         -> "Pending Review";
        };
        Label statusBadge = new Label(badgeText);
        statusBadge.setStyle(
            "-fx-background-color: " + badgeColor + ";" +
            "-fx-text-fill: white; -fx-font-family: 'Cambria'; -fx-font-size: 12px;" +
            "-fx-font-weight: bold; -fx-background-radius: 4; -fx-padding: 4 10;"
        );

        topRow.getChildren().addAll(addressText, statusBadge);

        // Application details
        HBox detailsRow = new HBox(30);
        detailsRow.setAlignment(Pos.CENTER_LEFT);
        detailsRow.getChildren().addAll(
            makeDetail("Occupation", app.getOccupation()),
            makeDetail("Monthly Income", app.getMonthlyIncome() + " DKK"),
            makeDetail("Occupants", String.valueOf(app.getNumberOfOccupants())),
            makeDetail("Pets", app.isHasPets()
                ? "Yes" + (app.getPetsDescription() != null && !app.getPetsDescription().isEmpty()
                    ? " (" + app.getPetsDescription() + ")" : "")
                : "No")
        );

        card.getChildren().addAll(topRow, detailsRow);

        // Additional info
        if (app.getAdditionalInfo() != null && !app.getAdditionalInfo().isEmpty()) {
            Text noteText = new Text("Your note: " + app.getAdditionalInfo());
            noteText.setWrappingWidth(700);
            noteText.setStyle("-fx-fill: #5A4A3A; -fx-font-family: 'Cambria'; -fx-font-size: 12px; -fx-font-style: italic;");
            card.getChildren().add(noteText);
        }

        // Status message for approved/rejected
        if ("approved".equals(status)) {
            Label approvedMsg = new Label("Your application was approved! Contact the landlord to finalise the rental.");
            approvedMsg.setStyle("-fx-text-fill: #1A5F3F; -fx-font-family: 'Cambria'; -fx-font-size: 13px; -fx-font-weight: bold;");
            card.getChildren().add(approvedMsg);
        } else if ("rejected".equals(status)) {
            Label rejectedMsg = new Label("Your application was not accepted for this listing.");
            rejectedMsg.setStyle("-fx-text-fill: #8B0000; -fx-font-family: 'Cambria'; -fx-font-size: 13px;");
            card.getChildren().add(rejectedMsg);
        }

        // Withdraw button (only for pending)
        if ("pending".equals(status)) {
            Button withdrawBtn = new Button("Withdraw Application");
            withdrawBtn.setStyle(
                "-fx-background-color: #D4C4B0; -fx-text-fill: #143D29;" +
                "-fx-font-family: 'Cambria'; -fx-font-size: 12px;" +
                "-fx-background-radius: 5; -fx-padding: 6 16; -fx-cursor: hand;"
            );
            withdrawBtn.setOnAction(e -> handleWithdraw(app, card));
            card.getChildren().add(withdrawBtn);
        }

        return card;
    }

    private VBox makeDetail(String label, String value) {
        VBox box = new VBox(2);
        Label lbl = new Label(label);
        lbl.setStyle("-fx-text-fill: #8B7355; -fx-font-family: 'Cambria'; -fx-font-size: 11px;");
        Label val = new Label(value != null ? value : "N/A");
        val.setStyle("-fx-text-fill: #143D29; -fx-font-family: 'Cambria'; -fx-font-size: 13px; -fx-font-weight: bold;");
        box.getChildren().addAll(lbl, val);
        return box;
    }

    private void handleWithdraw(TenancyApplication app, VBox card) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Withdraw Application");
        confirm.setHeaderText("Withdraw your application for " + app.getListingAddress() + "?");
        confirm.setContentText("This cannot be undone.");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            tenancyDAO.deleteApplication(app.getId());
            appsContainer.getChildren().remove(card);
            int remaining = appsContainer.getChildren().size();
            statusLabel.setText(remaining == 0
                ? "You have not applied for any long-term apartments yet."
                : remaining + " application(s)");
        }
    }
}
