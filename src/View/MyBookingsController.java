package View;

import Model.Booking;
import Model.Client;
import Model.Listing;
import Persistence.BookingDAO;
import Persistence.ListingDAO;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MyBookingsController {

    @FXML
    private ScrollPane bookingsScrollPane;
    
    @FXML
    private TilePane bookingsTilePane;
    
    @FXML
    private Label statusLabel;

    private final BookingDAO bookingDAO = new BookingDAO();
    private final ListingDAO listingDAO = new ListingDAO();
    private Client client;

    public void setClient(Client client) {
        this.client = client;
        loadBookings();
    }

    @FXML
    private void initialize() {
        // Will be called when client is set
    }

    private void loadBookings() {
        if (client == null) {
            statusLabel.setText("No client logged in");
            return;
        }

        try {
            ArrayList<Booking> bookings = bookingDAO.getBookingsByClientId(client.getID());
            
            if (bookings.isEmpty()) {
                statusLabel.setText("You have no bookings yet. Browse available listings to make a booking!");
                statusLabel.setStyle("-fx-text-fill: #8B7355; -fx-font-size: 14px;");
                return;
            }

            statusLabel.setText("");
            bookingsTilePane.getChildren().clear();

            for (Booking booking : bookings) {
                Listing listing = listingDAO.getListingById(booking.getListingId());
                if (listing != null) {
                    VBox bookingCard = createBookingCard(booking, listing);
                    bookingsTilePane.getChildren().add(bookingCard);
                }
            }

        } catch (Exception e) {
            statusLabel.setText("Error loading bookings: " + e.getMessage());
            statusLabel.setStyle("-fx-text-fill: red;");
            e.printStackTrace();
        }
    }

    private VBox createBookingCard(Booking booking, Listing listing) {
        VBox card = new VBox(10);
        card.setPrefWidth(350);
        card.setAlignment(Pos.TOP_LEFT);
        card.setPadding(new Insets(20));
        card.setStyle("""
            -fx-background-color: white;
            -fx-background-radius: 8px;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0, 0, 2);
            """);

        // Property title
        Text titleText = new Text(listing.getStreet());
        titleText.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-fill: #143D29;");

        // Property address
        Label addressLabel = new Label(listing.getRegion() + ", " + listing.getCountry());
        addressLabel.setStyle("-fx-text-fill: #666666; -fx-font-size: 12px;");

        // Booking dates
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        String startDate = String.format("%02d/%02d/%d", 
            booking.getStartDate().getDay(), 
            booking.getStartDate().getMonth(), 
            booking.getStartDate().getYear());
        String endDate = String.format("%02d/%02d/%d", 
            booking.getEndDate().getDay(), 
            booking.getEndDate().getMonth(), 
            booking.getEndDate().getYear());

        Label datesLabel = new Label("📅 " + startDate + " → " + endDate);
        datesLabel.setStyle("-fx-text-fill: #1A5F3F; -fx-font-size: 14px; -fx-font-weight: bold;");

        // Check-in/out times
        Label checkInLabel = new Label("Check-in: " + formatTime(booking.getCheck_in_time()));
        checkInLabel.setStyle("-fx-text-fill: #8B7355; -fx-font-size: 12px;");

        Label checkOutLabel = new Label("Check-out: " + formatTime(booking.getCheck_out_time()));
        checkOutLabel.setStyle("-fx-text-fill: #8B7355; -fx-font-size: 12px;");

        // Number of people
        Label peopleLabel = new Label("👥 " + booking.getNumber_of_people() + " guest" + 
            (booking.getNumber_of_people() > 1 ? "s" : ""));
        peopleLabel.setStyle("-fx-text-fill: #666666; -fx-font-size: 12px;");

        // Price
        Label priceLabel = new Label("💰 " + listing.getPrice() + " DKK/month");
        priceLabel.setStyle("-fx-text-fill: #143D29; -fx-font-size: 14px; -fx-font-weight: bold;");

        // Cancel button
        Button cancelButton = new Button("Cancel Booking");
        cancelButton.setStyle("""
            -fx-background-color: #B22222;
            -fx-text-fill: white;
            -fx-font-size: 12px;
            -fx-background-radius: 5;
            -fx-padding: 8 20;
            -fx-cursor: hand;
            """);
        cancelButton.setOnAction(e -> handleCancelBooking(booking, card));

        card.getChildren().addAll(
            titleText,
            addressLabel,
            datesLabel,
            checkInLabel,
            checkOutLabel,
            peopleLabel,
            priceLabel,
            cancelButton
        );

        return card;
    }

    private void handleCancelBooking(Booking booking, VBox card) {
        // Show confirmation dialog
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Cancel Booking");
        confirmAlert.setHeaderText("Are you sure you want to cancel this booking?");
        confirmAlert.setContentText("This action cannot be undone.");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    // Delete the booking from database using composite key
                    bookingDAO.deleteBooking(booking.getClientId(), booking.getListingId());
                    
                    // Remove the card from UI immediately
                    bookingsTilePane.getChildren().remove(card);
                    
                    // Show success message
                    statusLabel.setText("Booking cancelled successfully!");
                    statusLabel.setStyle("-fx-text-fill: #2E7D32; -fx-font-size: 14px; -fx-font-weight: bold;");
                    
                    // Check if there are no more bookings and update status
                    if (bookingsTilePane.getChildren().isEmpty()) {
                        statusLabel.setText("You have no bookings yet. Browse available listings to make a booking!");
                        statusLabel.setStyle("-fx-text-fill: #8B7355; -fx-font-size: 14px;");
                    }
                    
                } catch (Exception e) {
                    statusLabel.setText("Error cancelling booking: " + e.getMessage());
                    statusLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
                    e.printStackTrace();
                }
            }
        });
    }

    private String formatTime(java.time.LocalTime time) {
        if (time == null) return "N/A";
        return time.format(DateTimeFormatter.ofPattern("HH:mm"));
    }
}
