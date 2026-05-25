package ViewModel;

import Model.Booking;
import Model.RentalModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class BookingViewModel implements PropertyChangeListener
{
  private RentalModel model;
  private ObservableList<Booking> bookings;

  private ObjectProperty<Booking> selectedBooking;

  private StringProperty errorMessage;

  public BookingViewModel(RentalModel model)
  {
    this.model = model;

    bookings = FXCollections.observableArrayList();

    selectedBooking = new SimpleObjectProperty<>();

    errorMessage = new SimpleStringProperty();

    model.addPropertyChangeListener(this);
  }
  public void loadBookings()
  {
    model.getAllBookings();
  }
  public void addBooking(
      Booking booking)
  {
    model.addBooking(booking);
  }
  public void removeBooking()
  {
    if(selectedBooking.get() == null)
    {
      errorMessage.set(
          "Select booking first");

      return;
    }

    model.removeBooking(
        selectedBooking.get().getClientId(),
        selectedBooking.get().getListingId());
  }
  @Override
  public void propertyChange(
      PropertyChangeEvent evt)
  {
    switch(evt.getPropertyName())
    {
      case "GET_ALL_BOOKINGS":
      {
        bookings.clear();

        bookings.addAll(
            (ArrayList<Booking>)
                evt.getNewValue());

        break;
      }

      case "BookingAdded":
      {
        Booking booking =
            (Booking)
                evt.getNewValue();

        bookings.add(booking);

        break;
      }

      case "BookingRemoved":
      {
        int bookingId =
            (int)
                evt.getNewValue();

        bookings.removeIf(
            b -> b.getId()
                == bookingId);

        break;
      }

      case "BookingUpdated":
      {
        Booking updatedBooking =
            (Booking)
                evt.getNewValue();

        for(int i = 0;
            i < bookings.size();
            i++)
        {
          if(bookings.get(i).getId()
              ==
              updatedBooking.getId())
          {
            bookings.set(
                i,
                updatedBooking);

            break;
          }
        }

        break;
      }

      case "ERROR":
      {
        errorMessage.set(
            (String)
                evt.getNewValue());

        break;
      }
    }
  }
  public ObservableList<Booking>
  getBookings()
  {
    return bookings;
  }

  public ObjectProperty<Booking>
  selectedBookingProperty()
  {
    return selectedBooking;
  }

  public StringProperty
  errorMessageProperty()
  {
    return errorMessage;
  }
}