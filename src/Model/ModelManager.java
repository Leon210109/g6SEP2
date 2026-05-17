package Model;

import Persistence.BookingDAO;
import Persistence.ClientDAO;
import Persistence.ListingDAO;
import Persistence.OwnerApplicationDAO;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class ModelManager implements RentalModel
{
  private ClientDAO clientDAO;
  private ListingDAO listingDAO;
  private BookingDAO bookingDAO;
  private OwnerApplicationDAO ownerApplicationDAO;
  private PropertyChangeSupport support;

  public ModelManager()
  {
    clientDAO = new ClientDAO();
    listingDAO = new ListingDAO();
    bookingDAO = new BookingDAO();
    ownerApplicationDAO = new OwnerApplicationDAO();
    support= new PropertyChangeSupport(this);
  }

  // ───────────────── CLIENT METHODS ─────────────────

  @Override
  public void registerClient(Client client)
  {
    clientDAO.createClient(client);
    support.firePropertyChange("Client registered",null,client);
  }

  @Override
  public User login(String username, String password)
  {
    return clientDAO.login(username, password);
    /* todo check whether it is admin or client or propertyOwner*/

  }

  // ───────────────── LISTING METHODS ─────────────────

  @Override
  public void addListing(Listing listing)
  {
    listingDAO.CreateListing(listing);
    support.firePropertyChange(
        "ListingAdded",
        null,
        listing);
  }

  @Override
  public void removeListing(int listingId)
  {
    listingDAO.deleteListing(listingId);
    support.firePropertyChange(
        "ListingRemoved",
        null,
        listingId);
  }

  @Override
  public void updateListing(Listing listing)
  {
    listingDAO.updateListing(listing);
    support.firePropertyChange(
        "ListingUpdated",
        null,
        listing);
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
    support.firePropertyChange("Booking created",null,booking);
  }

  @Override
  public void removeBooking(int bookingId)
  {
    bookingDAO.deleteBooking(bookingId);
    support.firePropertyChange("Booking removed",null,bookingId);
  }

  @Override
  public void updateBooking(Booking booking)
  {
    bookingDAO.updateBooking(booking);

    support.firePropertyChange(
        "BookingUpdated",
        null,
        booking);
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
    support.firePropertyChange(
        "OwnerApplicationAdded",
        null,
        ownerApplication);
  }

  @Override
  public void removeOwnerApplication(int applicationId)
  {
    ownerApplicationDAO
        .deleteOwnerApplication(applicationId);
    support.firePropertyChange(
        "OwnerApplicationRemoved",
        null,
        applicationId);
  }

  @Override
  public void updateOwnerApplication(
      OwnerApplication ownerApplication)
  {
    ownerApplicationDAO
        .updateOwnerApplication(ownerApplication);
    support.firePropertyChange("OwnerApplication updated",null,ownerApplication);
  }

  @Override
  public ArrayList<OwnerApplication>
  getAllOwnerApplications()
  {
    return ownerApplicationDAO
        .getAllApplications();
  }
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