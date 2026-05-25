package Model;

import Network.SocketClient;

import Shared.Request;
import Shared.RequestType;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ClientModelManager
    implements RentalModel,
    PropertyChangeListener
{
  private SocketClient socketClient;
  private Gson gson;

  private PropertyChangeSupport support;

  // ───────────────── LOCAL STATE ─────────────────

  private ArrayList<Listing> listings;
  private ArrayList<Booking> bookings;
  private ArrayList<OwnerApplication>
      ownerApplications;

  private User currentUser;

  public ClientModelManager(
      SocketClient socketClient)
  {
    this.socketClient = socketClient;
    gson= new Gson();

    support =
        new PropertyChangeSupport(this);

    listings =
        new ArrayList<>();

    bookings =
        new ArrayList<>();

    ownerApplications =
        new ArrayList<>();

    socketClient
        .addPropertyChangeListener(this);

    socketClient.connect();
  }

  // ───────────────── CLIENT METHODS ─────────────────

  @Override
  public void registerClient(
      Client client)
  {
    Request request =
        new Request(
            RequestType.Register_Client,
            client);

    socketClient.sendRequest(request);
  }

  @Override
  public void login(
      String username,
      String password)
  {
    Request request =
        new Request(
            RequestType.Login,
            username,
            password);

    socketClient.sendRequest(request);
  }

  // ───────────────── LISTING METHODS ─────────────────

  @Override
  public void addListing(
      Listing listing)
  {
    Request request =
        new Request(
            RequestType.Add_Listing,
            listing);

    socketClient.sendRequest(request);
  }

  @Override
  public void removeListing(
      int listingId)
  {
    Request request =
        new Request(
            RequestType.Remove_Listing,
            listingId);

    socketClient.sendRequest(request);
  }

  @Override
  public void updateListing(
      Listing listing)
  {
    Request request =
        new Request(
            RequestType.Update_Listing,
            listing);

    socketClient.sendRequest(request);
  }

  @Override
  public void getAllListings()
  {
    Request request =
        new Request(
            RequestType.Get_All_Listings);

    socketClient.sendRequest(request);
  }

  // ───────────────── BOOKING METHODS ─────────────────

  @Override
  public void addBooking(
      Booking booking)
  {
    Request request =
        new Request(
            RequestType.Make_Booking,
            booking);

    socketClient.sendRequest(request);
  }

  @Override
  public void removeBooking(
      int clientId,
      int listingId)
  {
    Request request =
        new Request(
            RequestType.Remove_Booking,
            clientId);

    socketClient.sendRequest(request);
  }

  @Override
  public void updateBooking(
      Booking booking)
  {
    Request request =
        new Request(
            RequestType.Update_Booking,
            booking);

    socketClient.sendRequest(request);
  }

  @Override
  public void getAllBookings()
  {
    Request request =
        new Request(
            RequestType.Get_All_Booking);

    socketClient.sendRequest(request);
  }

  // ───────────────── OWNER APPLICATION METHODS ─────────────────

  @Override
  public void addOwnerApplication(
      OwnerApplication ownerApplication)
  {
    Request request =
        new Request(
            RequestType.Add_Owner_Application,
            ownerApplication);

    socketClient.sendRequest(request);
  }

  @Override
  public void removeOwnerApplication(
      int applicationId)
  {
    Request request =
        new Request(
            RequestType.Remove_Owner_Application,
            applicationId);

    socketClient.sendRequest(request);
  }

  @Override
  public void updateOwnerApplication(
      OwnerApplication ownerApplication)
  {
    Request request =
        new Request(
            RequestType.Update_Owner_Application,
            ownerApplication);

    socketClient.sendRequest(request);
  }

  @Override
  public void getAllOwnerApplications()
  {
    Request request =
        new Request(
            RequestType.Get_All_Owner_Applications);

    socketClient.sendRequest(request);
  }

  // ───────────────── RECEIVE EVENTS ─────────────────
  private <T> T convertObject(Object object, Class<T> clazz)
  {
    return gson.fromJson(gson.toJson(object),clazz);

  }
  private <T> ArrayList<T> convertList(
      Object object,
      Type type)
  {
    return gson.fromJson(
        gson.toJson(object),
        type);
  }

  @Override
  public void propertyChange(
      PropertyChangeEvent evt)
  {
    switch(evt.getPropertyName())
    {
      // ───────── LOGIN ─────────

      case "LOGIN_SUCCESS":
      {
        currentUser =
            convertObject(
                evt.getNewValue(),
                User.class);

        support.firePropertyChange(
            "LOGIN_SUCCESS",
            null,
            currentUser);

        break;
      }

      // ───────── ERROR ─────────

      case "ERROR":
      {
        support.firePropertyChange(
            "ERROR",
            null,
            evt.getNewValue());

        break;
      }

      // ───────── LISTINGS ─────────

      case "GET_ALL_LISTINGS":
      {
        Type listingListType =
            new TypeToken<ArrayList<Listing>>(){}.getType();

        listings =
            convertList(
                evt.getNewValue(),
                listingListType);

        support.firePropertyChange(
            "GET_ALL_LISTINGS",
            null,
            listings);

        break;
      }

      case "ListingAdded":
      {
        Listing listing =
            convertObject(
                evt.getNewValue(),
                Listing.class);

        listings.add(listing);

        support.firePropertyChange(
            "ListingAdded",
            null,
            listing);

        break;
      }

      case "ListingRemoved":
      {
        int listingId =
            (int) evt.getNewValue();

        listings.removeIf(
            l -> l.getId()
                == listingId);

        support.firePropertyChange(
            "ListingRemoved",
            null,
            listingId);

        break;
      }

      case "ListingUpdated":
      {
        Listing updatedListing =
            convertObject(
                evt.getNewValue(),
                Listing.class);

        for(int i = 0;
            i < listings.size();
            i++)
        {
          if(listings.get(i).getId()
              ==
              updatedListing.getId())
          {
            listings.set(
                i,
                updatedListing);

            break;
          }
        }

        support.firePropertyChange(
            "ListingUpdated",
            null,
            updatedListing);

        break;
      }

      // ───────── BOOKINGS ─────────

      case "BookingAdded":
      {
        Booking booking =
            convertObject(
                evt.getNewValue(),
                Booking.class);

        bookings.add(booking);

        support.firePropertyChange(
            "BookingAdded",
            null,
            booking);

        break;
      }

      case "BookingRemoved":
      {
        int bookingId =
            (int) evt.getNewValue();

        bookings.removeIf(
            b -> b.getId()
                == bookingId);

        support.firePropertyChange(
            "BookingRemoved",
            null,
            bookingId);

        break;
      }

      case "BookingUpdated":
      {
        Booking updatedBooking =
            convertObject(
                evt.getNewValue(),
                Booking.class);

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

        support.firePropertyChange(
            "BookingUpdated",
            null,
            updatedBooking);

        break;
      }

      case "GET_ALL_BOOKINGS":
      {
        Type bookingListType =
            new TypeToken<
                ArrayList<Booking>>(){}.getType();

        bookings =
            convertList(
                evt.getNewValue(),
                bookingListType);
        support.firePropertyChange(
            "GET_ALL_BOOKINGS",
            null,
            bookings);

        break;
      }

      // ───────── OWNER APPLICATIONS ─────────

      case "OwnerApplicationAdded":
      {
        OwnerApplication ownerApplication =
            convertObject(
                evt.getNewValue(),
                OwnerApplication.class);

        ownerApplications.add(
            ownerApplication);

        support.firePropertyChange(
            "OwnerApplicationAdded",
            null,
            ownerApplication);

        break;
      }

      case "OwnerApplicationRemoved":
      {
        int applicationId =
            (int) evt.getNewValue();

        ownerApplications.removeIf(
            o -> o.getApplicationId()
                == applicationId);

        support.firePropertyChange(
            "OwnerApplicationRemoved",
            null,
            applicationId);

        break;
      }

      case "OwnerApplicationUpdated":
      {
        OwnerApplication updatedApplication =
            convertObject(
                evt.getNewValue(),
                OwnerApplication.class);

        for(int i = 0;
            i < ownerApplications.size();
            i++)
        {
          if(ownerApplications.get(i)
              .getApplicationId()
              ==
              updatedApplication.getApplicationId())
          {
            ownerApplications.set(
                i,
                updatedApplication);

            break;
          }
        }

        support.firePropertyChange(
            "OwnerApplicationUpdated",
            null,
            updatedApplication);

        break;
      }

      case "GET_ALL_OWNER_APPLICATIONS":
      {
        Type ownerApplicationListType =
            new TypeToken<
                ArrayList<OwnerApplication>>(){}.getType();

        ownerApplications =
            convertList(
                evt.getNewValue(),
                ownerApplicationListType);

        support.firePropertyChange(
            "GET_ALL_OWNER_APPLICATIONS",
            null,
            ownerApplications);

        break;
      }
    }
  }

  // ───────────────── LISTENERS ─────────────────

  @Override
  public void addPropertyChangeListener(
      PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(
        listener);
  }

  @Override
  public void removePropertyChangeListener(
      PropertyChangeListener listener)
  {
    support.removePropertyChangeListener(
        listener);
  }
}