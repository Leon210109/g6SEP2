package Model;



public class ModelManager implements Model
{
  private UserManager userManager;
  private ListingManager listingManager;
  private BookingManager bookingManager;
  private OwnerApplicationManager ownerApplicationManager;

  public ModelManager()
  {
    userManager = new UserManager();
    listingManager = new ListingManager();
    bookingManager = new BookingManager();
    ownerApplicationManager = new OwnerApplicationManager();
  }
    // this one for user manager methods
  @Override
   public void registerClient(User user, Client client) {
    // UserManager.registerClient only needs the Client (User is embedded in Client)
    userManager.registerClient(client);
  }

  @Override
  public void addUser(User user) {
    // No generic addUser in UserManager — register based on role
    // This is a no-op placeholder; use registerClient/registerPropertyOwner instead
    throw new UnsupportedOperationException("Use registerClient or registerPropertyOwner instead");
  }

   @Override
   public User login(String username, String password){
    return userManager.login(username,password);
   }
   @Override
   public void addListing(Listing listing){
    listingManager.addListing(listing);
   }
   @Override
    public void removeListing(int listingId){
    listingManager.removeListing(listingId);
    }
    @Override
    public void updateListing(Listing listing)
    {
      listingManager.updateListing(listing);
    }
    // booking methods
  @Override
  public void addBooking(Booking booking){
    bookingManager.addBooking(booking);
  }
  @Override
  public void removeBooking(int bookingId) {
    bookingManager.removeBooking(bookingId);
  }

  @Override
  public void updateBooking(Booking booking) {
    bookingManager.updateBooking(booking);
  }
  // owner application methods
  @Override
  public void addOwnerApplication(OwnerApplication ownerApplication)
  {
    ownerApplicationManager.addOwnerApplication(ownerApplication);
  }
  @Override
  public void removeOwnerApplication(int applicationId)
  {
    ownerApplicationManager.removeOwnerApplication(applicationId);
  }
  @Override
  public void updateOwnerApplication(OwnerApplication ownerApplication) {
    ownerApplicationManager.updateApplication(ownerApplication);
  }

}