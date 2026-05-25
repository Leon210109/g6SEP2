package View;

import Model.Admin;
import Model.Listing;
import Model.PropertyOwner;
import Persistence.ListingDAO;
import Persistence.PropertyOwnerDAO;
import Util.ImageConverter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class AdminListingsController {

    @FXML private ScrollPane listingsScrollPane;
    @FXML private TilePane listingsTilePane;
    @FXML private Label statusLabel;

    private final ListingDAO listingDAO = new ListingDAO();
    private final PropertyOwnerDAO ownerDAO = new PropertyOwnerDAO();
    private Admin admin;

    public void setAdmin(Admin admin) {
        this.admin = admin;
        loadListings();
    }

    @FXML
    private void initialize() {
        loadListings();
    }

    private void loadListings() {
        try {
            listingsTilePane.getChildren().clear();
            ArrayList<Listing> listings = listingDAO.getAllListings();

            if (listings.isEmpty()) {
                statusLabel.setText("No listings in the system.");
                return;
            }

            for (Listing listing : listings) {
                listingsTilePane.getChildren().add(createListingCard(listing));
            }
            statusLabel.setText(listings.size() + " listing(s) total");

        } catch (Exception e) {
            statusLabel.setText("Error loading listings: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private VBox createListingCard(Listing listing) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPadding(new Insets(15));
        card.setPrefWidth(280);
        card.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: #D4C4B0;" +
            "-fx-border-radius: 10;" +
            "-fx-border-width: 2;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);"
        );

        ImageView imageView = new ImageView();
        imageView.setFitWidth(250);
        imageView.setFitHeight(140);
        imageView.setPreserveRatio(true);
        loadImage(imageView, listing.getStreet());

        Text streetText = new Text(listing.getStreet());
        streetText.setStyle(
            "-fx-fill: #143D29; -fx-font-family: 'Palatino Linotype';" +
            "-fx-font-size: 17px; -fx-font-weight: bold;"
        );

        // Owner info
        String ownerName = "Unknown Owner";
        try {
            PropertyOwner owner = ownerDAO.getPropertyOwnerById(listing.getOwnerId());
            if (owner != null) {
                ownerName = owner.getFirstName() + " " + owner.getLastName();
            }
        } catch (Exception ignored) { }
        Text ownerText = new Text("Owner: " + ownerName);
        ownerText.setStyle("-fx-fill: #8B7355; -fx-font-family: 'Cambria'; -fx-font-size: 12px;");

        Text locationText = new Text(listing.getRegion() + ", " + listing.getCountry());
        locationText.setStyle("-fx-fill: #8B7355; -fx-font-family: 'Cambria'; -fx-font-size: 12px;");

        Text detailsText = new Text(
            listing.getNumberOfRooms() + " rooms · " +
            listing.getSurfaceArea() + "m² · " +
            listing.getPrice() + " DKK/month"
        );
        detailsText.setStyle(
            "-fx-fill: #1A5F3F; -fx-font-family: 'Cambria';" +
            "-fx-font-size: 12px; -fx-font-weight: bold;"
        );

        HBox buttonRow = new HBox(8);
        buttonRow.setAlignment(Pos.CENTER);

        Button editBtn = new Button("Edit");
        editBtn.setStyle(
            "-fx-background-color: #6B4A2A; -fx-text-fill: white;" +
            "-fx-font-family: 'Cambria'; -fx-font-size: 12px;" +
            "-fx-background-radius: 5; -fx-padding: 6 18; -fx-cursor: hand;"
        );
        editBtn.setOnAction(e -> openEditListing(listing));

        Button deleteBtn = new Button("Delete");
        deleteBtn.setStyle(
            "-fx-background-color: #8B0000; -fx-text-fill: white;" +
            "-fx-font-family: 'Cambria'; -fx-font-size: 12px;" +
            "-fx-background-radius: 5; -fx-padding: 6 18; -fx-cursor: hand;"
        );
        deleteBtn.setOnAction(e -> handleDelete(listing, card));

        buttonRow.getChildren().addAll(editBtn, deleteBtn);
        card.getChildren().addAll(imageView, streetText, ownerText, locationText, detailsText, buttonRow);
        return card;
    }

    private void openEditListing(Listing listing) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EditListingView.fxml"));
            Parent root = loader.load();
            EditListingController ctrl = loader.getController();
            // Pass null as owner — admin is editing, owner details not needed for update
            PropertyOwner owner = null;
            try { owner = ownerDAO.getPropertyOwnerById(listing.getOwnerId()); } catch (Exception ignored) { }
            ctrl.setListing(listing, owner);
            ctrl.setOnSuccess(() -> loadListings());

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Edit Listing - " + listing.getStreet());
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            statusLabel.setText("Error opening edit form: " + e.getMessage());
        }
    }

    private void handleDelete(Listing listing, VBox card) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Listing");
        confirm.setHeaderText("Delete listing at " + listing.getStreet() + "?");
        confirm.setContentText("This action cannot be undone. All associated bookings and favourites will also be removed.");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            listingDAO.deleteListing(listing.getId());
            listingsTilePane.getChildren().remove(card);
            int remaining = listingsTilePane.getChildren().size();
            statusLabel.setText(remaining + " listing(s) total");
        }
    }

    private void loadImage(ImageView imageView, String streetName) {
        try {
            File folder = new File("room_rental_img/" + streetName);
            if (folder.exists() && folder.isDirectory()) {
                ImageConverter.convertWebPFilesInFolder(folder.getAbsolutePath());
                File[] files = folder.listFiles((dir, name) -> {
                    String lower = name.toLowerCase();
                    return lower.endsWith(".jpg") || lower.endsWith(".jpeg") || lower.endsWith(".png");
                });
                if (files != null && files.length > 0) {
                    imageView.setImage(new Image(files[0].toURI().toString()));
                    return;
                }
            }
            imageView.setStyle("-fx-background-color: #E0E0E0;");
        } catch (Exception e) {
            imageView.setStyle("-fx-background-color: #E0E0E0;");
        }
    }
}
