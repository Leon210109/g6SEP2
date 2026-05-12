package Model;

import Persistence.BookingDAO;
import java.util.ArrayList;

public class BookingManager
{
  private BookingDAO bookingDAO;

  public BookingManager(){
    bookingDAO = new BookingDAO();
  }

  public void addBooking(Booking booking){
    bookingDAO.CreateBooking(booking);
  }

  public Booking getBookingById(int bookingId){
    return bookingDAO.getBookingById(bookingId);
  }

  public ArrayList<Booking> getBookingsByClientId(int clientId){
    return bookingDAO.getBookingsByClientId(clientId);
  }

  public ArrayList<Booking> getBookingsByListingId(int listingId){
    return bookingDAO.getBookingsByListingId(listingId);
  }

  public ArrayList<Booking> getAllBookings(){
    return bookingDAO.getAllBookings();
  }

  public int getSize(){
    return bookingDAO.getAllBookings().size();
  }

  // Note: Delete and update operations would need DELETE/UPDATE SQL methods in DAO
  // Keeping these as placeholders for now
  public void removeBooking(int bookingId){
    // TODO: Add deleteBooking method to BookingDAO
    throw new UnsupportedOperationException("Delete operation not yet implemented in DAO");
  }

  public void updateBooking(Booking booking){
    // TODO: Add updateBooking method to BookingDAO
    throw new UnsupportedOperationException("Update operation not yet implemented in DAO");
  }
}
