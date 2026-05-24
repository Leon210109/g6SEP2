package View;

import Model.Client;
import Model.Listing;
import Model.PropertyOwner;
import Persistence.BookingDAO;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
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
import java.util.List;
import java.util.stream.Collectors;

public class BrowseListingsController {

    @FXML
    private ScrollPane listingsScrollPane;
    
    @FXML
    private TilePane listingsTilePane;
    
    @FXML
    private Label statusLabel;

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> maxPriceFilter;

    @FXML
    private ComboBox<String> minRoomsFilter;

    private final ListingDAO listingDAO = new ListingDAO();
    private final PropertyOwnerDAO ownerDAO = new PropertyOwnerDAO();
    private final BookingDAO bookingDAO = new BookingDAO();
    private final FavoriteDAO favoriteDAO = new FavoriteDAO();
    private Client client;
    private List<Listing> allAvailableListings = new ArrayList<>();

    public void setClient(Client client) {
        this.client = client;
        loadListings();
    }

    @FXML
    private void initialize() {
        // Populate filter options
        maxPriceFilter.getItems().addAll("3000", "5000", "8000", "12000", "20000");
        minRoomsFilter.getItems().addAll("1", "2", "3", "4", "5");

        // Live search as user types
        searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        maxPriceFilter.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        minRoomsFilter.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());

    }

    @FXML
    private void handleClearFilters() {
        searchField.clear();
        maxPriceFilter.setValue(null);
        minRoomsFilter.setValue(null);
    }

    private void loadListings() {
        try {
            ArrayList<Listing> allListings = listingDAO.getAllListings();
            
            if (allListings.isEmpty()) {
                statusLabel.setText("No listings available at the moment.");
                return;
            }

            allAvailableListings = allListings.stream()
                .filter(l -> bookingDAO.getBookingsByListingId(l.getId()).isEmpty())
                .collect(Collectors.toList());

            applyFilters();
            
        } catch (Exception e) {
            statusLabel.setText("Error loading listings: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void applyFilters() {
        String search = searchField != null && searchField.getText() != null
            ? searchField.getText().trim().toLowerCase() : "";
        String maxPriceStr = maxPriceFilter != null ? maxPriceFilter.getValue() : null;
        String minRoomsStr = minRoomsFilter != null ? minRoomsFilter.getValue() : null;

        List<Listing> filtered = allAvailableListings.stream()
            .filter(l -> search.isEmpty()
                || l.getStreet().toLowerCase().contains(search)
                || l.getRegion().toLowerCase().contains(search))
            .filter(l -> maxPriceStr == null
                || l.getPrice() <= Integer.parseInt(maxPriceStr))
            .filter(l -> minRoomsStr == null
                || l.getNumberOfRooms() >= Integer.parseInt(minRoomsStr))
            .collect(Collectors.toList());

        listingsTilePane.getChildren().clear();
        for (Listing listing : filtered) {
            listingsTilePane.getChildren().add(createListingCard(listing));
        }

        if (filtered.isEmpty()) {
            statusLabel.setText("No listings match your filters.");
        } else {
            statusLabel.setText(filtered.size() + " listing(s) found");
        }
    }

    private VBox createListingCard(Listing listing) {
        VBox card = new VBox(12);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPadding(new Insets(15));
        card.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: #D4C4B0;" +
            "-fx-border-radius: 10;" +
            "-fx-border-width: 2;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);" +
            "-fx-cursor: hand;"
        );
        card.setPrefWidth(280);
        card.setPrefHeight(420);

        // Add hover effect
        card.setOnMouseEntered(e -> card.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: #1A5F3F;" +
            "-fx-border-radius: 10;" +
            "-fx-border-width: 3;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0, 0, 4);" +
            "-fx-cursor: hand;"
        ));
        card.setOnMouseExited(e -> card.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: #D4C4B0;" +
            "-fx-border-radius: 10;" +
            "-fx-border-width: 2;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);" +
            "-fx-cursor: hand;"
        ));

        // Image
        ImageView imageView = new ImageView();
        imageView.setFitWidth(250);
        imageView.setFitHeight(180);
        imageView.setPreserveRatio(true);
        imageView.setStyle("-fx-background-radius: 8;");
        
        // Try to load first image from the listing's street folder
        loadListingImage(imageView, listing.getStreet());

        // Street name (title)
        Text streetText = new Text(listing.getStreet());
        streetText.setStyle(
            "-fx-fill: #143D29;" +
            "-fx-font-family: 'Palatino Linotype';" +
            "-fx-font-size: 18px;" +
            "-fx-font-weight: bold;"
        );

        // Location
        Text locationText = new Text(listing.getRegion() + ", " + listing.getCountry());
        locationText.setStyle(
            "-fx-fill: #8B7355;" +
            "-fx-font-family: 'Cambria';" +
            "-fx-font-size: 13px;"
        );

        // Listing type badge
        boolean isLongTerm = "LONG_TERM".equals(listing.getListingType());
        Label typeBadge = new Label(isLongTerm ? "Long-term Rental" : "Short-term");
        typeBadge.setStyle(
            "-fx-background-color: " + (isLongTerm ? "#6B4A2A" : "#1A5F3F") + ";" +
            "-fx-text-fill: white; -fx-font-family: 'Cambria'; -fx-font-size: 11px;" +
            "-fx-font-weight: bold; -fx-background-radius: 4; -fx-padding: 3 8;"
        );

        // Details (rooms, size, price)
        Text detailsText = new Text(
            listing.getNumberOfRooms() + " rooms · " +
            listing.getSurfaceArea() + "m² · " +
            listing.getPrice() + " DKK/month"
        );
        detailsText.setStyle(
            "-fx-fill: #1A5F3F;" +
            "-fx-font-family: 'Cambria';" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: bold;"
        );

        // Buttons row
        HBox buttonRow = new HBox(8);
        buttonRow.setAlignment(Pos.CENTER);

        // Heart / favourite toggle (only when logged in as client)
        if (client != null) {
            boolean[] fav = { favoriteDAO.isFavorite(client.getID(), listing.getId()) };
            Button heartBtn = new Button(fav[0] ? "\u2665 Saved" : "\u2661 Save");
            heartBtn.setStyle(fav[0]
                ? "-fx-background-color: #B22222; -fx-text-fill: white; -fx-font-family: 'Cambria'; -fx-font-size: 12px; -fx-background-radius: 5; -fx-padding: 6 14; -fx-cursor: hand;"
                : "-fx-background-color: #F5F0E8; -fx-text-fill: #B22222; -fx-font-family: 'Cambria'; -fx-font-size: 12px; -fx-background-radius: 5; -fx-border-color: #B22222; -fx-border-radius: 5; -fx-padding: 6 14; -fx-cursor: hand;"
            );
            heartBtn.setOnAction(e -> {
                e.consume();
                if (fav[0]) {
                    favoriteDAO.removeFavorite(client.getID(), listing.getId());
                    fav[0] = false;
                    heartBtn.setText("\u2661 Save");
                    heartBtn.setStyle("-fx-background-color: #F5F0E8; -fx-text-fill: #B22222; -fx-font-family: 'Cambria'; -fx-font-size: 12px; -fx-background-radius: 5; -fx-border-color: #B22222; -fx-border-radius: 5; -fx-padding: 6 14; -fx-cursor: hand;");
                } else {
                    favoriteDAO.addFavorite(client.getID(), listing.getId());
                    fav[0] = true;
                    heartBtn.setText("\u2665 Saved");
                    heartBtn.setStyle("-fx-background-color: #B22222; -fx-text-fill: white; -fx-font-family: 'Cambria'; -fx-font-size: 12px; -fx-background-radius: 5; -fx-padding: 6 14; -fx-cursor: hand;");
                }
            });
            buttonRow.getChildren().add(heartBtn);
        }

        // View Details button
        Button viewButton = new Button("View Details");
        viewButton.setStyle(
            "-fx-background-color: #1A5F3F;" +
            "-fx-text-fill: white;" +
            "-fx-font-family: 'Cambria';" +
            "-fx-font-size: 12px;" +
            "-fx-background-radius: 5;" +
            "-fx-padding: 6 20;" +
            "-fx-cursor: hand;"
        );
        viewButton.setOnAction(e -> { e.consume(); openListingDetails(listing); });
        buttonRow.getChildren().add(viewButton);

        card.getChildren().addAll(imageView, streetText, locationText, typeBadge, detailsText, buttonRow);

        // Click anywhere on card (but not a button) to open details
        card.setOnMouseClicked(e -> {
            if (!(e.getTarget() instanceof Button)) {
                openListingDetails(listing);
            }
        });

        return card;
    }

    private void loadListingImage(ImageView imageView, String streetName) {
        try {
            // Path to images: room_rental_img/{streetName}/
            File imageFolder = new File("room_rental_img/" + streetName);
            
            if (imageFolder.exists() && imageFolder.isDirectory()) {
                // Auto-convert any WebP files to JPG
                ImageConverter.convertWebPFilesInFolder(imageFolder.getAbsolutePath());
                
                File[] imageFiles = imageFolder.listFiles((dir, name) -> {
                    String lower = name.toLowerCase();
                    return lower.endsWith(".jpg") || 
                           lower.endsWith(".jpeg") ||
                           lower.endsWith(".png") ||
                           lower.endsWith(".webp");
                });
                
                if (imageFiles != null && imageFiles.length > 0) {
                    // Load first image
                    Image image = new Image(imageFiles[0].toURI().toString());
                    imageView.setImage(image);
                    return;
                }
            }
            
            // Fallback: placeholder image
            imageView.setStyle("-fx-background-color: #E0E0E0;");
            
        } catch (Exception e) {
            System.err.println("Error loading image for " + streetName + ": " + e.getMessage());
            imageView.setStyle("-fx-background-color: #E0E0E0;");
        }
    }

    private void openListingDetails(Listing listing) {
        try {
            // Load owner information
            PropertyOwner owner = ownerDAO.getPropertyOwnerById(listing.getOwnerId());
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ListingDetailView.fxml"));
            Parent root = loader.load();
            
            ListingDetailController controller = loader.getController();
            controller.setListing(listing, owner, client);
            
            Stage detailStage = new Stage();
            detailStage.initModality(Modality.APPLICATION_MODAL);
            detailStage.setTitle(listing.getStreet() + " - Details");
            detailStage.setScene(new Scene(root));
            detailStage.setResizable(false);
            detailStage.show();
            
        } catch (IOException e) {
            statusLabel.setText("Error opening details: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
