package View;

import Model.Listing;
import Model.PropertyOwner;
import Util.ImageConverter;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;

public class ListingDetailController {

    @FXML private Text titleText;
    @FXML private FlowPane imageGallery;
    @FXML private Label addressLabel;
    @FXML private Label roomNumberLabel;
    @FXML private Label roomsLabel;
    @FXML private Label bathroomsLabel;
    @FXML private Label surfaceAreaLabel;
    @FXML private Label maxPeopleLabel;
    @FXML private Label balconyLabel;
    @FXML private Label renovatedLabel;
    @FXML private Label priceLabel;
    @FXML private Label ownerNameLabel;
    @FXML private Label ownerEmailLabel;
    @FXML private Label ownerPhoneLabel;
    @FXML private Button bookButton;

    private Listing listing;
    private PropertyOwner owner;

    public void setListing(Listing listing, PropertyOwner owner) {
        this.listing = listing;
        this.owner = owner;
        populateDetails();
    }

    private void populateDetails() {
        if (listing == null) return;

        // Update title
        titleText.setText(listing.getStreet());

        // Property details
        addressLabel.setText(listing.getStreet() + ", " + listing.getRegion() + ", " + listing.getCountry());
        roomNumberLabel.setText(listing.getRoomNumber());
        roomsLabel.setText(String.valueOf(listing.getNumberOfRooms()));
        bathroomsLabel.setText(String.valueOf(listing.getNumberOfBathrooms()));
        surfaceAreaLabel.setText(listing.getSurfaceArea() + " m²");
        maxPeopleLabel.setText(String.valueOf(listing.getMaxNumberOfPeople()) + " people");
        balconyLabel.setText(listing.isBalcony() ? "Yes ✓" : "No");
        renovatedLabel.setText(formatDate(listing.getLastRenovated()));
        priceLabel.setText(listing.getPrice() + " DKK / month");

        // Owner details
        if (owner != null) {
            ownerNameLabel.setText(owner.getFirstName() + " " + owner.getLastName());
            ownerEmailLabel.setText(owner.getEmail());
            ownerPhoneLabel.setText(formatPhoneNumber(owner.getPhoneNumber()));
        } else {
            ownerNameLabel.setText("N/A");
            ownerEmailLabel.setText("N/A");
            ownerPhoneLabel.setText("N/A");
        }

        // Load images
        loadImages();
    }

    private void loadImages() {
        imageGallery.getChildren().clear();
        
        try {
            File imageFolder = new File("room_rental_img/" + listing.getStreet());
            
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
                    for (File imageFile : imageFiles) {
                        ImageView imageView = new ImageView();
                        imageView.setFitWidth(200);
                        imageView.setFitHeight(150);
                        imageView.setPreserveRatio(true);
                        imageView.setStyle(
                            "-fx-cursor: hand;" +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);"
                        );
                        
                        try {
                            Image image = new Image(imageFile.toURI().toString());
                            imageView.setImage(image);
                            
                            // Add click-to-enlarge functionality
                            imageView.setOnMouseClicked(e -> enlargeImage(image));
                            
                            // Add hover effect
                            imageView.setOnMouseEntered(e -> imageView.setStyle(
                                "-fx-cursor: hand;" +
                                "-fx-effect: dropshadow(gaussian, rgba(26,95,63,0.6), 10, 0, 0, 3);" +
                                "-fx-scale-x: 1.05; -fx-scale-y: 1.05;"
                            ));
                            imageView.setOnMouseExited(e -> imageView.setStyle(
                                "-fx-cursor: hand;" +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);"
                            ));
                            
                            // Wrap in VBox for styling
                            VBox imageContainer = new VBox(imageView);
                            imageContainer.setAlignment(Pos.CENTER);
                            imageContainer.setStyle("-fx-background-color: white; -fx-padding: 5; -fx-background-radius: 8;");
                            
                            imageGallery.getChildren().add(imageContainer);
                        } catch (Exception e) {
                            System.err.println("Error loading image: " + imageFile.getName() + " - " + e.getMessage());
                        }
                    }
                    return;
                }
            }
            
            // No images found
            Label noImagesLabel = new Label("No images available");
            noImagesLabel.setStyle("-fx-text-fill: #8B7355; -fx-font-family: 'Cambria'; -fx-font-size: 14px;");
            imageGallery.getChildren().add(noImagesLabel);
            
        } catch (Exception e) {
            System.err.println("Error loading images: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void enlargeImage(Image image) {
        try {
            // Create a new stage for enlarged image
            Stage imageStage = new Stage();
            imageStage.initModality(Modality.APPLICATION_MODAL);
            imageStage.setTitle("Image Viewer");
            
            ImageView enlargedView = new ImageView(image);
            enlargedView.setPreserveRatio(true);
            
            // Set max size based on screen, but allow full image if smaller
            enlargedView.setFitWidth(Math.min(image.getWidth(), 1200));
            enlargedView.setFitHeight(Math.min(image.getHeight(), 800));
            
            // Wrap in ScrollPane for very large images
            ScrollPane scrollPane = new ScrollPane(enlargedView);
            scrollPane.setStyle("-fx-background: #000000;");
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
            
            VBox container = new VBox(scrollPane);
            container.setAlignment(Pos.CENTER);
            container.setStyle("-fx-background-color: #000000;");
            
            // Close on click
            container.setOnMouseClicked(e -> imageStage.close());
            
            Scene scene = new Scene(container);
            imageStage.setScene(scene);
            imageStage.show();
            
        } catch (Exception e) {
            System.err.println("Error enlarging image: " + e.getMessage());
        }
    }

    private String formatDate(Model.Date date) {
        if (date == null) return "N/A";
        return String.format("%02d/%02d/%d", date.getDay(), date.getMonth(), date.getYear());
    }

    private String formatPhoneNumber(String phone) {
        if (phone == null || phone.isEmpty()) return "N/A";
        
        // Format as: +45 12 34 56 78
        if (phone.length() == 10) {
            return String.format("+45 %s %s %s %s", 
                phone.substring(0, 2),
                phone.substring(2, 4),
                phone.substring(4, 6),
                phone.substring(6, 10)
            );
        }
        return phone;
    }

    @FXML
    private void handleBook() {
        // TODO: Implement booking functionality
        System.out.println("Booking property: " + listing.getStreet());
        // For now, just show a message or close
        bookButton.setText("Booking feature coming soon!");
        bookButton.setDisable(true);
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) titleText.getScene().getWindow();
        stage.close();
    }
}
