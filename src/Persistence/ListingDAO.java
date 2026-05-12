package SEP2.SEP2.src.Persistence;

import SEP2.SEP2.src.Model.Listing;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class ListingDAO
{
  public void CreateListing(Listing listing){
    try{
      Connection connection= DatabaseConnection.getConnection();
      String sql= """
          Insert into listing(id,ownerId, number_of_rooms, number_of_bathrooms, has_balcony, surface_area,
          price, last_Renovated, max_number_of_people, country, region, street, room_number,streetNo)
              values (?,?,?,?,?,?,?,?,?,?,?,?)
          """;
      PreparedStatement statement=connection.prepareStatement(sql);
      statement.setInt(1,listing.getId());
      statement.setInt(
          2,
          listing.getOwnerId());
      statement.setInt(
          3,
          listing.getNumberOfRooms());
      statement.setInt(
          4,
          listing.getNumberOfBathrooms());
      statement.setBoolean(
          5,
          listing.isBalcony());
      statement.setFloat(
          6,
          listing.getSurfaceArea());
      statement.setInt(
          7,
          listing.getPrice());
      statement.setDate(
          8,
          java.sql.Date.valueOf(
              LocalDate.of(
                  listing.getLastRenovated().getYear(),
                  listing.getLastRenovated().getMonth(),
                  listing.getLastRenovated().getDay()
              )
          )
      );
      statement.setString(
          9,
          listing.getCountry());
      statement.setString(
          10,
          listing.getRegion());
      statement.setString(
          11,
          listing.getStreet());
      statement.setInt(
          12,
          listing.getRoomNumber());
      statement.setInt(
          13,
          listing.getStreetNumber());

      statement.executeUpdate();
      connection.close();
    }
    catch (SQLException e)
    {
      throw new RuntimeException(e);
    }
  }
}
