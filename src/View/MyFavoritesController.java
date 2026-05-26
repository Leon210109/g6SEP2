package View;

import Model.Client;
import Model.Listing;
import Model.PropertyOwner;
import ViewModel.FavoriteViewModel;
import ViewModel.ListingViewModel;
import ViewModel.ViewModelFactory;
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

public class MyFavoritesController {

    @FXML private ScrollPane favScrollPane;
    @FXML private TilePane   favTilePane;
    @FXML private Label      statusLabel;

    private FavoriteViewModel favoriteViewModel;
    private ListingViewModel  listingViewModel;
    private Client client;

    public void setClient(Client client) {
        this.client = client;
        favoriteViewModel.loadFavoritesForClient(client.getID());
    }

    @FXML
    private void initialize() {
        favoriteViewModel = ViewModelFactory.getInstance().getFavoriteViewModel();
        listingViewModel  = ViewModelFactory.getInstance().getListingViewModel();
        favoriteViewModel.getFavorites().addListener(
            (javafx.collections.ListChangeListener<Listing>) c -> rebuildTiles());
        // When owner lookup resolves, open detail dialog
        listingViewModel.ownerResultProperty().addListener((obs, o, owner) -> {
            if (owner != null && pendingDetailListing != null) {
                openDetails(pendingDetailListing, owner);
                pendingDetailListing = null;
            }
        });
    }

    private Listing pendingDetailListing = null;

    private void rebuildTiles() {
        favTilePane.getChildren().clear();
        if (favoriteViewModel.getFavorites().isEmpty()) {
            statusLabel.setText("You haven't saved any favourites yet.");
            return;
        }
        statusLabel.setText(favoriteViewModel.getFavorites().size() + " saved listing(s)");
        for (Listing l : favoriteViewModel.getFavorites())
            favTilePane.getChildren().add(createFavCard(l));
    }

    private VBox createFavCard(Listing listing) {
        VBox card = new VBox(12);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPadding(new Insets(15));
        card.setPrefWidth(280);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: #D4C4B0; -fx-border-radius: 10; -fx-border-width: 2;");

        ImageView iv = new ImageView();
        iv.setFitWidth(250); iv.setFitHeight(150); iv.setPreserveRatio(true);
        loadImage(iv, listing.getStreet());

        Text title = new Text(listing.getStreet());
        title.setStyle("-fx-fill: #143D29; -fx-font-size: 18px; -fx-font-weight: bold;");
        Text loc = new Text(listing.getRegion() + ", " + listing.getCountry());
        loc.setStyle("-fx-fill: #8B7355; -fx-font-size: 13px;");
        Text details = new Text(listing.getNumberOfRooms() + " rooms · " + listing.getSurfaceArea() + "m² · " + listing.getPrice() + " DKK/month");
        details.setStyle("-fx-fill: #1A5F3F; -fx-font-size: 12px; -fx-font-weight: bold;");

        Button removeBtn = new Button("\u2665 Remove");
        removeBtn.setStyle("-fx-background-color: #B22222; -fx-text-fill: white; -fx-background-radius: 5; -fx-padding: 6 14; -fx-cursor: hand;");
        removeBtn.setOnAction(e -> favoriteViewModel.removeFavorite(client.getID(), listing.getId()));

        Button viewBtn = new Button("View Details");
        viewBtn.setStyle("-fx-background-color: #1A5F3F; -fx-text-fill: white; -fx-background-radius: 5; -fx-padding: 6 14; -fx-cursor: hand;");
        viewBtn.setOnAction(e -> { pendingDetailListing = listing; listingViewModel.loadOwnerById(listing.getOwnerId()); });

        HBox btns = new HBox(10, removeBtn, viewBtn);
        btns.setAlignment(Pos.CENTER);
        card.getChildren().addAll(iv, title, loc, details, btns);
        return card;
    }

    private void openDetails(Listing listing, PropertyOwner owner) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/ListingDetailView.fxml"));
            Parent root = loader.load();
            ListingDetailController ctrl = loader.getController();
            ctrl.setListing(listing, owner, client);
            Stage s = new Stage();
            s.initModality(Modality.APPLICATION_MODAL);
            s.setTitle(listing.getStreet());
            s.setScene(new Scene(root));
            s.setResizable(false);
            s.show();
        } catch (IOException e) { statusLabel.setText("Error: " + e.getMessage()); }
    }

    private void loadImage(ImageView iv, String streetName) {
        try {
            File folder = new File("room_rental_img/" + streetName);
            if (folder.exists()) {
                ImageConverter.convertWebPFilesInFolder(folder.getAbsolutePath());
                File[] files = folder.listFiles((d, n) -> n.toLowerCase().matches(".*\\.(jpg|jpeg|png)"));
                if (files != null && files.length > 0) { iv.setImage(new Image(files[0].toURI().toString())); return; }
            }
        } catch (Exception ignored) {}
        iv.setStyle("-fx-background-color: #E0E0E0;");
    }
}
