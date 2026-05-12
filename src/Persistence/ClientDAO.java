package SEP2.SEP2.src.Persistence;

import SEP2.SEP2.src.Model.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ClientDAO
{
  public void CreateClient(Client client){
    try{
      Connection connection= DatabaseConnection.getConnection();
      String sql= """
          Insert into client(firstName, lastName,
           email, phoneNumber,
            username, password,
             dateOfBirth, gender,
              nationality)
              values (?,?,?,?,?,?,?,?,?)
          """;
      PreparedStatement statement=connection.prepareStatement(sql);
      statement.setString(1,client.getFirstName());
      statement.setString(
          2,
          client.getLastName());
      statement.setString(
          3,
          client.getEmail());

      statement.setString(
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
