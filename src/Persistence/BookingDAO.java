package Persistence;

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
}
