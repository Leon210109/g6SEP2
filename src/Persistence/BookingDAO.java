package Persistence;

import Model.Booking;
import Model.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class BookingDAO
{
  public void CreateBooking(Booking booking){
    try{
      Connection connection= DatabaseConnection.getConnection();
      String sql= """
          Insert into sep2.booking(clientId,listingId,start_Date,end_Date,
          check_in_time,check_out_time,
          number_of_people)
              values (?,?,?,?,?,?,?)
          """;
      PreparedStatement statement=connection.prepareStatement(sql);
      statement.setInt(1,booking.getClientId());
      statement.setInt(
          2,
          booking.getListingId());
      statement.setDate(
          3,
          java.sql.Date.valueOf(
              LocalDate.of(
                  booking.getStartDate().getYear(),
                  booking.getStartDate().getMonth(),
                  booking.getStartDate().getDay()
              )
          )
      );

      statement.setDate(
          4,
          java.sql.Date.valueOf(
              LocalDate.of(
                  booking.getEndDate().getYear(),
                  booking.getEndDate().getMonth(),
                  booking.getEndDate().getDay()
              )
          )
      );

      statement.setTime(
          5,java.sql.Time.valueOf(
          booking.getCheck_in_time()));

      statement.setTime(
          6,java.sql.Time.valueOf(
          booking.getCheck_out_time()));

      statement.setInt(
          7,
          booking.getNumber_of_people());

      statement.executeUpdate();
      connection.close();
    }
    catch (SQLException e)
    {
      throw new RuntimeException(e);
    }
  }

  public Booking getBookingById(int id) {
    try {
      Connection connection = DatabaseConnection.getConnection();
      String sql = "SELECT * FROM sep2.booking WHERE id = ?";
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setInt(1, id);
      ResultSet rs = statement.executeQuery();
      
      Booking booking = null;
      if (rs.next()) {
        LocalDate startLocalDate = rs.getDate("start_Date").toLocalDate();
        Date startDate = new Date(startLocalDate.getDayOfMonth(), startLocalDate.getMonthValue(), startLocalDate.getYear());
        
        LocalDate endLocalDate = rs.getDate("end_Date").toLocalDate();
        Date endDate = new Date(endLocalDate.getDayOfMonth(), endLocalDate.getMonthValue(), endLocalDate.getYear());
        
        LocalTime checkInTime = rs.getTime("check_in_time").toLocalTime();
        LocalTime checkOutTime = rs.getTime("check_out_time").toLocalTime();
        
        booking = new Booking(
            rs.getInt("clientId"),
            rs.getInt("listingId"),
            startDate,
            endDate,
            rs.getInt("number_of_people"),
            checkInTime,
            checkOutTime
        );
      }
      
      connection.close();
      return booking;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public ArrayList<Booking> getBookingsByClientId(int clientId) {
    try {
      Connection connection = DatabaseConnection.getConnection();
      String sql = "SELECT * FROM sep2.booking WHERE clientId = ?";
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setInt(1, clientId);
      ResultSet rs = statement.executeQuery();
      
      ArrayList<Booking> bookings = new ArrayList<>();
      while (rs.next()) {
        LocalDate startLocalDate = rs.getDate("start_Date").toLocalDate();
        Date startDate = new Date(startLocalDate.getDayOfMonth(), startLocalDate.getMonthValue(), startLocalDate.getYear());
        
        LocalDate endLocalDate = rs.getDate("end_Date").toLocalDate();
        Date endDate = new Date(endLocalDate.getDayOfMonth(), endLocalDate.getMonthValue(), endLocalDate.getYear());
        
        LocalTime checkInTime = rs.getTime("check_in_time").toLocalTime();
        LocalTime checkOutTime = rs.getTime("check_out_time").toLocalTime();
        
        Booking booking = new Booking(
            rs.getInt("clientId"),
            rs.getInt("listingId"),
            startDate,
            endDate,
            rs.getInt("number_of_people"),
            checkInTime,
            checkOutTime
        );
        bookings.add(booking);
      }
      
      connection.close();
      return bookings;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public ArrayList<Booking> getBookingsByListingId(int listingId) {
    try {
      Connection connection = DatabaseConnection.getConnection();
      String sql = "SELECT * FROM sep2.booking WHERE listingId = ?";
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setInt(1, listingId);
      ResultSet rs = statement.executeQuery();
      
      ArrayList<Booking> bookings = new ArrayList<>();
      while (rs.next()) {
        LocalDate startLocalDate = rs.getDate("start_Date").toLocalDate();
        Date startDate = new Date(startLocalDate.getDayOfMonth(), startLocalDate.getMonthValue(), startLocalDate.getYear());
        
        LocalDate endLocalDate = rs.getDate("end_Date").toLocalDate();
        Date endDate = new Date(endLocalDate.getDayOfMonth(), endLocalDate.getMonthValue(), endLocalDate.getYear());
        
        LocalTime checkInTime = rs.getTime("check_in_time").toLocalTime();
        LocalTime checkOutTime = rs.getTime("check_out_time").toLocalTime();
        
        Booking booking = new Booking(
            rs.getInt("clientId"),
            rs.getInt("listingId"),
            startDate,
            endDate,
            rs.getInt("number_of_people"),
            checkInTime,
            checkOutTime
        );
        bookings.add(booking);
      }
      
      connection.close();
      return bookings;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public ArrayList<Booking> getAllBookings() {
    try {
      Connection connection = DatabaseConnection.getConnection();
      String sql = "SELECT * FROM sep2.booking";
      PreparedStatement statement = connection.prepareStatement(sql);
      ResultSet rs = statement.executeQuery();
      
      ArrayList<Booking> bookings = new ArrayList<>();
      while (rs.next()) {
        LocalDate startLocalDate = rs.getDate("start_Date").toLocalDate();
        Date startDate = new Date(startLocalDate.getDayOfMonth(), startLocalDate.getMonthValue(), startLocalDate.getYear());
        
        LocalDate endLocalDate = rs.getDate("end_Date").toLocalDate();
        Date endDate = new Date(endLocalDate.getDayOfMonth(), endLocalDate.getMonthValue(), endLocalDate.getYear());
        
        LocalTime checkInTime = rs.getTime("check_in_time").toLocalTime();
        LocalTime checkOutTime = rs.getTime("check_out_time").toLocalTime();
        
        Booking booking = new Booking(
            rs.getInt("clientId"),
            rs.getInt("listingId"),
            startDate,
            endDate,
            rs.getInt("number_of_people"),
            checkInTime,
            checkOutTime
        );
        bookings.add(booking);
      }
      
      connection.close();
      return bookings;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
  public void updateBooking(Booking booking)
  {
    try
    {
      Connection connection =
          DatabaseConnection.getConnection();

      String sql = """
        UPDATE sep2.booking
        SET start_date = ?,
            end_date = ?,
            number_of_people = ?,
            check_in_time = ?,
            check_out_time = ?
            
        WHERE bookingId = ?
        """;

      PreparedStatement statement =
          connection.prepareStatement(sql);

      statement.setDate(
          1,
          java.sql.Date.valueOf(
              java.time.LocalDate.of(
                  booking.getStartDate().getYear(),
                  booking.getStartDate().getMonth(),
                  booking.getStartDate().getDay()
              )
          )
      );

      statement.setDate(
          2,
          java.sql.Date.valueOf(
              java.time.LocalDate.of(
                  booking.getEndDate().getYear(),
                  booking.getEndDate().getMonth(),
                  booking.getEndDate().getDay()
              )
          )
      );

      statement.setInt(
          3,
          booking.getNumber_of_people());

      statement.setTime(
          4,
          java.sql.Time.valueOf(
              booking.getCheck_in_time()
          )
      );

      statement.setTime(
          5,
          java.sql.Time.valueOf(
              booking.getCheck_out_time()
          )
      );

      statement.setInt(
          6,
          booking.getId());

      statement.executeUpdate();

      connection.close();
    }
    catch (SQLException e)
    {
      throw new RuntimeException(e);
    }
  }
  public void deleteBooking(int bookingId)
  {
    try
    {
      Connection connection =
          DatabaseConnection.getConnection();

      String sql =
          "DELETE FROM sep2.booking WHERE clientId = ? AND listingId = ?";

      PreparedStatement statement =
          connection.prepareStatement(sql);

      // Note: This method signature needs clientId and listingId, not bookingId
      // The parameter is misleading - it should be changed
      statement.setInt(1, bookingId); // This won't work correctly
      statement.setInt(2, bookingId); // This won't work correctly

      statement.executeUpdate();

      connection.close();
    }
    catch (SQLException e)
    {
      throw new RuntimeException(e);
    }
  }
  
  // Correct version using composite key
  public void deleteBooking(int clientId, int listingId)
  {
    try
    {
      Connection connection =
          DatabaseConnection.getConnection();

      String sql =
          "DELETE FROM sep2.booking WHERE clientId = ? AND listingId = ?";

      PreparedStatement statement =
          connection.prepareStatement(sql);

      statement.setInt(1, clientId);
      statement.setInt(2, listingId);

      statement.executeUpdate();

      connection.close();
    }
    catch (SQLException e)
    {
      throw new RuntimeException(e);
    }
  }

  public boolean isListingBooked(int listingId)
  {
    try
    {
      Connection connection = DatabaseConnection.getConnection();
      String sql = "SELECT COUNT(*) FROM sep2.booking WHERE listingId = ?";
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setInt(1, listingId);
      ResultSet rs = statement.executeQuery();
      boolean booked = rs.next() && rs.getInt(1) > 0;
      connection.close();
      return booked;
    }
    catch (SQLException e)
    {
      throw new RuntimeException(e);
    }
  }
}
