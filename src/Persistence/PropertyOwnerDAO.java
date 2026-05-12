package SEP2.SEP2.src.Persistence;

import SEP2.SEP2.src.Model.PropertyOwner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class PropertyOwnerDAO
{
  public void CreatePropertyOwner(PropertyOwner owner){
    try{
      Connection connection= DatabaseConnection.getConnection();
      String sql= """
          Insert into listing(id, first_name, last_name, email, phoneNumber, 
          username, password, dateOfBirth, gender, nationality, numberOfListings)
              values (?,?,?,?,?,?,?,?,?,?,?)
          """;
      PreparedStatement statement=connection.prepareStatement(sql);
      statement.setInt(1,owner.getID());
      statement.setString(
          2,
          owner.getFirstName());
      statement.setString(
          3,
          owner.getLastName());
      statement.setString(
          4,
          owner.getEmail());
      statement.setString(
          5,
          owner.getPhoneNumber());
      statement.setString(
          6,
          owner.getLogIn().getUsername());
      statement.setString(
          7,
          owner.getLogIn().getPassword());
      statement.setDate(
          8,
          java.sql.Date.valueOf(
              LocalDate.of(
                  owner.getDOB().getYear(),
                  owner.getDOB().getMonth(),
                  owner.getDOB().getDay()
              )
          )
      );
      statement.setString(
          9,
          owner.getGender());
      statement.setString(
          10,
          owner.getNationality());
      statement.setInt(
          11,
          owner.getNumberOflistings());


      statement.executeUpdate();
      connection.close();
    }
    catch (SQLException e)
    {
      throw new RuntimeException(e);
    }
  }
}
