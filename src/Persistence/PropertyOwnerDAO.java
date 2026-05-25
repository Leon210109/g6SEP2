package Persistence;

import Model.PropertyOwner;
import Model.User;
import Model.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class PropertyOwnerDAO
{
  public void CreatePropertyOwner(PropertyOwner owner){
    try{
      Connection connection= DatabaseConnection.getConnection();
      String sql= """
          Insert into sep2.propertyowner( first_name, last_name, email, phoneNumber, 
          username, password, dateOfBirth, gender, nationality, numberOfListings)
              values (?,?,?,?,?,?,?,?,?,?)
          """;
      PreparedStatement statement=connection.prepareStatement(sql);
      statement.setString(
          1,
          owner.getFirstName());
      statement.setString(
          2,
          owner.getLastName());
      statement.setString(
          3,
          owner.getEmail());
      statement.setString(
          4,
          owner.getPhoneNumber());
      statement.setString(
          5,
          owner.getUsername());
      statement.setString(
          6,
          owner.getPassword());
      statement.setDate(
          7,
          java.sql.Date.valueOf(
              LocalDate.of(
                  owner.getDOB().getYear(),
                  owner.getDOB().getMonth(),
                  owner.getDOB().getDay()
              )
          )
      );
      statement.setString(
          8,
          owner.getGender());
      statement.setString(
          9,
          owner.getNationality());
      statement.setInt(
          10,
          owner.getNumberOflistings());


      statement.executeUpdate();
      connection.close();
    }
    catch (SQLException e)
    {
      throw new RuntimeException(e);
    }
  }

  public PropertyOwner getPropertyOwnerById(int id) {
    try {
      Connection connection = DatabaseConnection.getConnection();
      String sql = "SELECT * FROM sep2.propertyowner WHERE id = ?";
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setInt(1, id);
      ResultSet rs = statement.executeQuery();
      
      PropertyOwner owner = null;
      if (rs.next()) {
        User user = new User(rs.getString("username"), rs.getString("password"));
        LocalDate dob = rs.getDate("dateOfBirth").toLocalDate();
        Date dateOfBirth = new Date(dob.getDayOfMonth(), dob.getMonthValue(), dob.getYear());
        
        owner = new PropertyOwner(
            rs.getString("first_name"),
            rs.getString("last_name"),
            rs.getString("email"),
            rs.getString("phoneNumber"),
            dateOfBirth,
            rs.getString("username"),
            rs.getString("password"),

            rs.getString("gender"),
            rs.getString("nationality"),
            rs.getInt("id")
        );
      }
      
      connection.close();
      return owner;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public PropertyOwner getPropertyOwnerByUsername(String username) {
    try {
      Connection connection = DatabaseConnection.getConnection();
      String sql = "SELECT * FROM sep2.propertyowner WHERE username = ?";
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setString(1, username);
      ResultSet rs = statement.executeQuery();
      
      PropertyOwner owner = null;
      if (rs.next()) {
        User user = new User(rs.getString("username"), rs.getString("password"));
        LocalDate dob = rs.getDate("dateOfBirth").toLocalDate();
        Date dateOfBirth = new Date(dob.getDayOfMonth(), dob.getMonthValue(), dob.getYear());
        
        owner = new PropertyOwner(
            rs.getString("first_name"),
            rs.getString("last_name"),
            rs.getString("email"),
            rs.getString("phoneNumber"),
            dateOfBirth,
            rs.getString("username"),
            rs.getString("password"),

            rs.getString("gender"),
            rs.getString("nationality"),
            rs.getInt("id")
        );
      }
      
      connection.close();
      return owner;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public ArrayList<PropertyOwner> getAllPropertyOwners() {
    try {
      Connection connection = DatabaseConnection.getConnection();
      String sql = "SELECT * FROM sep2.propertyowner";
      PreparedStatement statement = connection.prepareStatement(sql);
      ResultSet rs = statement.executeQuery();
      
      ArrayList<PropertyOwner> owners = new ArrayList<>();
      while (rs.next()) {
        User user = new User(rs.getString("username"), rs.getString("password"));
        LocalDate dob = rs.getDate("dateOfBirth").toLocalDate();
        Date dateOfBirth = new Date(dob.getDayOfMonth(), dob.getMonthValue(), dob.getYear());
        
        PropertyOwner owner = new PropertyOwner(
            rs.getString("first_name"),
            rs.getString("last_name"),
            rs.getString("email"),
            rs.getString("phoneNumber"),
            rs.getString("username"),
            rs.getString("password"),
            dateOfBirth,
            rs.getString("gender"),
            rs.getString("nationality")

        );
        owners.add(owner);
      }
      
      connection.close();
      return owners;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
