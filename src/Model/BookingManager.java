package Model;

import java.util.ArrayList;

public class BookingManager
{
  private ArrayList<Booking> bookings;


  public BookingManager(){
    bookings= new ArrayList<>();
  }
  public void addBooking(Booking booking){
    bookings.add(booking);
  }
  public int getSize(){
    return bookings.size();
  }
  public void removeListing(int bookingId){
    for(Booking booking:bookings){
      if(booking.getId()==bookingId){
        bookings.remove(booking);
      }
    }
  }
  public void updateListing(Booking booking){
    for(int i=0; i<bookings.size();i++){
      if(bookings.get(i).getId()== booking.getId()){
        bookings.set(i,booking);
      }
    }
  }
}
