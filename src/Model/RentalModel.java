package Model;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public interface RentalModel
{
  // ───────────────── CLIENT METHODS ─────────────────

  void registerClient( Client client);

  void login(String username, String password);

  // ───────────────── LISTING METHODS ─────────────────

  void addListing(Listing listing);

  void removeListing(int listingId);

  void updateListing(Listing listing);

  void getAllListings();

  // ───────────────── BOOKING METHODS ─────────────────

  void addBooking(Booking booking);

  void removeBooking(int clientId, int listingId);

  void updateBooking(Booking booking);

  void getAllBookings();

  // ───────────────── OWNER APPLICATION METHODS ─────────────────

  void addOwnerApplication(
      OwnerApplication ownerApplication);

  void removeOwnerApplication(int applicationId);

  void updateOwnerApplication(
      OwnerApplication ownerApplication);

 void  getAllOwnerApplications();


  // property change listener
  void addPropertyChangeListener(PropertyChangeListener listener);
  void removePropertyChangeListener(PropertyChangeListener listener);
}