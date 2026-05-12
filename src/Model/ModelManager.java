package SEP2.SEP2.src.Model;

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
   public void registerClient(User user,Client client){
    userManager.registerClient(user,client);
   }
   @Override
   public void addUser(User user){
    userManager.addUser(user);
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
  public void removeBooking(int bookingId)
  {
    bookingManager.removeListing(bookingId);
  }
  @Override
  public void updateBooking(Booking booking)
  {
    bookingManager.updateListing(booking);
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
  public void updateOwnerApplication(OwnerApplication ownerApplication){
    ownerApplicationManager.updateListing(ownerApplication);
  }


}