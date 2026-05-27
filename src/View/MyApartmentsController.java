package View;

import Model.Client;
import Model.TenancyApplication;
import ViewModel.TenancyViewModel;
import ViewModel.ViewModelFactory;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class MyApartmentsController {

    @FXML private ScrollPane appsScrollPane;
    @FXML private VBox       appsContainer;
    @FXML private Label      statusLabel;

    private TenancyViewModel tenancyViewModel;
    private Client client;

    public void setClient(Client client) {
        this.client = client;
        tenancyViewModel.loadApplicationsByClient(client.getID());
    }

    @FXML
    private void initialize() {
        tenancyViewModel = ViewModelFactory.getInstance().getTenancyViewModel();
        tenancyViewModel.getApplications().addListener(
            (javafx.collections.ListChangeListener<TenancyApplication>) c -> rebuildCards());
    }

    private void rebuildCards() {
        appsContainer.getChildren().clear();
        if (tenancyViewModel.getApplications().isEmpty()) {
            statusLabel.setText("You have not applied for any long-term apartments yet.");
            return;
        }
        statusLabel.setText(tenancyViewModel.getApplications().size() + " application(s)");
        for (TenancyApplication app : tenancyViewModel.getApplications())
            appsContainer.getChildren().add(createApplicationCard(app));
    }

    private VBox createApplicationCard(TenancyApplication app) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.TOP_LEFT);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: #D4C4B0; -fx-border-radius: 10; -fx-border-width: 2;");

        String status    = app.getStatus();
        String badgeColor = switch (status) { case "approved" -> "#1A5F3F"; case "rejected" -> "#8B0000"; default -> "#8B7355"; };
        String badgeText  = switch (status) { case "approved" -> "Approved"; case "rejected" -> "Rejected"; default -> "Pending Review"; };
        Label statusBadge = new Label(badgeText);
        statusBadge.setStyle("-fx-background-color: " + badgeColor + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 4; -fx-padding: 4 10;");

        Text address = new Text(app.getListingAddress() != null ? app.getListingAddress() : "Unknown Address");
        address.setStyle("-fx-fill: #143D29; -fx-font-size: 20px; -fx-font-weight: bold;");

        HBox topRow = new HBox(12, address, statusBadge);
        topRow.setAlignment(Pos.CENTER_LEFT);

        HBox details = new HBox(30,
            makeDetail("Occupation", app.getOccupation()),
            makeDetail("Monthly Income", app.getMonthlyIncome() + " DKK"),
            makeDetail("Occupants", String.valueOf(app.getNumberOfOccupants())),
            makeDetail("Pets", app.isHasPets() ? "Yes" + (app.getPetsDescription() != null && !app.getPetsDescription().isEmpty() ? " (" + app.getPetsDescription() + ")" : "") : "No")
        );
        details.setAlignment(Pos.CENTER_LEFT);
        card.getChildren().addAll(topRow, details);

        if (app.getAdditionalInfo() != null && !app.getAdditionalInfo().isEmpty()) {
            Text note = new Text("Your note: " + app.getAdditionalInfo());
            note.setWrappingWidth(700);
            note.setStyle("-fx-fill: #5A4A3A; -fx-font-size: 12px; -fx-font-style: italic;");
            card.getChildren().add(note);
        }
        if ("approved".equals(status)) { Label m = new Label("Your application was approved!"); m.setStyle("-fx-text-fill: #1A5F3F; -fx-font-weight: bold;"); card.getChildren().add(m); }
        if ("rejected".equals(status)) { Label m = new Label("Your application was not accepted."); m.setStyle("-fx-text-fill: #8B0000;"); card.getChildren().add(m); }

        if ("pending".equals(status)) {
            Button withdrawBtn = new Button("Withdraw Application");
            withdrawBtn.setStyle("-fx-background-color: #D4C4B0; -fx-text-fill: #143D29; -fx-background-radius: 5; -fx-padding: 6 16; -fx-cursor: hand;");
            withdrawBtn.setOnAction(e -> {
                Alert c = new Alert(Alert.AlertType.CONFIRMATION, "Withdraw application for " + app.getListingAddress() + "? Cannot be undone.", ButtonType.OK, ButtonType.CANCEL);
                c.setTitle("Withdraw Application");
                c.showAndWait().ifPresent(r -> { if (r == ButtonType.OK) tenancyViewModel.deleteApplication(app.getId()); });
            });
            card.getChildren().add(withdrawBtn);
        }
        return card;
    }

    private VBox makeDetail(String label, String value) {
        Label lbl = new Label(label); lbl.setStyle("-fx-text-fill: #8B7355; -fx-font-size: 11px;");
        Label val = new Label(value != null ? value : "N/A"); val.setStyle("-fx-text-fill: #143D29; -fx-font-size: 13px; -fx-font-weight: bold;");
        return new VBox(2, lbl, val);
    }
}
