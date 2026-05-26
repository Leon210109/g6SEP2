package Model;

import java.beans.PropertyChangeListener;

public interface RentalModel
{
  // ── Auth ──
  void registerClient(Client client);
  void login(String username, String password);

  // ── Listings ──
  void addListing(Listing listing);
  void removeListing(int listingId);
  void updateListing(Listing listing);
  void getAllListings();
  void getAvailableListings();
  void getListingsByOwner(int ownerId);

  // ── Bookings ──
  void addBooking(Booking booking);
  void removeBooking(int clientId, int listingId);
  void updateBooking(Booking booking);
  void getAllBookings();
  void getBookingsByClient(int clientId);

  // ── Owner Applications ──
  void addOwnerApplication(OwnerApplication ownerApplication);
  void removeOwnerApplication(int applicationId);
  void updateOwnerApplication(OwnerApplication ownerApplication);
  void getAllOwnerApplications();
  void approveOwnerApplication(int applicationId, int adminId);
  void rejectOwnerApplication(int applicationId);

  // ── Favorites ──
  void addFavorite(int clientId, int listingId);
  void removeFavorite(int clientId, int listingId);
  void getFavoritesByClient(int clientId);

  // ── Tenancy Applications ──
  void addTenancyApplication(TenancyApplication app);
  void getTenancyApplicationsByOwner(int ownerId);
  void getTenancyApplicationsByClient(int clientId);
  void deleteTenancyApplication(int id);
  void updateTenancyApplicationStatus(int id, String status);

  // ── Account ──
  void deleteClient(int clientId);
  void deletePropertyOwner(int ownerId);

  // ── Lookup ──
  void getOwnerById(int ownerId);

  // ── Listeners ──
  void addPropertyChangeListener(PropertyChangeListener listener);
  void removePropertyChangeListener(PropertyChangeListener listener);
}
