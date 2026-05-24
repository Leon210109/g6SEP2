package Persistence;

import Model.Listing;
import Model.City;
import Model.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class ListingDAO
{
  private CityDAO cityDAO = new CityDAO();

  public void CreateListing(Listing listing){
    try{
      Connection connection= DatabaseConnection.getConnection();
      String sql= """
          Insert into sep2.listing(ownerId, number_of_rooms, number_of_bathrooms, has_balcony, surface_area,
          price, last_Renovated, max_number_of_people, country, region, street, room_number,postal_code,floor)
              values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)
          """;
      PreparedStatement statement=connection.prepareStatement(sql);
      statement.setInt(
          1,
          listing.getOwnerId());
      statement.setInt(
          2,
          listing.getNumberOfRooms());
      statement.setInt(
          3,
          listing.getNumberOfBathrooms());
      statement.setBoolean(
          4,
          listing.isBalcony());
      statement.setFloat(
          5,
          listing.getSurfaceArea());
      statement.setInt(
          6,
          listing.getPrice());
      statement.setDate(
          7,
          java.sql.Date.valueOf(
              LocalDate.of(
                  listing.getLastRenovated().getYear(),
                  listing.getLastRenovated().getMonth(),
                  listing.getLastRenovated().getDay()
              )
          )
      );
      statement.setInt(
          8,
          listing.getMaxNumberOfPeople());
      statement.setString(
          9,
          listing.getCountry());
      statement.setString(
          10,
          listing.getRegion());
      statement.setString(
          11,
          listing.getStreet());
      statement.setString(
          12,
          listing.getRoomNumber());
      statement.setString(
          13,
          listing.getPostalcode());
      statement.setInt(14,listing.getFloor());

      statement.executeUpdate();
      connection.close();
    }
    catch (SQLException e)
    {
      throw new RuntimeException(e);
    }
  }

  public Listing getListingById(int id) {
    try {
      Connection connection = DatabaseConnection.getConnection();
      String sql = "SELECT * FROM sep2.listing WHERE id = ?";
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setInt(1, id);
      ResultSet rs = statement.executeQuery();
      
      Listing listing = null;
      if (rs.next()) {
        LocalDate renovatedLocalDate = rs.getDate("last_Renovated").toLocalDate();
        Date lastRenovated = new Date(renovatedLocalDate.getDayOfMonth(), renovatedLocalDate.getMonthValue(), renovatedLocalDate.getYear());
        Listing listing1 = new Listing(
            rs.getInt("id"),
            rs.getString("street"),
            rs.getString("country"),
            rs.getString("region"),
            rs.getInt("floor"),
            rs.getString("room_number"),
            rs.getInt("number_of_rooms"),
            rs.getInt("number_of_bathrooms"),
            rs.getBoolean("has_balcony"),
            rs.getFloat("surface_area"),
            rs.getInt("price"),
            lastRenovated,
            rs.getInt("ownerId"),
            rs.getInt("max_number_of_people"),
            rs.getString("postal_code")
        );
        

      }
      
      connection.close();
      return listing;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public ArrayList<Listing> getListingsByOwnerId(int ownerId) {
    try {
      Connection connection = DatabaseConnection.getConnection();
      String sql = "SELECT * FROM sep2.listing WHERE ownerId = ?";
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setInt(1, ownerId);
      ResultSet rs = statement.executeQuery();
      
      ArrayList<Listing> listings = new ArrayList<>();
      while (rs.next()) {
        LocalDate renovatedLocalDate = rs.getDate("last_Renovated").toLocalDate();
        Date lastRenovated = new Date(renovatedLocalDate.getDayOfMonth(), renovatedLocalDate.getMonthValue(), renovatedLocalDate.getYear());
        
        City city = new City(rs.getString("region"), "");

        Listing listing = new Listing(
            rs.getInt("id"),
            rs.getString("street"),
            rs.getString("country"),
            rs.getString("region"),
            rs.getInt("floor"),
            rs.getString("room_number"),
            rs.getInt("number_of_rooms"),
            rs.getInt("number_of_bathrooms"),
            rs.getBoolean("has_balcony"),
            rs.getFloat("surface_area"),
            rs.getInt("price"),
            lastRenovated,
            rs.getInt("ownerId"),
            rs.getInt("max_number_of_people"),
            rs.getString("postal_code")
        );
        listings.add(listing);
      }
      
      connection.close();
      return listings;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public ArrayList<Listing> getAllListings() {
    try {
      Connection connection = DatabaseConnection.getConnection();
      String sql = "SELECT * FROM sep2.listing";
      PreparedStatement statement = connection.prepareStatement(sql);
      ResultSet rs = statement.executeQuery();
      
      ArrayList<Listing> listings = new ArrayList<>();
      while (rs.next()) {
        LocalDate renovatedLocalDate = rs.getDate("last_Renovated").toLocalDate();
        Date lastRenovated = new Date(renovatedLocalDate.getDayOfMonth(), renovatedLocalDate.getMonthValue(), renovatedLocalDate.getYear());
        
        City city = new City(rs.getString("region"), "");
        Listing listing = new Listing(
            rs.getInt("id"),
            rs.getString("street"),
            rs.getString("country"),
            rs.getString("region"),
            rs.getInt("floor"),
            rs.getString("room_number"),
            rs.getInt("number_of_rooms"),
            rs.getInt("number_of_bathrooms"),
            rs.getBoolean("has_balcony"),
            rs.getFloat("surface_area"),
            rs.getInt("price"),
            lastRenovated,
            rs.getInt("ownerId"),
            rs.getInt("max_number_of_people"),
            rs.getString("postal_code")
        );
        listings.add(listing);
      }
      
      connection.close();
      return listings;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public ArrayList<Listing> getAvailableListings() {
    try {
      Connection connection = DatabaseConnection.getConnection();
      String sql = "SELECT * FROM sep2.listing WHERE isBooked = false OR isBooked IS NULL";
      PreparedStatement statement = connection.prepareStatement(sql);
      ResultSet rs = statement.executeQuery();
      
      ArrayList<Listing> listings = new ArrayList<>();
      while (rs.next()) {
        LocalDate renovatedLocalDate = rs.getDate("last_Renovated").toLocalDate();
        Date lastRenovated = new Date(renovatedLocalDate.getDayOfMonth(), renovatedLocalDate.getMonthValue(), renovatedLocalDate.getYear());
        
        City city = new City(rs.getString("region"), "");
        
        Listing listing = new Listing(
            rs.getInt("id"),
            rs.getString("street"),
            rs.getString("country"),
            rs.getString("region"),
            rs.getInt("floor"),
            rs.getString("room_number"),
            rs.getInt("number_of_rooms"),
            rs.getInt("number_of_bathrooms"),
            rs.getBoolean("has_balcony"),
            rs.getFloat("surface_area"),
            rs.getInt("price"),
            lastRenovated,
            rs.getInt("ownerId"),
            rs.getInt("max_number_of_people"),
            rs.getString("postal_code")
        );
        listings.add(listing);
      }
      
      connection.close();
      return listings;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
  public void updateListing(Listing listing)
  {
    try
    {
      Connection connection =
          DatabaseConnection.getConnection();

      String sql = """
        UPDATE sep2.listing
        SET numberOfRooms = ?,
            numberOfBathrooms = ?,
            balcony = ?,
            surfaceArea = ?,
            price = ?,
            last_renovated=?,
            maxNumberOfPeople = ?,
            street = ?,
            country = ?,
            region = ?,
            floor = ?,
            roomNumber = ?,
            postal_code=?,
            ownerid=?
            
        WHERE id = ?
        """;

      PreparedStatement statement =
          connection.prepareStatement(sql);

      statement.setInt(
          1,
          listing.getNumberOfRooms());

      statement.setInt(
          2,
          listing.getNumberOfBathrooms());

      statement.setBoolean(
          3,
          listing.isBalcony());

      statement.setFloat(
          4,
          listing.getSurfaceArea());

      statement.setInt(
          5,
          listing.getPrice());

      statement.setInt(
          6,
          listing.getMaxNumberOfPeople());

      statement.setString(
          7,
          listing.getStreet());

      statement.setString(
          8,
          listing.getCountry());

      statement.setString(
          9,
          listing.getRegion());

      statement.setInt(
          11,
          listing.getFloor());

      statement.setString(
          12,
          listing.getRoomNumber());
      statement.setString(13,listing.getPostalcode());
      statement.setInt(14,listing.getOwnerId());

     statement.executeUpdate();

      connection.close();
    }
    catch (SQLException e)
    {
      throw new RuntimeException(e);
    }
  }
  public void deleteListing(int listingId)
  {
    try
    {
      Connection connection =
          DatabaseConnection.getConnection();

      String sql =
          "DELETE FROM sep2.listing WHERE listingId = ?";

      PreparedStatement statement =
          connection.prepareStatement(sql);

      statement.setInt(1, listingId);

      statement.executeUpdate();

      connection.close();
    }
    catch (SQLException e)
    {
      throw new RuntimeException(e);
    }
  }
}
