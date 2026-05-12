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
          Insert into listing(id,ownerId, number_of_rooms, number_of_bathrooms, has_balcony, surface_area,
          price, last_Renovated, max_number_of_people, country, region, street, room_number,streetNo)
              values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)
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
      statement.setInt(
          9,
          listing.getMaxNumberOfPeople());
      statement.setString(
          10,
          listing.getCountry());
      statement.setString(
          11,
          listing.getRegion());
      statement.setString(
          12,
          listing.getStreet());
      statement.setInt(
          13,
          listing.getRoomNumber());
      statement.setInt(
          14,
          listing.getStreetNumber());

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
      String sql = "SELECT * FROM listing WHERE id = ?";
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setInt(1, id);
      ResultSet rs = statement.executeQuery();
      
      Listing listing = null;
      if (rs.next()) {
        LocalDate renovatedLocalDate = rs.getDate("last_Renovated").toLocalDate();
        Date lastRenovated = new Date(renovatedLocalDate.getDayOfMonth(), renovatedLocalDate.getMonthValue(), renovatedLocalDate.getYear());
        
        // Create a City object - using region as city name and empty postal code for now
        City city = new City(rs.getString("region"), "");
        
        listing = new Listing(
            rs.getInt("id"),
            rs.getString("street"),
            rs.getString("country"),
            rs.getString("region"),
            rs.getInt("streetNo"),
            rs.getInt("floor"),
            rs.getInt("room_number"),
            city,
            rs.getInt("number_of_rooms"),
            rs.getInt("number_of_bathrooms"),
            rs.getBoolean("has_balcony"),
            rs.getFloat("surface_area"),
            rs.getInt("price"),
            lastRenovated,
            rs.getInt("ownerId"),
            rs.getInt("max_number_of_people")
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
      String sql = "SELECT * FROM listing WHERE ownerId = ?";
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
            rs.getInt("streetNo"),
            rs.getInt("floor"),
            rs.getInt("room_number"),
            city,
            rs.getInt("number_of_rooms"),
            rs.getInt("number_of_bathrooms"),
            rs.getBoolean("has_balcony"),
            rs.getFloat("surface_area"),
            rs.getInt("price"),
            lastRenovated,
            rs.getInt("ownerId"),
            rs.getInt("max_number_of_people")
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
      String sql = "SELECT * FROM listing";
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
            rs.getInt("streetNo"),
            rs.getInt("floor"),
            rs.getInt("room_number"),
            city,
            rs.getInt("number_of_rooms"),
            rs.getInt("number_of_bathrooms"),
            rs.getBoolean("has_balcony"),
            rs.getFloat("surface_area"),
            rs.getInt("price"),
            lastRenovated,
            rs.getInt("ownerId"),
            rs.getInt("max_number_of_people")
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
      String sql = "SELECT * FROM listing WHERE isBooked = false OR isBooked IS NULL";
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
            rs.getInt("streetNo"),
            rs.getInt("floor"),
            rs.getInt("room_number"),
            city,
            rs.getInt("number_of_rooms"),
            rs.getInt("number_of_bathrooms"),
            rs.getBoolean("has_balcony"),
            rs.getFloat("surface_area"),
            rs.getInt("price"),
            lastRenovated,
            rs.getInt("ownerId"),
            rs.getInt("max_number_of_people")
        );
        listings.add(listing);
      }
      
      connection.close();
      return listings;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
