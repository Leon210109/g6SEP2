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

public class ClientModelManager implements RentalModel, PropertyChangeListener
{
  private final SocketClient socketClient;
  private final Gson gson;
  private final PropertyChangeSupport support;

  public ClientModelManager(SocketClient socketClient)
  {
    this.socketClient = socketClient;
    this.gson = new Gson();
    this.support = new PropertyChangeSupport(this);
    socketClient.addPropertyChangeListener(this);
    socketClient.connect();
  }

  // ── AUTH ──────────────────────────────────────────────────────────────────

  @Override
  public void registerClient(Client client)
  {
    socketClient.sendRequest(new Request(RequestType.Register_Client, client));
  }

  @Override
  public void login(String username, String password)
  {
    socketClient.sendRequest(new Request(RequestType.Login, username, password));
  }

  // ── LISTINGS ──────────────────────────────────────────────────────────────

  @Override
  public void addListing(Listing listing)
  {
    socketClient.sendRequest(new Request(RequestType.Add_Listing, listing));
  }

  @Override
  public void removeListing(int listingId)
  {
    socketClient.sendRequest(new Request(RequestType.Remove_Listing, listingId));
  }

  @Override
  public void updateListing(Listing listing)
  {
    socketClient.sendRequest(new Request(RequestType.Update_Listing, listing));
  }

  @Override
  public void getAllListings()
  {
    socketClient.sendRequest(new Request(RequestType.Get_All_Listings));
  }

  @Override
  public void getAvailableListings()
  {
    socketClient.sendRequest(new Request(RequestType.Get_Available_Listings));
  }

  @Override
  public void getListingsByOwner(int ownerId)
  {
    socketClient.sendRequest(new Request(RequestType.Get_Listings_By_Owner, ownerId));
  }

  // ── BOOKINGS ──────────────────────────────────────────────────────────────

  @Override
  public void addBooking(Booking booking)
  {
    socketClient.sendRequest(new Request(RequestType.Make_Booking, booking));
  }

  @Override
  public void removeBooking(int clientId, int listingId)
  {
    socketClient.sendRequest(new Request(RequestType.Remove_Booking, clientId, listingId));
  }

  @Override
  public void updateBooking(Booking booking)
  {
    socketClient.sendRequest(new Request(RequestType.Update_Booking, booking));
  }

  @Override
  public void getAllBookings()
  {
    socketClient.sendRequest(new Request(RequestType.Get_All_Booking));
  }

  @Override
  public void getBookingsByClient(int clientId)
  {
    socketClient.sendRequest(new Request(RequestType.Get_Bookings_By_Client, clientId));
  }

  // ── OWNER APPLICATIONS ────────────────────────────────────────────────────

  @Override
  public void addOwnerApplication(OwnerApplication ownerApplication)
  {
    socketClient.sendRequest(new Request(RequestType.Add_Owner_Application, ownerApplication));
  }

  @Override
  public void removeOwnerApplication(int applicationId)
  {
    socketClient.sendRequest(new Request(RequestType.Remove_Owner_Application, applicationId));
  }

  @Override
  public void updateOwnerApplication(OwnerApplication ownerApplication)
  {
    socketClient.sendRequest(new Request(RequestType.Update_Owner_Application, ownerApplication));
  }

  @Override
  public void getAllOwnerApplications()
  {
    socketClient.sendRequest(new Request(RequestType.Get_All_Owner_Applications));
  }

  @Override
  public void approveOwnerApplication(int applicationId, int adminId)
  {
    socketClient.sendRequest(new Request(RequestType.Approve_Owner_Application, applicationId, adminId));
  }

  @Override
  public void rejectOwnerApplication(int applicationId)
  {
    socketClient.sendRequest(new Request(RequestType.Reject_Owner_Application, applicationId));
  }

  // ── FAVORITES ─────────────────────────────────────────────────────────────

  @Override
  public void addFavorite(int clientId, int listingId)
  {
    socketClient.sendRequest(new Request(RequestType.Add_Favorite, clientId, listingId));
  }

