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
import javafx.scene.control.*;
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
import java.util.List;
import java.util.stream.Collectors;

public class BrowseListingsController {

    @FXML private ScrollPane listingsScrollPane;
    @FXML private TilePane   listingsTilePane;
    @FXML private Label      statusLabel;
    @FXML private TextField  searchField;
    @FXML private ComboBox<String> maxPriceFilter;
    @FXML private ComboBox<String> minRoomsFilter;

    private ListingViewModel  listingViewModel;
    private FavoriteViewModel favoriteViewModel;
    private Client client;

    public void setClient(Client client) {
        this.client = client;
        listingViewModel.loadAvailableListings();
        if (client != null) favoriteViewModel.loadFavoritesForClient(client.getID());
    }

    @FXML
    private void initialize() {
        listingViewModel  = ViewModelFactory.getInstance().getListingViewModel();
        favoriteViewModel = ViewModelFactory.getInstance().getFavoriteViewModel();

        maxPriceFilter.getItems().addAll("3000", "5000", "8000", "12000", "20000");
        minRoomsFilter.getItems().addAll("1", "2", "3", "4", "5");
        searchField.textProperty().addListener((o, v, n) -> applyFilters());
        maxPriceFilter.valueProperty().addListener((o, v, n) -> applyFilters());
        minRoomsFilter.valueProperty().addListener((o, v, n) -> applyFilters());

        listingViewModel.getListings().addListener((javafx.collections.ListChangeListener<Listing>) c -> applyFilters());
        favoriteViewModel.getFavorites().addListener((javafx.collections.ListChangeListener<Listing>) c -> applyFilters());

        // When owner lookup resolves, open details dialog
        listingViewModel.ownerResultProperty().addListener((obs, o, owner) -> {
            if (owner != null && pendingDetailListing != null) {
                openListingDetails(pendingDetailListing, owner);
                pendingDetailListing = null;
            }
        });
    }

    private Listing pendingDetailListing = null;

    @FXML
    private void handleClearFilters() {
        searchField.clear();
        maxPriceFilter.setValue(null);
        minRoomsFilter.setValue(null);
    }

    private void applyFilters() {
        String search    = searchField != null && searchField.getText() != null ? searchField.getText().trim().toLowerCase() : "";
        String maxP      = maxPriceFilter != null ? maxPriceFilter.getValue() : null;
        String minR      = minRoomsFilter != null ? minRoomsFilter.getValue() : null;

        List<Listing> filtered = listingViewModel.getListings().stream()
            .filter(l -> search.isEmpty() || l.getStreet().toLowerCase().contains(search) || l.getRegion().toLowerCase().contains(search))
            .filter(l -> maxP == null || l.getPrice() <= Integer.parseInt(maxP))
            .filter(l -> minR == null || l.getNumberOfRooms() >= Integer.parseInt(minR))
            .collect(Collectors.toList());

        listingsTilePane.getChildren().clear();
        for (Listing l : filtered)
            listingsTilePane.getChildren().add(createListingCard(l));
        statusLabel.setText(filtered.isEmpty() ? "No listings match your filters." : filtered.size() + " listing(s) found");
    }

    private boolean isFavorite(Listing listing) {
        return favoriteViewModel.getFavorites().stream().anyMatch(f -> f.getId() == listing.getId());
    }

    private VBox createListingCard(Listing listing) {
        VBox card = new VBox(12);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: #D4C4B0; -fx-border-radius: 10; -fx-border-width: 2; -fx-cursor: hand;");
        card.setPrefWidth(280); card.setPrefHeight(420);

        ImageView iv = new ImageView();
        iv.setFitWidth(250); iv.setFitHeight(180); iv.setPreserveRatio(true);
        loadListingImage(iv, listing.getStreet());

        Text title = new Text(listing.getStreet());
        title.setStyle("-fx-fill: #143D29; -fx-font-size: 18px; -fx-font-weight: bold;");
        Text loc = new Text(listing.getRegion() + ", " + listing.getCountry());
        loc.setStyle("-fx-fill: #8B7355; -fx-font-size: 13px;");
        boolean lt = "LONG_TERM".equals(listing.getListingType());
        Label typeBadge = new Label(lt ? "Long-term" : "Short-term");
        typeBadge.setStyle("-fx-background-color: " + (lt ? "#6B4A2A" : "#1A5F3F") + "; -fx-text-fill: white; -fx-font-size: 11px; -fx-font-weight: bold; -fx-background-radius: 4; -fx-padding: 3 8;");
        Text details = new Text(listing.getNumberOfRooms() + " rooms · " + listing.getSurfaceArea() + "m² · " + listing.getPrice() + " DKK/month");
        details.setStyle("-fx-fill: #1A5F3F; -fx-font-size: 12px; -fx-font-weight: bold;");

        HBox btns = new HBox(8);
        btns.setAlignment(Pos.CENTER);

        if (client != null) {
            boolean[] fav = { isFavorite(listing) };
            Button heartBtn = new Button(fav[0] ? "\u2665 Saved" : "\u2661 Save");
            String savedStyle  = "-fx-background-color: #B22222; -fx-text-fill: white; -fx-background-radius: 5; -fx-padding: 6 14; -fx-cursor: hand;";
            String unsavedStyle = "-fx-background-color: #F5F0E8; -fx-text-fill: #B22222; -fx-background-radius: 5; -fx-border-color: #B22222; -fx-border-radius: 5; -fx-padding: 6 14; -fx-cursor: hand;";
            heartBtn.setStyle(fav[0] ? savedStyle : unsavedStyle);
            heartBtn.setOnAction(e -> {
                e.consume();
                if (fav[0]) {
                    favoriteViewModel.removeFavorite(client.getID(), listing.getId());
                    fav[0] = false;
                    heartBtn.setText("\u2661 Save");
                    heartBtn.setStyle(unsavedStyle);
                } else {
                    favoriteViewModel.addFavorite(client.getID(), listing.getId());
                    fav[0] = true;
                    heartBtn.setText("\u2665 Saved");
                    heartBtn.setStyle(savedStyle);
                }
            });
            btns.getChildren().add(heartBtn);
        }

        Button viewBtn = new Button("View Details");
        viewBtn.setStyle("-fx-background-color: #1A5F3F; -fx-text-fill: white; -fx-background-radius: 5; -fx-padding: 6 20; -fx-cursor: hand;");
        viewBtn.setOnAction(e -> { e.consume(); requestOwnerAndOpenDetails(listing); });
        btns.getChildren().add(viewBtn);

        card.getChildren().addAll(iv, title, loc, typeBadge, details, btns);
        card.setOnMouseClicked(e -> { if (!(e.getTarget() instanceof Button)) requestOwnerAndOpenDetails(listing); });
        return card;
    }

    private void requestOwnerAndOpenDetails(Listing listing) {
        pendingDetailListing = listing;
        listingViewModel.loadOwnerById(listing.getOwnerId());
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

    private void openListingDetails(Listing listing, PropertyOwner owner) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ListingDetailView.fxml"));
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
}
