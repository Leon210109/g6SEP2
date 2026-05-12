package SEP2.SEP2.src.Model;


public interface Model
{
  // ------------------------------------------------
  // USER METHODS
  // ------------------------------------------------

  void registerClient(User user,
      Client client);

  void addUser(User user);

  User login(String username,
      String password);

  // ------------------------------------------------
  // LISTING METHODS
  // ------------------------------------------------

  void addListing(Listing listing);

  void removeListing(int listingId);

  void updateListing(Listing listing);

  // ------------------------------------------------
  // BOOKING METHODS
  // ------------------------------------------------

  void addBooking(Booking booking);

  void removeBooking(int bookingId);

  void updateBooking(Booking booking);

  // ------------------------------------------------
  // OWNER APPLICATION METHODS
  // ------------------------------------------------

  void addOwnerApplication(
      OwnerApplication ownerApplication);

  void removeOwnerApplication(
      int applicationId);

  void updateOwnerApplication(
      OwnerApplication ownerApplication);
}
