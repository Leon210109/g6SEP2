package Model;

import Persistence.BookingDAO;
import Persistence.ClientDAO;
import Persistence.ListingDAO;
import Persistence.OwnerApplicationDAO;

import java.util.ArrayList;

public class ModelManager implements Model
{
  private ClientDAO clientDAO;
  private ListingDAO listingDAO;
  private BookingDAO bookingDAO;
  private OwnerApplicationDAO ownerApplicationDAO;

  public ModelManager()
  {
    clientDAO = new ClientDAO();
    listingDAO = new ListingDAO();
    bookingDAO = new BookingDAO();
    ownerApplicationDAO = new OwnerApplicationDAO();
  }

  // ───────────────── CLIENT METHODS ─────────────────

  @Override
  public void registerClient(User user, Client client)
  {
    clientDAO.createClient(client);
  }

  @Override
  public User login(String username, String password)
  {
    return clientDAO.login(username, password);
  }

  // ───────────────── LISTING METHODS ─────────────────

  @Override
  public void addListing(Listing listing)
  {
    listingDAO.CreateListing(listing);
  }

  @Override
  public void removeListing(int listingId)
  {
    listingDAO.deleteListing(listingId);
  }

  @Override
  public void updateListing(Listing listing)
  {
    listingDAO.updateListing(listing);
  }

  @Override
  public ArrayList<Listing> getAllListings()
  {
    return listingDAO.getAllListings();
  }

  // ───────────────── BOOKING METHODS ─────────────────

  @Override
  public void addBooking(Booking booking)
  {
    bookingDAO.CreateBooking(booking);
  }

  @Override
  public void removeBooking(int bookingId)
  {
    bookingDAO.deleteBooking(bookingId);
  }

  @Override
  public void updateBooking(Booking booking)
  {
    bookingDAO.updateBooking(booking);
  }

  @Override
  public ArrayList<Booking> getAllBookings()
  {
    return bookingDAO.getAllBookings();
  }

  // ───────────────── OWNER APPLICATION METHODS ─────────────────

  @Override
  public void addOwnerApplication(
      OwnerApplication ownerApplication)
  {
    ownerApplicationDAO
        .createApplication(ownerApplication);
  }

  @Override
  public void removeOwnerApplication(int applicationId)
  {
    ownerApplicationDAO
        .deleteOwnerApplication(applicationId);
  }

  @Override
  public void updateOwnerApplication(
      OwnerApplication ownerApplication)
  {
    ownerApplicationDAO
        .updateOwnerApplication(ownerApplication);
  }

  @Override
  public ArrayList<OwnerApplication>
  getAllOwnerApplications()
  {
    return ownerApplicationDAO
        .getAllApplications();
  }
}