package Model;

import Persistence.AdminDAO;
import Persistence.BookingDAO;
import Persistence.CityDAO;
import Persistence.ClientDAO;
import Persistence.FavoriteDAO;
import Persistence.ListingDAO;
import Persistence.OwnerApplicationDAO;
import Persistence.PropertyOwnerDAO;
import Persistence.TenancyApplicationDAO;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class ModelManager implements RentalModel
{
  private final ClientDAO clientDAO;
  private final ListingDAO listingDAO;
  private final BookingDAO bookingDAO;
  private final OwnerApplicationDAO ownerApplicationDAO;
  private final PropertyOwnerDAO propertyOwnerDAO;
  private final AdminDAO adminDAO;
  private final FavoriteDAO favoriteDAO;
  private final TenancyApplicationDAO tenancyApplicationDAO;
  private final CityDAO cityDAO;
  private final PropertyChangeSupport support;

  public ModelManager()
  {
    clientDAO              = new ClientDAO();
    listingDAO             = new ListingDAO();
    bookingDAO             = new BookingDAO();
    ownerApplicationDAO    = new OwnerApplicationDAO();
    propertyOwnerDAO       = new PropertyOwnerDAO();
    adminDAO               = new AdminDAO();
    favoriteDAO            = new FavoriteDAO();
    tenancyApplicationDAO  = new TenancyApplicationDAO();
    cityDAO                = new CityDAO();
    support                = new PropertyChangeSupport(this);
  }

  // ── AUTH ──────────────────────────────────────────────────────────────────

  @Override
  public void registerClient(Client client)
  {
    clientDAO.createClient(client);
    support.firePropertyChange("ClientRegistered", null, client);
  }

  @Override
  public void login(String username, String password)
  {
    try {
      Client client = clientDAO.getClientByUsername(username);
      if (client != null && client.getPassword().equals(password)) {
        support.firePropertyChange("LOGIN_SUCCESS_CLIENT", null, client);
        return;
      }
      PropertyOwner owner = propertyOwnerDAO.getPropertyOwnerByUsername(username);
      if (owner != null && owner.getPassword().equals(password)) {
        support.firePropertyChange("LOGIN_SUCCESS_OWNER", null, owner);
        return;
      }
      Admin admin = adminDAO.getAdminByUsername(username);
      if (admin != null && admin.getPassword().equals(password)) {
        support.firePropertyChange("LOGIN_SUCCESS_ADMIN", null, admin);
        return;
      }
      support.firePropertyChange("ERROR", null, "Invalid username or password");
    } catch (Exception e) {
      support.firePropertyChange("ERROR", null, "Login error: " + e.getMessage());
    }
  }

  // ── LISTINGS ──────────────────────────────────────────────────────────────

  @Override
  public void addListing(Listing listing)
  {
    ensureCityExists(listing.getPostalcode(), listing.getRegion());
    listingDAO.CreateListing(listing);
    support.firePropertyChange("ListingAdded", null, listing);
  }

  @Override
  public void removeListing(int listingId)
  {
    listingDAO.deleteListing(listingId);
    support.firePropertyChange("ListingRemoved", null, listingId);
  }

  @Override
  public void updateListing(Listing listing)
  {
    ensureCityExists(listing.getPostalcode(), listing.getRegion());
    listingDAO.updateListing(listing);
    support.firePropertyChange("ListingUpdated", null, listing);
  }

  @Override
  public void getAllListings()
  {
    ArrayList<Listing> listings = listingDAO.getAllListings();
    support.firePropertyChange("GET_ALL_LISTINGS", null, listings);
  }

  @Override
  public void getAvailableListings()
  {
    ArrayList<Listing> available = listingDAO.getAvailableListings();
    support.firePropertyChange("AVAILABLE_LISTINGS", null, available);
  }

  @Override
  public void getListingsByOwner(int ownerId)
  {
    ArrayList<Listing> listings = listingDAO.getListingsByOwnerId(ownerId);
    support.firePropertyChange("LISTINGS_BY_OWNER", null, listings);
  }

  // ── BOOKINGS ──────────────────────────────────────────────────────────────

  @Override
  public void addBooking(Booking booking)
  {
    if (bookingDAO.isListingBooked(booking.getListingId())) {
      support.firePropertyChange("ERROR", null, "This listing has already been booked.");
      return;
    }
    bookingDAO.CreateBooking(booking);
    support.firePropertyChange("BookingAdded", null, booking);
  }

  @Override
  public void removeBooking(int clientId, int listingId)
  {
    bookingDAO.deleteBooking(clientId, listingId);
    support.firePropertyChange("BookingRemoved", null, new int[]{clientId, listingId});
  }

  @Override
  public void updateBooking(Booking booking)
  {
    bookingDAO.updateBooking(booking);
    support.firePropertyChange("BookingUpdated", null, booking);
  }

  @Override
  public void getAllBookings()
  {
    ArrayList<Booking> bookings = bookingDAO.getAllBookings();
    support.firePropertyChange("GET_ALL_BOOKINGS", null, bookings);
  }

  @Override
  public void getBookingsByClient(int clientId)
  {
    ArrayList<Booking> bookings = bookingDAO.getBookingsByClientId(clientId);
    support.firePropertyChange("BOOKINGS_BY_CLIENT", null, bookings);
  }

  // ── OWNER APPLICATIONS ────────────────────────────────────────────────────

  @Override
  public void addOwnerApplication(OwnerApplication ownerApplication)
  {
    ownerApplicationDAO.createApplication(ownerApplication);
    support.firePropertyChange("OwnerApplicationAdded", null, ownerApplication);
  }

  @Override
  public void removeOwnerApplication(int applicationId)
  {
    ownerApplicationDAO.deleteOwnerApplication(applicationId);
    support.firePropertyChange("OwnerApplicationRemoved", null, applicationId);
  }

  @Override
  public void updateOwnerApplication(OwnerApplication ownerApplication)
  {
    ownerApplicationDAO.updateOwnerApplication(ownerApplication);
    support.firePropertyChange("OwnerApplicationUpdated", null, ownerApplication);
  }

  @Override
  public void getAllOwnerApplications()
  {
    ArrayList<OwnerApplication> apps = ownerApplicationDAO.getAllApplications();
    support.firePropertyChange("GET_ALL_OWNER_APPLICATIONS", null, apps);
  }

  @Override
  public void approveOwnerApplication(int applicationId, int adminId)
  {
    try {
      OwnerApplication app = ownerApplicationDAO.getApplicationById(applicationId);
      if (app == null) {
        support.firePropertyChange("ERROR", null, "Application not found");
        return;
      }
      Client client = clientDAO.getClientById(app.getClientId());
      if (client != null && propertyOwnerDAO.getPropertyOwnerByUsername(client.getUsername()) == null) {
        PropertyOwner newOwner = new PropertyOwner(
            client.getFirstName(), client.getLastName(), client.getEmail(),
            client.getPhoneNumber(), client.getUsername(), client.getPassword(),
            client.getDOB(), client.getGender(), client.getNationality()
        );
        propertyOwnerDAO.CreatePropertyOwner(newOwner);
        clientDAO.deleteClient(client.getID());
      }
      if (adminId > 0) {
        ownerApplicationDAO.updateApplicationStatusAndAdmin(applicationId, "Approved", adminId);
      } else {
        ownerApplicationDAO.updateApplicationStatus(applicationId, "Approved");
      }
      support.firePropertyChange("OwnerApplicationApproved", null, applicationId);
    } catch (Exception e) {
      support.firePropertyChange("ERROR", null, "Approval failed: " + e.getMessage());
    }
  }

  @Override
  public void rejectOwnerApplication(int applicationId)
  {
    ownerApplicationDAO.updateApplicationStatus(applicationId, "Rejected");
    support.firePropertyChange("OwnerApplicationRejected", null, applicationId);
  }

  // ── FAVORITES ─────────────────────────────────────────────────────────────

  @Override
  public void addFavorite(int clientId, int listingId)
  {
    favoriteDAO.addFavorite(clientId, listingId);
    support.firePropertyChange("FavoriteAdded", null, new int[]{clientId, listingId});
  }

  @Override
  public void removeFavorite(int clientId, int listingId)
  {
    favoriteDAO.removeFavorite(clientId, listingId);
    support.firePropertyChange("FavoriteRemoved", null, new int[]{clientId, listingId});
  }

  @Override
  public void getFavoritesByClient(int clientId)
  {
    ArrayList<Listing> favorites = favoriteDAO.getFavoriteListingsByClientId(clientId);
    support.firePropertyChange("FAVORITES_BY_CLIENT", null, favorites);
  }

  // ── TENANCY APPLICATIONS ──────────────────────────────────────────────────

  @Override
  public void addTenancyApplication(TenancyApplication app)
  {
    tenancyApplicationDAO.createApplication(app);
    support.firePropertyChange("TenancyApplicationAdded", null, app);
  }

  @Override
  public void getTenancyApplicationsByOwner(int ownerId)
  {
    ArrayList<TenancyApplication> apps = tenancyApplicationDAO.getApplicationsByOwnerId(ownerId);
    support.firePropertyChange("TENANCY_APPS_BY_OWNER", null, apps);
  }

  @Override
  public void getTenancyApplicationsByClient(int clientId)
  {
    ArrayList<TenancyApplication> apps = tenancyApplicationDAO.getApplicationsByClientId(clientId);
    support.firePropertyChange("TENANCY_APPS_BY_CLIENT", null, apps);
  }

  @Override
  public void deleteTenancyApplication(int id)
  {
    tenancyApplicationDAO.deleteApplication(id);
    support.firePropertyChange("TenancyApplicationDeleted", null, id);
  }

  @Override
  public void updateTenancyApplicationStatus(int id, String status)
  {
    tenancyApplicationDAO.updateStatus(id, status);
    // Refetch to get full object with JOIN data for broadcast
    support.firePropertyChange("TenancyApplicationStatusUpdated", null, id);
  }

  // ── ACCOUNT ───────────────────────────────────────────────────────────────

  @Override
  public void deleteClient(int clientId)
  {
    clientDAO.deleteClient(clientId);
    support.firePropertyChange("ClientDeleted", null, clientId);
  }

  @Override
  public void deletePropertyOwner(int ownerId)
  {
    propertyOwnerDAO.deletePropertyOwner(ownerId);
    support.firePropertyChange("PropertyOwnerDeleted", null, ownerId);
  }

  // ── LOOKUP ────────────────────────────────────────────────────────────────

  @Override
  public void getOwnerById(int ownerId)
  {
    PropertyOwner owner = propertyOwnerDAO.getPropertyOwnerById(ownerId);
    support.firePropertyChange("OWNER_BY_ID", null, owner);
  }

  // ── HELPERS ───────────────────────────────────────────────────────────────

  private void ensureCityExists(String postalCode, String cityName)
  {
    if (postalCode == null || postalCode.isEmpty()) return;
    if (cityDAO.getCityByPostalCode(postalCode) == null) {
      cityDAO.createCity(postalCode, cityName != null ? cityName : "");
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
