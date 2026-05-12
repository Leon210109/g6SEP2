package Persistence;
import Model.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ClientDAO
{
  public void createClient(Client client)
  {
    try
    {
      Connection connection =
          DatabaseConnection.getConnection();

      String sql = """
          INSERT INTO client
          (firstName,
           lastName,
           email,
           phoneNumber,
           username,
           password,
           dateOfBirth,
           gender,
           nationality)
           
           VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
          """;

      PreparedStatement statement =
          connection.prepareStatement(sql);

      statement.setString(
          1,
          client.getFirstName());

      statement.setString(
          2,
          client.getLastName());

      statement.setString(
          3,
          client.getEmail());

      statement.setInt(
          4,
          client.getPhoneNumber());

      statement.setString(
          5,
          client.getUser().getUsername());

      statement.setString(
          6,
          client.getUser().getPassword());

      statement.setDate(
          7,
          java.sql.Date.valueOf(
              java.time.LocalDate.of(
                  client.getDOB().getYear(),
                  client.getDOB().getMonth(),
                  client.getDOB().getDay()
              )
          )
      );

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
