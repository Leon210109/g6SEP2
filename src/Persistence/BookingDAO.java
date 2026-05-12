package SEP2.SEP2.src.Persistence;

import SEP2.SEP2.src.Model.Booking;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class BookingDAO
{
  public void CreateBooking(Booking booking){
    try{
      Connection connection= DatabaseConnection.getConnection();
      String sql= """
          Insert into booking(clientId,listingId,start_Date,end_Date,
          check_in_time,check_out_time,
          number_of_people)
              values (?,?,?,?,?,?,?)
          """;
      PreparedStatement statement=connection.prepareStatement(sql);
      statement.setDate(1,booking.getStartDate());
      statement.setDate(
          2,
          booking.getEndDate());
      statement.setInt(
          3,
          booking.getNumber_of_people());

      statement.set(
          4,
          String.valueOf(
              client.getPhoneNumber()));

      statement.setString(
          5,
          client.getUser()
              .getUsername());

      statement.setString(
          6,
          client.getUser()
              .getPassword());

      statement.setDate(
          7,
          java.sql.Date.valueOf(
              client.getDOB()));

      statement.setString(
          8,
          client.getGender());

      statement.setString(
          9,
          client.getNationality());
      statement.executeUpdate();
      connection.close();
    }
    catch (SQLException e)
    {
      throw new RuntimeException(e);
    }
  }
}
