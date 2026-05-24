package View;

import Model.Client;
import Model.Listing;
import Model.PropertyOwner;
import Persistence.FavoriteDAO;
import Persistence.ListingDAO;
import Persistence.PropertyOwnerDAO;
import Util.ImageConverter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

public class MyFavoritesController {

    @FXML private ScrollPane favScrollPane;
    @FXML private TilePane favTilePane;
    @FXML private Label statusLabel;

    private final FavoriteDAO favoriteDAO = new FavoriteDAO();
    private final PropertyOwnerDAO ownerDAO = new PropertyOwnerDAO();
    private Client client;

    public void setClient(Client client) {
        this.client = client;
        loadFavorites();
    }

    @FXML
    private void initialize() { }

    private void loadFavorites() {
        try {
            ArrayList<Listing> favorites = favoriteDAO.getFavoriteListingsByClientId(client.getID());

            favTilePane.getChildren().clear();

            if (favorites.isEmpty()) {
                statusLabel.setText("You haven't saved any favourites yet. Browse listings to add some!");
                statusLabel.setStyle("-fx-text-fill: #8B7355; -fx-font-size: 14px;");
                return;
            }

            for (Listing listing : favorites) {
                favTilePane.getChildren().add(createFavCard(listing));
            }
            statusLabel.setText(favorites.size() + " saved listing(s)");
            statusLabel.setStyle("-fx-text-fill: #8B7355; -fx-font-size: 13px;");

        } catch (Exception e) {
            statusLabel.setText("Error loading favourites: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private VBox createFavCard(Listing listing) {
        VBox card = new VBox(12);
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
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);
        loadImage(imageView, listing.getStreet());

        Text streetText = new Text(listing.getStreet());
        streetText.setStyle(
            "-fx-fill: #143D29; -fx-font-family: 'Palatino Linotype';" +
            "-fx-font-size: 18px; -fx-font-weight: bold;"
        );

        Text locationText = new Text(listing.getRegion() + ", " + listing.getCountry());
        locationText.setStyle("-fx-fill: #8B7355; -fx-font-family: 'Cambria'; -fx-font-size: 13px;");

        Text detailsText = new Text(
            listing.getNumberOfRooms() + " rooms · " +
            listing.getSurfaceArea() + "m² · " +
            listing.getPrice() + " DKK/month"
        );
        detailsText.setStyle(
            "-fx-fill: #1A5F3F; -fx-font-family: 'Cambria';" +
            "-fx-font-size: 12px; -fx-font-weight: bold;"
        );

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button removeBtn = new Button("♥ Remove");
        removeBtn.setStyle(
            "-fx-background-color: #B22222; -fx-text-fill: white;" +
            "-fx-font-family: 'Cambria'; -fx-font-size: 12px;" +
            "-fx-background-radius: 5; -fx-padding: 6 14; -fx-cursor: hand;"
        );
        removeBtn.setOnAction(e -> handleRemoveFavorite(listing, card));

        Button viewBtn = new Button("View Details");
        viewBtn.setStyle(
            "-fx-background-color: #1A5F3F; -fx-text-fill: white;" +
            "-fx-font-family: 'Cambria'; -fx-font-size: 12px;" +
            "-fx-background-radius: 5; -fx-padding: 6 14; -fx-cursor: hand;"
        );
        viewBtn.setOnAction(e -> openDetails(listing));

        buttonBox.getChildren().addAll(removeBtn, viewBtn);
        card.getChildren().addAll(imageView, streetText, locationText, detailsText, buttonBox);
        return card;
    }

    private void handleRemoveFavorite(Listing listing, VBox card) {
        favoriteDAO.removeFavorite(client.getID(), listing.getId());
        favTilePane.getChildren().remove(card);
        int remaining = favTilePane.getChildren().size();
        if (remaining == 0) {
            statusLabel.setText("You haven't saved any favourites yet. Browse listings to add some!");
        } else {
            statusLabel.setText(remaining + " saved listing(s)");
        }
    }

    private void openDetails(Listing listing) {
        try {
            PropertyOwner owner = ownerDAO.getPropertyOwnerById(listing.getOwnerId());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ListingDetailView.fxml"));
            Parent root = loader.load();
            ListingDetailController ctrl = loader.getController();
            ctrl.setListing(listing, owner, client);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(listing.getStreet() + " - Details");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            statusLabel.setText("Error opening details: " + e.getMessage());
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
