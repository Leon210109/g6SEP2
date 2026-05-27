package Server;

import Model.Client;
import Model.Date;
import Model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class ClientDAO {
    public void createClient(Client client) {
        try {
            Connection connection = DatabaseConnection.getConnection();

            String sql = """
                    INSERT INTO sep2.client
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

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(
                    1,
                    client.getFirstName());

            statement.setString(
                    2,
                    client.getLastName());

            statement.setString(
                    3,
                    client.getEmail());

            statement.setString(
                    4,
                    client.getPhoneNumber());

            statement.setString(
                    5,
                    client.getUsername());

            statement.setString(
                    6,
                    client.getPassword());

            statement.setDate(
                    7,
                    java.sql.Date.valueOf(
                            LocalDate.of(
                                    client.getDOB().getYear(),
                                    client.getDOB().getMonth(),
                                    client.getDOB().getDay())));

            statement.setString(
                    8,
                    client.getGender());

            statement.setString(
                    9,
                    client.getNationality());

            statement.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Client getClientById(int id) {
        try {
            Connection connection = DatabaseConnection.getConnection();

            String sql = "SELECT * FROM sep2.client WHERE id = ?";

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, id);

            ResultSet rs = statement.executeQuery();

            Client client = null;

            if (rs.next()) {
                User user = new User(
                        rs.getString("username"),
                        rs.getString("password"));

                LocalDate dob = rs.getDate("dateOfBirth")
                        .toLocalDate();

                Date date = new Date(
                        dob.getDayOfMonth(),
                        dob.getMonthValue(),
                        dob.getYear());

                client = new Client(
                        rs.getInt("id"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("email"),
                        rs.getString("phoneNumber"),
                        rs.getString("username"),
                        rs.getString("password"),
                        date,
                        rs.getString("gender"),
                        rs.getString("nationality"));
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

            String sql = "SELECT * FROM sep2.client WHERE username = ?";

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, username);

            ResultSet rs = statement.executeQuery();

            Client client = null;

            if (rs.next()) {
                User user = new User(
                        rs.getString("username"),
                        rs.getString("password"));

                LocalDate dob = rs.getDate("dateOfBirth")
                        .toLocalDate();

                Date date = new Date(
                        dob.getDayOfMonth(),
                        dob.getMonthValue(),
                        dob.getYear());

                client = new Client(
                        rs.getInt("id"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("email"),
                        rs.getString("phoneNumber"),
                        rs.getString("username"),
                        rs.getString("password"),
                        date,
                        rs.getString("gender"),
                        rs.getString("nationality"));
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

            String sql = "SELECT * FROM sep2.client";

            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet rs = statement.executeQuery();

            ArrayList<Client> clients = new ArrayList<>();

            while (rs.next()) {
                User user = new User(
                        rs.getString("username"),
                        rs.getString("password"));

                LocalDate dob = rs.getDate("dateOfBirth")
                        .toLocalDate();

                Date date = new Date(
                        dob.getDayOfMonth(),
                        dob.getMonthValue(),
                        dob.getYear());

                Client client = new Client(
                        rs.getInt("id"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("email"),
                        rs.getString("phoneNumber"),
                        rs.getString("username"),
                        rs.getString("password"),
                        date,
                        rs.getString("gender"),
                        rs.getString("nationality"));

                clients.add(client);
            }

            connection.close();

            return clients;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User login(String username,
            String password) {
        Client client = getClientByUsername(username);

        if (client != null &&
                client
                        .getPassword()
                        .equals(password)) {
            return client.getUser();
        }

        return null;
    }

    public void deleteClient(int id) {
        try {
            Connection connection = DatabaseConnection.getConnection();

            // Delete bookings (no ON DELETE CASCADE on booking.clientId)
            PreparedStatement deleteBookings = connection.prepareStatement(
                    "DELETE FROM sep2.booking WHERE clientId = ?");
            deleteBookings.setInt(1, id);
            deleteBookings.executeUpdate();

            // Delete owner applications (no ON DELETE CASCADE on ownerApplication.clientId)
            PreparedStatement deleteApplications = connection.prepareStatement(
                    "DELETE FROM sep2.ownerApplication WHERE clientId = ?");
            deleteApplications.setInt(1, id);
            deleteApplications.executeUpdate();

            // Delete client (favorites and tenancy_application cascade automatically)
            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM sep2.client WHERE id = ?");
            statement.setInt(1, id);
            statement.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateClient(Client client) {
        try {
            Connection connection = DatabaseConnection.getConnection();

            String sql = """
                    UPDATE sep2.client
                    SET firstName = ?,
                        lastName = ?,
                        email = ?,
                        phoneNumber = ?,
                        username = ?,
                        password = ?,
                        dateOfBirth = ?,
                        gender = ?,
                        nationality = ?

                    WHERE id = ?
                    """;

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(
                    1,
                    client.getFirstName());

            statement.setString(
                    2,
                    client.getLastName());

            statement.setString(
                    3,
                    client.getEmail());

            statement.setString(
                    4,
                    client.getPhoneNumber());

            statement.setString(
                    5,
                    client.getUsername());

            statement.setString(
                    6,
                    client.getPassword());

            statement.setDate(
                    7,
                    java.sql.Date.valueOf(
                            LocalDate.of(
                                    client.getDOB().getYear(),
                                    client.getDOB().getMonth(),
                                    client.getDOB().getDay())));

            statement.setString(
                    8,
                    client.getGender());

            statement.setString(
                    9,
                    client.getNationality());

            statement.setInt(
                    10,
                    client.getID());

            statement.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}