  @Override
  public void removeFavorite(int clientId, int listingId)
  {
    socketClient.sendRequest(new Request(RequestType.Remove_Favorite, clientId, listingId));
  }

  @Override
  public void getFavoritesByClient(int clientId)
  {
    socketClient.sendRequest(new Request(RequestType.Get_Favorites_By_Client, clientId));
  }

  // ── TENANCY APPLICATIONS ──────────────────────────────────────────────────

  @Override
  public void addTenancyApplication(TenancyApplication app)
  {
    socketClient.sendRequest(new Request(RequestType.Submit_Tenancy_Application, app));
  }

  @Override
  public void getTenancyApplicationsByOwner(int ownerId)
  {
    socketClient.sendRequest(new Request(RequestType.Get_Tenancy_Apps_By_Owner, ownerId));
  }

  @Override
  public void getTenancyApplicationsByClient(int clientId)
  {
    socketClient.sendRequest(new Request(RequestType.Get_Tenancy_Apps_By_Client, clientId));
  }

  @Override
  public void deleteTenancyApplication(int id)
  {
    socketClient.sendRequest(new Request(RequestType.Delete_Tenancy_Application, id));
  }

  @Override
  public void updateTenancyApplicationStatus(int id, String status)
  {
    socketClient.sendRequest(new Request(RequestType.Update_Tenancy_Application_Status, id, status));
  }

  // ── ACCOUNT ───────────────────────────────────────────────────────────────

  @Override
  public void deleteClient(int clientId)
  {
    socketClient.sendRequest(new Request(RequestType.Delete_Client, clientId));
  }

  @Override
  public void deletePropertyOwner(int ownerId)
  {
    socketClient.sendRequest(new Request(RequestType.Delete_Property_Owner, ownerId));
  }

  // ── LOOKUP ────────────────────────────────────────────────────────────────

  @Override
  public void getOwnerById(int ownerId)
  {
    socketClient.sendRequest(new Request(RequestType.Get_Owner_By_Id, ownerId));
  }

  // ── EVENT RELAY ───────────────────────────────────────────────────────────
  // Receives events from the socket reader thread and re-fires to ViewModels.
  // ViewModels are responsible for Platform.runLater() when updating JavaFX state.

