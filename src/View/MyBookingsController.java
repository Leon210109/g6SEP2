package View;

import Model.Booking;
import Model.Client;
import Model.Listing;
import ViewModel.BookingViewModel;
import ViewModel.ListingViewModel;
import ViewModel.ViewModelFactory;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.time.format.DateTimeFormatter;

public class MyBookingsController {

    @FXML private ScrollPane bookingsScrollPane;
    @FXML private TilePane   bookingsTilePane;
    @FXML private Label      statusLabel;

    private BookingViewModel bookingViewModel;
    private ListingViewModel listingViewModel;
    private Client client;

    public void setClient(Client client) {
        this.client = client;
        loadBookings();
    }

    @FXML
    private void initialize() {
        bookingViewModel = ViewModelFactory.getInstance().getBookingViewModel();
        listingViewModel = ViewModelFactory.getInstance().getListingViewModel();

        // Rebuild tiles whenever the booking list changes
        bookingViewModel.getBookings().addListener(
            (javafx.collections.ListChangeListener<Booking>) change -> rebuildTiles());

        // Pre-load all listings so we can look up listing info for each booking
        listingViewModel.getListings().addListener(
            (javafx.collections.ListChangeListener<Listing>) change -> rebuildTiles());
    }

    private void loadBookings() {
        if (client == null) return;
        bookingViewModel.loadBookingsByClient(client.getID());
        listingViewModel.loadListings();
    }

    private void rebuildTiles() {
        bookingsTilePane.getChildren().clear();
        if (bookingViewModel.getBookings().isEmpty()) {
            statusLabel.setText("You have no bookings yet.");
            statusLabel.setStyle("-fx-text-fill: #8B7355; -fx-font-size: 14px;");
            return;
        }
        statusLabel.setText("");
        for (Booking b : bookingViewModel.getBookings()) {
            Listing listing = findListing(b.getListingId());
            if (listing != null)
                bookingsTilePane.getChildren().add(createBookingCard(b, listing));
        }
    }

    private Listing findListing(int listingId) {
        for (Listing l : listingViewModel.getListings())
            if (l.getId() == listingId) return l;
        return null;
    }

    private VBox createBookingCard(Booking booking, Listing listing) {
        VBox card = new VBox(10);
        card.setPrefWidth(350);
        card.setAlignment(Pos.TOP_LEFT);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 8px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0, 0, 2);");

        Text title = new Text(listing.getStreet());
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-fill: #143D29;");

        Label addr  = new Label(listing.getRegion() + ", " + listing.getCountry());
        addr.setStyle("-fx-text-fill: #666666; -fx-font-size: 12px;");

        String sd = String.format("%02d/%02d/%d", booking.getStartDate().getDay(), booking.getStartDate().getMonth(), booking.getStartDate().getYear());
        String ed = String.format("%02d/%02d/%d", booking.getEndDate().getDay(),   booking.getEndDate().getMonth(),   booking.getEndDate().getYear());
        Label dates = new Label("\uD83D\uDCC5 " + sd + " → " + ed);
        dates.setStyle("-fx-text-fill: #1A5F3F; -fx-font-size: 14px; -fx-font-weight: bold;");

        Label ci = new Label("Check-in: "  + formatTime(booking.getCheck_in_time()));
        Label co = new Label("Check-out: " + formatTime(booking.getCheck_out_time()));
        ci.setStyle("-fx-text-fill: #8B7355; -fx-font-size: 12px;");
        co.setStyle("-fx-text-fill: #8B7355; -fx-font-size: 12px;");

        Label people = new Label("\uD83D\uDC65 " + booking.getNumber_of_people() + " guest" + (booking.getNumber_of_people() > 1 ? "s" : ""));
        people.setStyle("-fx-text-fill: #666666; -fx-font-size: 12px;");

        Label price = new Label("\uD83D\uDCB0 " + listing.getPrice() + " DKK/month");
        price.setStyle("-fx-text-fill: #143D29; -fx-font-size: 14px; -fx-font-weight: bold;");

        Button cancel = new Button("Cancel Booking");
        cancel.setStyle("-fx-background-color: #B22222; -fx-text-fill: white; -fx-background-radius: 5; -fx-padding: 8 20; -fx-cursor: hand;");
        cancel.setOnAction(e -> handleCancelBooking(booking, card));

        card.getChildren().addAll(title, addr, dates, ci, co, people, price, cancel);
        return card;
    }

    private void handleCancelBooking(Booking booking, VBox card) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Cancel this booking? This cannot be undone.", ButtonType.OK, ButtonType.CANCEL);
        alert.setTitle("Cancel Booking");
        alert.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK)
                bookingViewModel.removeBooking(booking.getClientId(), booking.getListingId());
        });
    }

    private String formatTime(java.time.LocalTime t) {
        if (t == null) return "N/A";
        return t.format(DateTimeFormatter.ofPattern("HH:mm"));
    }
}
