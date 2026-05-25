package View;

import Model.Admin;
import Model.Date;
import Model.OwnerApplication;
import ViewModel.AdminViewModel;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class AdminController
{
    @FXML private ScrollPane applicationsScrollPane;
    @FXML private TilePane   applicationsTilePane;
    @FXML private Label      statusLabel;

    private Admin admin;
    private AdminViewModel adminVM;

    public void setAdmin(Admin admin) { this.admin = admin; }

    @FXML
    private void initialize()
    {
        adminVM = new AdminViewModel();
        statusLabel.textProperty().bind(adminVM.statusMessageProperty());
        adminVM.getApplications().addListener(
            (javafx.collections.ListChangeListener<OwnerApplication>) c -> rebuildCards());
        adminVM.loadApplications();
    }

    @FXML
    private void handleRefresh() { adminVM.loadApplications(); }

    private void rebuildCards()
    {
        applicationsTilePane.getChildren().clear();
        for (OwnerApplication app : adminVM.getApplications())
            applicationsTilePane.getChildren().add(createApplicationCard(app));
    }

    private VBox createApplicationCard(OwnerApplication app)
    {
        VBox card = new VBox(10);
        card.setPrefWidth(420);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 10, 0, 0, 2);");

        String badgeColor = switch (app.getStatus().toLowerCase()) {
            case "approved" -> "#1A5F3F"; case "rejected" -> "#B22222"; default -> "#8B7355";
        };
        Text statusBadge = new Text("\u25CF " + app.getStatus().toUpperCase());
        statusBadge.setStyle("-fx-fill: " + badgeColor + "; -fx-font-size: 11px; -fx-font-weight: bold;");

        Text appId = new Text("Application #" + app.getApplicationId());
        appId.setStyle("-fx-fill: #143D29; -fx-font-size: 16px; -fx-font-weight: bold;");

        Label clientLbl = new Label("Client ID: " + app.getClientId());
        clientLbl.setStyle("-fx-text-fill: #666666; -fx-font-size: 12px;");

        Label addrLbl = new Label("Property: " + app.getPropertyAddress());
        addrLbl.setStyle("-fx-text-fill: #143D29; -fx-font-size: 13px; -fx-font-weight: bold;");
        addrLbl.setWrapText(true);

        Label regLbl = new Label("Reg. No: " + app.getPropertyRegistrationNumber());
        regLbl.setStyle("-fx-text-fill: #555555; -fx-font-size: 12px;");

        Date d = app.getSubmissionDate();
        Label dateLbl = new Label(String.format("Submitted: %02d/%02d/%d", d.getDay(), d.getMonth(), d.getYear()));
        dateLbl.setStyle("-fx-text-fill: #8B7355; -fx-font-size: 11px;");

        card.getChildren().addAll(statusBadge, appId, clientLbl, addrLbl, regLbl, dateLbl);

        HBox btns = new HBox(10);
        btns.setAlignment(Pos.CENTER_LEFT);
        btns.setPadding(new Insets(8, 0, 0, 0));

        if ("pending".equalsIgnoreCase(app.getStatus())) {
            Button approveBtn = new Button("\u2713 Approve");
            approveBtn.setStyle("-fx-background-color: #1A5F3F; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5; -fx-padding: 7 18; -fx-cursor: hand;");
            approveBtn.setOnAction(e -> handleApprove(app));

            Button rejectBtn = new Button("\u2717 Reject");
            rejectBtn.setStyle("-fx-background-color: #B22222; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5; -fx-padding: 7 18; -fx-cursor: hand;");
            rejectBtn.setOnAction(e -> handleReject(app));

            btns.getChildren().addAll(approveBtn, rejectBtn);
        }

        Button removeBtn = new Button("\uD83D\uDDD1 Remove");
        removeBtn.setStyle("-fx-background-color: #555555; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5; -fx-padding: 7 18; -fx-cursor: hand;");
        removeBtn.setOnAction(e -> handleRemove(app));
        btns.getChildren().add(removeBtn);
        card.getChildren().add(btns);
        return card;
    }

    private void handleApprove(OwnerApplication app)
    {
        Alert c = new Alert(Alert.AlertType.CONFIRMATION,
            "Approve application #" + app.getApplicationId() + "? The client will become a Property Owner.",
            ButtonType.OK, ButtonType.CANCEL);
        c.setTitle("Approve Application");
        c.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK)
                adminVM.approveApplication(app.getApplicationId(), admin != null ? admin.getID() : 0);
        });
    }

    private void handleReject(OwnerApplication app)
    {
        Alert c = new Alert(Alert.AlertType.CONFIRMATION,
            "Reject application #" + app.getApplicationId() + "?",
            ButtonType.OK, ButtonType.CANCEL);
        c.setTitle("Reject Application");
        c.showAndWait().ifPresent(r -> { if (r == ButtonType.OK) adminVM.rejectApplication(app.getApplicationId()); });
    }

    private void handleRemove(OwnerApplication app)
    {
        Alert c = new Alert(Alert.AlertType.CONFIRMATION,
            "Remove application #" + app.getApplicationId() + "? Cannot be undone.",
            ButtonType.OK, ButtonType.CANCEL);
        c.setTitle("Remove Application");
        c.showAndWait().ifPresent(r -> { if (r == ButtonType.OK) adminVM.removeApplication(app.getApplicationId()); });
    }
}