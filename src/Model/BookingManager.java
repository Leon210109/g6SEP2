package Model;

import Server.BookingDAO;
import java.util.ArrayList;

public class BookingManager {
  private BookingDAO bookingDAO;

  public BookingManager() {
    bookingDAO = new BookingDAO();
  }

  public void addBooking(Booking booking) {
    bookingDAO.CreateBooking(booking);
  }

  public Booking getBookingByKey(int clientId, int listingId) {
    return bookingDAO.getBookingByKey(clientId, listingId);
  }

  public ArrayList<Booking> getBookingsByClientId(int clientId) {
    return bookingDAO.getBookingsByClientId(clientId);
  }

  public ArrayList<Booking> getBookingsByListingId(int listingId) {
    return bookingDAO.getBookingsByListingId(listingId);
  }

  public ArrayList<Booking> getAllBookings() {
    return bookingDAO.getAllBookings();
  }

  public int getSize() {
    return bookingDAO.getAllBookings().size();
  }

  // Note: Delete and update operations would need DELETE/UPDATE SQL methods in
  // DAO
  // Keeping these as placeholders for now
  public void removeBooking(int clientId, int listingId) {
    bookingDAO.deleteBooking(clientId, listingId);
  }

  public void updateBooking(Booking booking) {
    // TODO: Add updateBooking method to BookingDAO
    throw new UnsupportedOperationException("Update operation not yet implemented in DAO");
  }
}
