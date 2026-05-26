package View;

import Model.Admin;
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

public class AdminListingsController {

    @FXML private ScrollPane listingsScrollPane;
    @FXML private TilePane   listingsTilePane;
    @FXML private Label      statusLabel;

    private ListingViewModel listingViewModel;
    private Admin admin;

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    @FXML
    private void initialize() {
        listingViewModel = ViewModelFactory.getInstance().getListingViewModel();
        listingViewModel.getListings().addListener(
            (javafx.collections.ListChangeListener<Listing>) c -> rebuildTiles());
        listingViewModel.loadListings();
        // When owner lookup resolves, open edit dialog
        listingViewModel.ownerResultProperty().addListener((obs, o, owner) -> {
            if (owner != null && pendingEditListing != null) {
                openEditListing(pendingEditListing, owner);
                pendingEditListing = null;
            }
        });
    }

    private Listing pendingEditListing = null;

    private void rebuildTiles() {
        listingsTilePane.getChildren().clear();
        if (listingViewModel.getListings().isEmpty()) { statusLabel.setText("No listings in the system."); return; }
        statusLabel.setText(listingViewModel.getListings().size() + " listing(s) total");
        for (Listing l : listingViewModel.getListings())
            listingsTilePane.getChildren().add(createListingCard(l));
    }

    private VBox createListingCard(Listing listing) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPadding(new Insets(15));
        card.setPrefWidth(280);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: #D4C4B0; -fx-border-radius: 10; -fx-border-width: 2;");

        ImageView iv = new ImageView(); iv.setFitWidth(250); iv.setFitHeight(140); iv.setPreserveRatio(true);
        loadImage(iv, listing.getStreet());

        Text street = new Text(listing.getStreet()); street.setStyle("-fx-fill: #143D29; -fx-font-size: 17px; -fx-font-weight: bold;");
        Text loc = new Text(listing.getRegion() + ", " + listing.getCountry()); loc.setStyle("-fx-fill: #8B7355; -fx-font-size: 12px;");
        Text details = new Text(listing.getNumberOfRooms() + " rooms \u00b7 " + listing.getSurfaceArea() + "m\u00b2 \u00b7 " + listing.getPrice() + " DKK/month");
        details.setStyle("-fx-fill: #1A5F3F; -fx-font-size: 12px; -fx-font-weight: bold;");

        Button editBtn = new Button("Edit");
        editBtn.setStyle("-fx-background-color: #6B4A2A; -fx-text-fill: white; -fx-background-radius: 5; -fx-padding: 6 18; -fx-cursor: hand;");
        editBtn.setOnAction(e -> { pendingEditListing = listing; listingViewModel.loadOwnerById(listing.getOwnerId()); });

        Button deleteBtn = new Button("Delete");
        deleteBtn.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white; -fx-background-radius: 5; -fx-padding: 6 18; -fx-cursor: hand;");
        deleteBtn.setOnAction(e -> handleDelete(listing));

        HBox btns = new HBox(8, editBtn, deleteBtn); btns.setAlignment(Pos.CENTER);
        card.getChildren().addAll(iv, street, loc, details, btns);
        return card;
    }

    private void openEditListing(Listing listing, PropertyOwner owner) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EditListingView.fxml"));
            Parent root = loader.load();
            EditListingController ctrl = loader.getController();
            ctrl.setListing(listing, owner);
            ctrl.setOnSuccess(() -> listingViewModel.loadListings());
            Stage s = new Stage();
            s.initModality(Modality.APPLICATION_MODAL);
            s.setTitle("Edit Listing - " + listing.getStreet());
            s.setScene(new Scene(root));
            s.show();
        } catch (IOException e) { statusLabel.setText("Error opening edit form: " + e.getMessage()); }
    }

    private void handleDelete(Listing listing) {
        Alert c = new Alert(Alert.AlertType.CONFIRMATION, "Delete listing at " + listing.getStreet() + "? Cannot be undone.", ButtonType.OK, ButtonType.CANCEL);
        c.setTitle("Delete Listing");
        c.showAndWait().ifPresent(r -> { if (r == ButtonType.OK) listingViewModel.removeListing(listing.getId()); });
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
