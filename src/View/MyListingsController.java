package View;

import Model.Listing;
import Model.PropertyOwner;
import Persistence.ListingDAO;
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
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MyListingsController {

    @FXML
    private ScrollPane listingsScrollPane;
    
    @FXML
    private TilePane listingsTilePane;
    
    @FXML
    private Label statusLabel;
    
    @FXML
    private Button newListingBtn;

    private final ListingDAO listingDAO = new ListingDAO();
    private PropertyOwner propertyOwner;

    public void setPropertyOwner(PropertyOwner owner) {
        this.propertyOwner = owner;
        loadListings();
    }

    private void loadListings() {
        if (propertyOwner == null) {
            statusLabel.setText("Error: Property owner not set");
            return;
        }

        try {
            ArrayList<Listing> listings = listingDAO.getListingsByOwnerId(propertyOwner.getID());
            
            if (listings.isEmpty()) {
                statusLabel.setText("You don't have any listings yet. Create your first listing!");
                return;
            }

            listingsTilePane.getChildren().clear();
            
            for (Listing listing : listings) {
                VBox listingCard = createListingCard(listing);
                listingsTilePane.getChildren().add(listingCard);
            }
            
            statusLabel.setText(listings.size() + " listing(s) found");
            
        } catch (Exception e) {
            statusLabel.setText("Error loading listings: " + e.getMessage());
            e.printStackTrace();
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
        card.setPrefHeight(360);

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

        // Action buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(javafx.geometry.Pos.CENTER);
        
        Button editButton = new Button("Edit");
        editButton.setStyle(
            "-fx-background-color: #8B7355;" +
            "-fx-text-fill: white;" +
            "-fx-font-family: 'Cambria';" +
            "-fx-font-size: 12px;" +
            "-fx-background-radius: 5;" +
            "-fx-padding: 6 14;" +
            "-fx-cursor: hand;"
        );
        editButton.setOnAction(e -> openEditListing(listing));
        
        Button viewButton = new Button("View Details");
        viewButton.setStyle(
            "-fx-background-color: #1A5F3F;" +
            "-fx-text-fill: white;" +
            "-fx-font-family: 'Cambria';" +
            "-fx-font-size: 12px;" +
            "-fx-background-radius: 5;" +
            "-fx-padding: 6 14;" +
            "-fx-cursor: hand;"
        );
        viewButton.setOnAction(e -> openListingDetails(listing));

        Button deleteButton = new Button("Delete");
        deleteButton.setStyle(
            "-fx-background-color: #B22222;" +
            "-fx-text-fill: white;" +
            "-fx-font-family: 'Cambria';" +
            "-fx-font-size: 12px;" +
            "-fx-background-radius: 5;" +
            "-fx-padding: 6 14;" +
            "-fx-cursor: hand;"
        );
        deleteButton.setOnAction(e -> handleDeleteListing(listing, card));
        
        buttonBox.getChildren().addAll(editButton, viewButton, deleteButton);

        card.getChildren().addAll(imageView, streetText, locationText, detailsText, buttonBox);
        
        // Click anywhere on card to open details (but not on buttons)
        card.setOnMouseClicked(e -> {
            if (e.getTarget() != viewButton && e.getTarget() != editButton && e.getTarget() != deleteButton) {
                openListingDetails(listing);
            }
        });

        return card;
    }

    private void handleDeleteListing(Listing listing, VBox card) {
        javafx.scene.control.Alert confirm = new javafx.scene.control.Alert(
            javafx.scene.control.Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Listing");
        confirm.setHeaderText("Delete \"" + listing.getStreet() + "\"?");
        confirm.setContentText("This will permanently delete the listing and cannot be undone.");
        confirm.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                try {
                    listingDAO.deleteListing(listing.getId());
                    listingsTilePane.getChildren().remove(card);
                    int remaining = listingsTilePane.getChildren().size();
                    statusLabel.setText(remaining == 0
                        ? "You don't have any listings yet. Create your first listing!"
                        : remaining + " listing(s) found");
                } catch (Exception e) {
                    statusLabel.setText("Error deleting listing: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
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
            
            // Fallback: placeholder
            imageView.setStyle("-fx-background-color: #E0E0E0;");
            
        } catch (Exception e) {
            System.err.println("Error loading image for " + streetName + ": " + e.getMessage());
            imageView.setStyle("-fx-background-color: #E0E0E0;");
        }
    }

    @FXML
    public void handleNewListing() {
        System.out.println("DEBUG: New Listing button clicked!");
        System.out.println("DEBUG: PropertyOwner = " + propertyOwner);
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CreateListingView.fxml"));
            System.out.println("DEBUG: About to load FXML...");
            Parent root = loader.load();
            System.out.println("DEBUG: FXML loaded successfully!");
            
            CreateListingController controller = loader.getController();
            controller.setPropertyOwner(propertyOwner);
            controller.setOnSuccess(() -> loadListings()); // Reload listings after creation
            
            Stage createStage = new Stage();
            createStage.initModality(Modality.APPLICATION_MODAL);
            createStage.setTitle("Create New Listing");
            createStage.setScene(new Scene(root));
            createStage.show();
            
        } catch (IOException e) {
            System.err.println("ERROR: Failed to load CreateListingView.fxml");
            statusLabel.setText("Error opening create listing form: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("ERROR: Unexpected exception");
            e.printStackTrace();
        }
    }

    private void openListingDetails(Listing listing) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ListingDetailView.fxml"));
            Parent root = loader.load();
            
            ListingDetailController controller = loader.getController();
            controller.setListing(listing, propertyOwner, null); // PropertyOwner viewing their own listing
            
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

    private void openEditListing(Listing listing) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EditListingView.fxml"));
            Parent root = loader.load();
            
            EditListingController controller = loader.getController();
            controller.setListing(listing, propertyOwner);
            controller.setOnSuccess(() -> loadListings()); // Reload listings after update
            
            Stage editStage = new Stage();
            editStage.initModality(Modality.APPLICATION_MODAL);
            editStage.setTitle("Edit Listing - " + listing.getStreet());
            editStage.setScene(new Scene(root));
            editStage.show();
            
        } catch (IOException e) {
            statusLabel.setText("Error opening edit form: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
