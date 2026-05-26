package View;

import Model.Listing;
import Model.PropertyOwner;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class MyListingsController {

    @FXML private ScrollPane listingsScrollPane;
    @FXML private TilePane   listingsTilePane;
    @FXML private Label      statusLabel;
    @FXML private Button     newListingBtn;

    private ListingViewModel listingViewModel;
    private PropertyOwner propertyOwner;

    public void setPropertyOwner(PropertyOwner owner) {
        this.propertyOwner = owner;
        listingViewModel.loadListingsByOwner(owner.getID());
    }

    @FXML
    private void initialize() {
        listingViewModel = ViewModelFactory.getInstance().getListingViewModel();
        listingViewModel.getListings().addListener(
            (javafx.collections.ListChangeListener<Listing>) change -> rebuildTiles());
    }

    private void rebuildTiles() {
        listingsTilePane.getChildren().clear();
        if (listingViewModel.getListings().isEmpty()) {
            statusLabel.setText("You don't have any listings yet.");
            return;
        }
        statusLabel.setText(listingViewModel.getListings().size() + " listing(s)");
        for (Listing l : listingViewModel.getListings())
            listingsTilePane.getChildren().add(createListingCard(l));
    }

    private VBox createListingCard(Listing listing) {
        VBox card = new VBox(12);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: #D4C4B0; -fx-border-radius: 10; -fx-border-width: 2; -fx-cursor: hand;");
        card.setPrefWidth(280);
        card.setPrefHeight(360);

        ImageView iv = new ImageView();
        iv.setFitWidth(250); iv.setFitHeight(180); iv.setPreserveRatio(true);
        loadListingImage(iv, listing.getStreet());

        Text title = new Text(listing.getStreet());
        title.setStyle("-fx-fill: #143D29; -fx-font-size: 18px; -fx-font-weight: bold;");
        Text loc = new Text(listing.getRegion() + ", " + listing.getCountry());
        loc.setStyle("-fx-fill: #8B7355; -fx-font-size: 13px;");
        Text details = new Text(listing.getNumberOfRooms() + " rooms · " + listing.getSurfaceArea() + "m² · " + listing.getPrice() + " DKK/month");
        details.setStyle("-fx-fill: #1A5F3F; -fx-font-size: 12px; -fx-font-weight: bold;");

        Button editBtn = new Button("Edit");
        editBtn.setStyle("-fx-background-color: #8B7355; -fx-text-fill: white; -fx-background-radius: 5; -fx-padding: 6 14; -fx-cursor: hand;");
        editBtn.setOnAction(e -> openEditListing(listing));

        Button viewBtn = new Button("View Details");
        viewBtn.setStyle("-fx-background-color: #1A5F3F; -fx-text-fill: white; -fx-background-radius: 5; -fx-padding: 6 14; -fx-cursor: hand;");
        viewBtn.setOnAction(e -> openListingDetails(listing));

        Button delBtn = new Button("Delete");
        delBtn.setStyle("-fx-background-color: #B22222; -fx-text-fill: white; -fx-background-radius: 5; -fx-padding: 6 14; -fx-cursor: hand;");
        delBtn.setOnAction(e -> handleDeleteListing(listing));

        HBox btns = new HBox(10, editBtn, viewBtn, delBtn);
        btns.setAlignment(Pos.CENTER);
        card.getChildren().addAll(iv, title, loc, details, btns);
        return card;
    }

    private void handleDeleteListing(Listing listing) {
        javafx.scene.control.Alert confirm = new javafx.scene.control.Alert(
            javafx.scene.control.Alert.AlertType.CONFIRMATION,
            "Delete \"" + listing.getStreet() + "\"? This cannot be undone.", ButtonType.OK, ButtonType.CANCEL);
        confirm.setTitle("Delete Listing");
        confirm.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) listingViewModel.removeListing(listing.getId());
        });
    }

    private void loadListingImage(ImageView iv, String streetName) {
        try {
            File folder = new File("room_rental_img/" + streetName);
            if (folder.exists()) {
                ImageConverter.convertWebPFilesInFolder(folder.getAbsolutePath());
                File[] files = folder.listFiles((d, n) -> n.toLowerCase().matches(".*\\.(jpg|jpeg|png|webp)"));
                if (files != null && files.length > 0) { iv.setImage(new Image(files[0].toURI().toString())); return; }
            }
        } catch (Exception ignored) {}
        iv.setStyle("-fx-background-color: #E0E0E0;");
    }

    @FXML
    public void handleNewListing() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/CreateListingView.fxml"));
            Parent root = loader.load();
            CreateListingController ctrl = loader.getController();
            ctrl.setPropertyOwner(propertyOwner);
            ctrl.setOnSuccess(() -> listingViewModel.loadListingsByOwner(propertyOwner.getID()));
            Stage s = new Stage();
            s.initModality(Modality.APPLICATION_MODAL);
            s.setTitle("Create New Listing");
            s.setScene(new Scene(root));
            s.show();
        } catch (IOException e) { statusLabel.setText("Error: " + e.getMessage()); }
    }

    private void openListingDetails(Listing listing) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/ListingDetailView.fxml"));
            Parent root = loader.load();
            ListingDetailController ctrl = loader.getController();
            ctrl.setListing(listing, propertyOwner, null);
            Stage s = new Stage();
            s.initModality(Modality.APPLICATION_MODAL);
            s.setTitle(listing.getStreet());
            s.setScene(new Scene(root));
            s.show();
        } catch (IOException e) { statusLabel.setText("Error: " + e.getMessage()); }
    }

    private void openEditListing(Listing listing) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/EditListingView.fxml"));
            Parent root = loader.load();
            EditListingController ctrl = loader.getController();
            ctrl.setListing(listing, propertyOwner);
            ctrl.setOnSuccess(() -> listingViewModel.loadListingsByOwner(propertyOwner.getID()));
            Stage s = new Stage();
            s.initModality(Modality.APPLICATION_MODAL);
            s.setTitle("Edit Listing");
            s.setScene(new Scene(root));
            s.show();
        } catch (IOException e) { statusLabel.setText("Error: " + e.getMessage()); }
    }
}
