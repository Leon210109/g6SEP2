package Model;

import java.util.ArrayList;

public interface Model
{
  // ───────────────── CLIENT METHODS ─────────────────

  void registerClient(User user, Client client);

  User login(String username, String password);

  // ───────────────── LISTING METHODS ─────────────────

  void addListing(Listing listing);

  void removeListing(int listingId);

  void updateListing(Listing listing);

  ArrayList<Listing> getAllListings();

  // ───────────────── BOOKING METHODS ─────────────────

  void addBooking(Booking booking);

  void removeBooking(int bookingId);

  void updateBooking(Booking booking);

  ArrayList<Booking> getAllBookings();

  // ───────────────── OWNER APPLICATION METHODS ─────────────────

  void addOwnerApplication(
      OwnerApplication ownerApplication);

  void removeOwnerApplication(int applicationId);

  void updateOwnerApplication(
      OwnerApplication ownerApplication);

  ArrayList<OwnerApplication>
  getAllOwnerApplications();
}