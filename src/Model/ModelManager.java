package SEP2.SEP2.src.Model;

import java.util.ArrayList;

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
  // related to user method

  @Override
  public void addUser(User user)
  {
    userManager.addUser(user);
  }

  @Override
  public void registerClient(User user,
      Client client)
  {
    userManager.registerClient(user, client);
  }

  @Override
  public User login(String username,
      String password)
  {
    return userManager.login(username,
        password);
  }



  //this is for listings

  @Override
  public void addListing(Listing listing)
  {
    listingManager.addListing(listing);
  }

  @Override
  public void removeListing(int listingId)
  {
    listingManager.removeListing(listingId);
  }

  @Override
  public void updateListing(Listing listing)
  {
    listingManager.updateListing(listing);
  }



  //this one for booking

  @Override
  public void addBooking(Booking booking)
  {
    bookingManager.addBooking(booking);
  }

  @Override
  public void removeBooking(int bookingId)
  {
    bookingManager.removeBooking(bookingId);
  }

  @Override
  public void updateBooking(Booking booking)
  {
    bookingManager.updateBooking(booking);
  }

  // for application

  @Override
  public void addOwnerApplication(
      OwnerApplication application)
  {
    ownerApplicationManager
        .addApplication(application);
  }

  @Override
  public void removeOwnerApplication(
      int applicationId)
  {
    ownerApplicationManager
        .removeApplication(applicationId);
  }

  @Override
  public void updateOwnerApplication(
      OwnerApplication application)
  {
    ownerApplicationManager
        .updateApplication(application);
  }

}