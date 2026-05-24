package View;

import Model.Admin;
import Model.Client;
import Model.Date;
import Model.OwnerApplication;
import Model.PropertyOwner;
import Persistence.ClientDAO;
import Persistence.OwnerApplicationDAO;
import Persistence.PropertyOwnerDAO;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class AdminController {

    @FXML private ScrollPane applicationsScrollPane;
    @FXML private TilePane applicationsTilePane;
    @FXML private Label statusLabel;

    private Admin admin;
    private final OwnerApplicationDAO applicationDAO = new OwnerApplicationDAO();
    private final ClientDAO clientDAO = new ClientDAO();
    private final PropertyOwnerDAO ownerDAO = new PropertyOwnerDAO();

    public void setAdmin(Admin admin) {
        this.admin = admin;
        loadApplications();
    }

    @FXML
    private void initialize() {
        // Data loaded when setAdmin is called (or directly if admin bypass)
        loadApplications();
    }

    private void loadApplications() {
        try {
            ArrayList<OwnerApplication> applications = applicationDAO.getAllApplications();

            applicationsTilePane.getChildren().clear();

            if (applications.isEmpty()) {
                statusLabel.setText("No owner applications found.");
                return;
            }

            for (OwnerApplication app : applications) {
                applicationsTilePane.getChildren().add(createApplicationCard(app));
            }

            statusLabel.setText(applications.size() + " application(s) total");

        } catch (Exception e) {
            statusLabel.setText("Error loading applications: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private VBox createApplicationCard(OwnerApplication app) {
        VBox card = new VBox(10);
        card.setPrefWidth(420);
        card.setPadding(new Insets(20));
        card.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 8;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 10, 0, 0, 2);"
        );

        // Status badge color
        String badgeColor = switch (app.getStatus().toLowerCase()) {
            case "approved" -> "#1A5F3F";
            case "rejected" -> "#B22222";
            default -> "#8B7355"; // pending
        };

        Text statusBadge = new Text("● " + app.getStatus().toUpperCase());
        statusBadge.setStyle("-fx-fill: " + badgeColor + "; -fx-font-size: 11px; -fx-font-weight: bold;");

        Text appIdText = new Text("Application #" + app.getApplicationId());
        appIdText.setStyle("-fx-fill: #143D29; -fx-font-size: 16px; -fx-font-weight: bold;");

        Label clientLabel = new Label("Client ID: " + app.getClientId());
        clientLabel.setStyle("-fx-text-fill: #666666; -fx-font-size: 12px;");

        // Try to load client name
        try {
            Client client = clientDAO.getClientById(app.getClientId());
            if (client != null) {
                clientLabel.setText("Applicant: " + client.getFirstName() + " " + client.getLastName()
                    + " (" + client.getEmail() + ")");
            }
        } catch (Exception ignored) {}

        Label addressLabel = new Label("Property: " + app.getPropertyAddress());
        addressLabel.setStyle("-fx-text-fill: #143D29; -fx-font-size: 13px; -fx-font-weight: bold;");
        addressLabel.setWrapText(true);

        Label regLabel = new Label("Reg. No: " + app.getPropertyRegistrationNumber());
        regLabel.setStyle("-fx-text-fill: #555555; -fx-font-size: 12px;");

        Date d = app.getSubmissionDate();
        Label dateLabel = new Label(String.format("Submitted: %02d/%02d/%d",
            d.getDay(), d.getMonth(), d.getYear()));
        dateLabel.setStyle("-fx-text-fill: #8B7355; -fx-font-size: 11px;");

        card.getChildren().addAll(statusBadge, appIdText, clientLabel, addressLabel, regLabel, dateLabel);

        // Action buttons only for pending applications
        if ("pending".equalsIgnoreCase(app.getStatus())) {
            HBox buttonBox = new HBox(10);
            buttonBox.setAlignment(Pos.CENTER_LEFT);
            buttonBox.setPadding(new Insets(8, 0, 0, 0));

            Button approveBtn = new Button("✓ Approve");
            approveBtn.setStyle(
                "-fx-background-color: #1A5F3F; -fx-text-fill: white;" +
                "-fx-font-size: 12px; -fx-font-weight: bold;" +
                "-fx-background-radius: 5; -fx-padding: 7 18; -fx-cursor: hand;"
            );
            approveBtn.setOnAction(e -> handleApprove(app, card, statusBadge));

            Button rejectBtn = new Button("✗ Reject");
            rejectBtn.setStyle(
                "-fx-background-color: #B22222; -fx-text-fill: white;" +
                "-fx-font-size: 12px; -fx-font-weight: bold;" +
                "-fx-background-radius: 5; -fx-padding: 7 18; -fx-cursor: hand;"
            );
            rejectBtn.setOnAction(e -> handleReject(app, card, statusBadge, buttonBox));

            buttonBox.getChildren().addAll(approveBtn, rejectBtn);
            card.getChildren().add(buttonBox);
        }

        return card;
    }

    private void handleApprove(OwnerApplication app, VBox card, Text statusBadge) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Approve Application");
        confirm.setHeaderText("Approve application #" + app.getApplicationId() + "?");
        confirm.setContentText("The client will be registered as a Property Owner.");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    // 1. Update application status
                    applicationDAO.updateApplicationStatus(app.getApplicationId(), "approved");

                    // 2. Create PropertyOwner from Client data
                    Client client = clientDAO.getClientById(app.getClientId());
                    if (client != null) {
                        PropertyOwner newOwner = new PropertyOwner(
                            client.getFirstName(),
                            client.getLastName(),
                            client.getEmail(),
                            client.getPhoneNumber(),
                            client.getUsername(),
                            client.getPassword(),
                            client.getDOB(),
                            client.getGender(),
                            client.getNationality()
                        );
                        ownerDAO.CreatePropertyOwner(newOwner);
                    }

                    // 3. Update card UI
                    statusBadge.setText("● APPROVED");
                    statusBadge.setStyle("-fx-fill: #1A5F3F; -fx-font-size: 11px; -fx-font-weight: bold;");
                    // Remove the button row (last child)
                    if (!card.getChildren().isEmpty()) {
                        card.getChildren().remove(card.getChildren().size() - 1);
                    }
                    statusLabel.setText("Application approved successfully.");

                } catch (Exception e) {
                    statusLabel.setText("Error approving application: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    private void handleReject(OwnerApplication app, VBox card, Text statusBadge, HBox buttonBox) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Reject Application");
        confirm.setHeaderText("Reject application #" + app.getApplicationId() + "?");
        confirm.setContentText("The application will be marked as rejected.");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    applicationDAO.updateApplicationStatus(app.getApplicationId(), "rejected");

                    statusBadge.setText("● REJECTED");
                    statusBadge.setStyle("-fx-fill: #B22222; -fx-font-size: 11px; -fx-font-weight: bold;");
                    card.getChildren().remove(buttonBox);
                    statusLabel.setText("Application rejected.");

                } catch (Exception e) {
                    statusLabel.setText("Error rejecting application: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }
}
