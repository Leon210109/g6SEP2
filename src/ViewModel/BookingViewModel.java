package ViewModel;

import Model.Booking;
import Model.RentalModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class BookingViewModel implements PropertyChangeListener {

    private final RentalModel model;


    private final ObservableList<Booking> bookings = FXCollections.observableArrayList();
    private final BooleanProperty bookingAddedSuccess = new SimpleBooleanProperty(false);

    public BookingViewModel(RentalModel model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }

    private final StringProperty errorMessage = new SimpleStringProperty("");

    public ObservableList<Booking> getBookings()         { return bookings; }
    public BooleanProperty bookingAddedSuccessProperty() { return bookingAddedSuccess; }
    public void resetBookingAddedSuccess()               { bookingAddedSuccess.set(false); }
    public void resetBookingSuccess()                    { bookingAddedSuccess.set(false); }
    public StringProperty errorMessageProperty()         { return errorMessage; }

    public void loadBookings()                   { model.getAllBookings(); }
    public void loadBookingsByClient(int clientId) { model.getBookingsByClient(clientId); }
    public void addBooking(Booking b)            { model.addBooking(b); }
    public void removeBooking(int clientId, int listingId) { model.removeBooking(clientId, listingId); }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "GET_ALL_BOOKINGS", "BOOKINGS_BY_CLIENT" -> {
              ArrayList<Booking> list =
                  (ArrayList<Booking>) evt.getNewValue();
                Platform.runLater(() -> { bookings.clear(); bookings.addAll(list); });
            }
            case "BookingAdded" -> {
              Booking b =
                  (Booking) evt.getNewValue();
                Platform.runLater(() -> {
                    bookings.add(b);
                    bookingAddedSuccess.set(true);
                });
            }
            case "BookingRemoved" -> {
              int[] ids =
                  (int[]) evt.getNewValue();
                int clientId = ids[0]; int listingId = ids[1];
                Platform.runLater(() -> bookings.removeIf(b -> b.getClientId() == clientId && b.getListingId() == listingId));
            }
            case "BookingUpdated" -> {
              Booking updated =
                  (Booking) evt.getNewValue();
                Platform.runLater(() -> {
                    for (int i = 0; i < bookings.size(); i++) {
                        if (bookings.get(i).getListingId() == updated.getListingId()) { bookings.set(i, updated); break; }
                    }
                });
            }
            case "ERROR" -> Platform.runLater(() -> errorMessage.set(evt.getNewValue() != null ? evt.getNewValue().toString() : "Error"));
        }
    }
}
