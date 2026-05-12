package Persistence;

import Model.Client;
import Model.User;
import Model.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

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
              LocalDate.of(
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

  public Client getClientById(int id) {
    try {
      Connection connection = DatabaseConnection.getConnection();
      String sql = "SELECT * FROM client WHERE id = ?";
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setInt(1, id);
      ResultSet rs = statement.executeQuery();
      
      Client client = null;
      if (rs.next()) {
        User user = new User(rs.getString("username"), rs.getString("password"));
        LocalDate dob = rs.getDate("dateOfBirth").toLocalDate();
        Date dateOfBirth = new Date(dob.getDayOfMonth(), dob.getMonthValue(), dob.getYear());
        
        client = new Client(
            rs.getString("firstName"),
            rs.getString("lastName"),
            rs.getString("gender"),
            rs.getString("nationality"),
            rs.getString("homeAddress") != null ? rs.getString("homeAddress") : "",
            rs.getString("email"),
            rs.getInt("phoneNumber"),
            dateOfBirth,
            user,
            rs.getInt("id")
        );
      }
      
      connection.close();
      return client;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public Client getClientByUsername(String username) {
    try {
      Connection connection = DatabaseConnection.getConnection();
      String sql = "SELECT * FROM client WHERE username = ?";
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setString(1, username);
      ResultSet rs = statement.executeQuery();
      
      Client client = null;
      if (rs.next()) {
        User user = new User(rs.getString("username"), rs.getString("password"));
        LocalDate dob = rs.getDate("dateOfBirth").toLocalDate();
        Date dateOfBirth = new Date(dob.getDayOfMonth(), dob.getMonthValue(), dob.getYear());
        
        client = new Client(
            rs.getString("firstName"),
            rs.getString("lastName"),
            rs.getString("gender"),
            rs.getString("nationality"),
            rs.getString("homeAddress") != null ? rs.getString("homeAddress") : "",
            rs.getString("email"),
            rs.getInt("phoneNumber"),
            dateOfBirth,
            user,
            rs.getInt("id")
        );
      }
      
      connection.close();
      return client;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public ArrayList<Client> getAllClients() {
    try {
      Connection connection = DatabaseConnection.getConnection();
      String sql = "SELECT * FROM client";
      PreparedStatement statement = connection.prepareStatement(sql);
      ResultSet rs = statement.executeQuery();
      
      ArrayList<Client> clients = new ArrayList<>();
      while (rs.next()) {
        User user = new User(rs.getString("username"), rs.getString("password"));
        LocalDate dob = rs.getDate("dateOfBirth").toLocalDate();
        Date dateOfBirth = new Date(dob.getDayOfMonth(), dob.getMonthValue(), dob.getYear());
        
        Client client = new Client(
            rs.getString("firstName"),
            rs.getString("lastName"),
            rs.getString("gender"),
            rs.getString("nationality"),
            rs.getString("homeAddress") != null ? rs.getString("homeAddress") : "",
            rs.getString("email"),
            rs.getInt("phoneNumber"),
            dateOfBirth,
            user,
            rs.getInt("id")
        );
        clients.add(client);
      }
      
      connection.close();
      return clients;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