  @Override
  public void propertyChange(PropertyChangeEvent evt)
  {
    String name = evt.getPropertyName();
    Object raw  = evt.getNewValue();

    switch (name) {
      // ── Auth ──
      case "LOGIN_SUCCESS_CLIENT": {
        Client c = gson.fromJson(gson.toJson(raw), Client.class);
        support.firePropertyChange(name, null, c);
        break;
      }
      case "LOGIN_SUCCESS_OWNER": {
        PropertyOwner o = gson.fromJson(gson.toJson(raw), PropertyOwner.class);
        support.firePropertyChange(name, null, o);
        break;
      }
      case "LOGIN_SUCCESS_ADMIN": {
        Admin a = gson.fromJson(gson.toJson(raw), Admin.class);
        support.firePropertyChange(name, null, a);
        break;
      }
      case "ClientRegistered": {
        Client c = gson.fromJson(gson.toJson(raw), Client.class);
        support.firePropertyChange(name, null, c);
        break;
      }

      // ── Listings ──
      case "GET_ALL_LISTINGS":
      case "AVAILABLE_LISTINGS":
      case "LISTINGS_BY_OWNER": {
        Type t = new TypeToken<ArrayList<Listing>>(){}.getType();
        ArrayList<Listing> list = gson.fromJson(gson.toJson(raw), t);
        support.firePropertyChange(name, null, list);
        break;
      }
      case "ListingAdded":
      case "ListingUpdated": {
        Listing l = gson.fromJson(gson.toJson(raw), Listing.class);
        support.firePropertyChange(name, null, l);
        break;
      }
      case "ListingRemoved": {
        int id = ((Double) raw).intValue();
        support.firePropertyChange(name, null, id);
        break;
      }

      // ── Bookings ──
      case "GET_ALL_BOOKINGS":
      case "BOOKINGS_BY_CLIENT": {
        Type t = new TypeToken<ArrayList<Booking>>(){}.getType();
        ArrayList<Booking> list = gson.fromJson(gson.toJson(raw), t);
        support.firePropertyChange(name, null, list);
        break;
      }
      case "BookingAdded":
      case "BookingUpdated": {
        Booking b = gson.fromJson(gson.toJson(raw), Booking.class);
        support.firePropertyChange(name, null, b);
        break;
      }
      case "BookingRemoved": {
        // raw is int[] serialised by Gson as List<Double>
        Type t = new TypeToken<ArrayList<Double>>(){}.getType();
        ArrayList<Double> ids = gson.fromJson(gson.toJson(raw), t);
        int[] arr = {ids.get(0).intValue(), ids.get(1).intValue()};
        support.firePropertyChange(name, null, arr);
        break;
      }

      // ── Owner Applications ──
      case "GET_ALL_OWNER_APPLICATIONS": {
        Type t = new TypeToken<ArrayList<OwnerApplication>>(){}.getType();
        ArrayList<OwnerApplication> list = gson.fromJson(gson.toJson(raw), t);
        support.firePropertyChange(name, null, list);
        break;
      }
      case "OwnerApplicationAdded":
      case "OwnerApplicationUpdated": {
        OwnerApplication a = gson.fromJson(gson.toJson(raw), OwnerApplication.class);
        support.firePropertyChange(name, null, a);
        break;
      }
      case "OwnerApplicationRemoved":
      case "OwnerApplicationApproved":
      case "OwnerApplicationRejected": {
        int id = ((Double) raw).intValue();
        support.firePropertyChange(name, null, id);
        break;
      }

      // ── Favorites ──
      case "FAVORITES_BY_CLIENT": {
        Type t = new TypeToken<ArrayList<Listing>>(){}.getType();
        ArrayList<Listing> list = gson.fromJson(gson.toJson(raw), t);
        support.firePropertyChange(name, null, list);
        break;
      }
      case "FavoriteAdded":
      case "FavoriteRemoved": {
        Type t = new TypeToken<ArrayList<Double>>(){}.getType();
        ArrayList<Double> ids = gson.fromJson(gson.toJson(raw), t);
        int[] arr = {ids.get(0).intValue(), ids.get(1).intValue()};
        support.firePropertyChange(name, null, arr);
        break;
      }

      // ── Tenancy Applications ──
      case "TENANCY_APPS_BY_OWNER":
      case "TENANCY_APPS_BY_CLIENT": {
        Type t = new TypeToken<ArrayList<TenancyApplication>>(){}.getType();
        ArrayList<TenancyApplication> list = gson.fromJson(gson.toJson(raw), t);
        support.firePropertyChange(name, null, list);
        break;
      }
      case "TenancyApplicationAdded": {
        TenancyApplication a = gson.fromJson(gson.toJson(raw), TenancyApplication.class);
        support.firePropertyChange(name, null, a);
        break;
      }
      case "TenancyApplicationDeleted":
      case "TenancyApplicationStatusUpdated": {
        int id = ((Double) raw).intValue();
        support.firePropertyChange(name, null, id);
        break;
      }

      // ── Account ──
      case "ClientDeleted":
      case "PropertyOwnerDeleted": {
        int id = ((Double) raw).intValue();
        support.firePropertyChange(name, null, id);
        break;
      }

      // ── Lookup ──
      case "OWNER_BY_ID": {
        PropertyOwner o = gson.fromJson(gson.toJson(raw), PropertyOwner.class);
        support.firePropertyChange(name, null, o);
        break;
      }

      // ── Errors ──
      case "ERROR":
      default: {
        support.firePropertyChange(name, null, raw);
        break;
      }
    }
  }

  // ── LISTENERS ─────────────────────────────────────────────────────────────

  @Override
  public void addPropertyChangeListener(PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(listener);
  }

  @Override
  public void removePropertyChangeListener(PropertyChangeListener listener)
  {
    support.removePropertyChangeListener(listener);
  }
}
