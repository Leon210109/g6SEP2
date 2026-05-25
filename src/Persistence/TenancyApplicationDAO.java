package Persistence;

import Model.TenancyApplication;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TenancyApplicationDAO {

    public void createApplication(TenancyApplication app) {
        try {
            Connection connection = DatabaseConnection.getConnection();
            String sql = """
                INSERT INTO sep2.tenancy_application
                    (client_id, listing_id, has_pets, pets_description, occupation,
                     monthly_income, number_of_occupants, additional_info, status)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'pending')
                """;
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, app.getClientId());
            statement.setInt(2, app.getListingId());
            statement.setBoolean(3, app.isHasPets());
            statement.setString(4, app.getPetsDescription());
            statement.setString(5, app.getOccupation());
            statement.setInt(6, app.getMonthlyIncome());
            statement.setInt(7, app.getNumberOfOccupants());
            statement.setString(8, app.getAdditionalInfo());
            statement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /** All applications for listings owned by the given owner (with JOIN data for display). */
    public ArrayList<TenancyApplication> getApplicationsByOwnerId(int ownerId) {
        try {
            Connection connection = DatabaseConnection.getConnection();
            String sql = """
                SELECT ta.*,
                       c.firstName AS client_first,
                       c.lastName  AS client_last,
                       c.email     AS client_email,
                       l.street    AS listing_street,
                       l.region    AS listing_region
                FROM sep2.tenancy_application ta
                JOIN sep2.client  c ON c.id = ta.client_id
                JOIN sep2.listing l ON l.id = ta.listing_id
                WHERE l.ownerId = ?
                ORDER BY ta.submitted_at DESC
                """;
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, ownerId);
            ResultSet rs = statement.executeQuery();

            ArrayList<TenancyApplication> list = new ArrayList<>();
            while (rs.next()) {
                TenancyApplication app = new TenancyApplication(
                    rs.getInt("id"),
                    rs.getInt("client_id"),
                    rs.getInt("listing_id"),
                    rs.getBoolean("has_pets"),
                    rs.getString("pets_description"),
                    rs.getString("occupation"),
                    rs.getInt("monthly_income"),
                    rs.getInt("number_of_occupants"),
                    rs.getString("additional_info"),
                    rs.getString("status")
                );
                app.setClientName(rs.getString("client_first") + " " + rs.getString("client_last"));
                app.setClientEmail(rs.getString("client_email"));
                app.setListingAddress(rs.getString("listing_street") + ", " + rs.getString("listing_region"));
                list.add(app);
            }
            connection.close();
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /** All applications submitted by a specific client. */
    public ArrayList<TenancyApplication> getApplicationsByClientId(int clientId) {
        try {
            Connection connection = DatabaseConnection.getConnection();
            String sql = """
                SELECT ta.*,
                       l.street AS listing_street,
                       l.region AS listing_region
                FROM sep2.tenancy_application ta
                JOIN sep2.listing l ON l.id = ta.listing_id
                WHERE ta.client_id = ?
                ORDER BY ta.submitted_at DESC
                """;
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, clientId);
            ResultSet rs = statement.executeQuery();

            ArrayList<TenancyApplication> list = new ArrayList<>();
            while (rs.next()) {
                TenancyApplication app = new TenancyApplication(
                    rs.getInt("id"),
                    rs.getInt("client_id"),
                    rs.getInt("listing_id"),
                    rs.getBoolean("has_pets"),
                    rs.getString("pets_description"),
                    rs.getString("occupation"),
                    rs.getInt("monthly_income"),
                    rs.getInt("number_of_occupants"),
                    rs.getString("additional_info"),
                    rs.getString("status")
                );
                app.setListingAddress(rs.getString("listing_street") + ", " + rs.getString("listing_region"));
                list.add(app);
            }
            connection.close();
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateStatus(int applicationId, String status) {
        try {
            Connection connection = DatabaseConnection.getConnection();
            String sql = "UPDATE sep2.tenancy_application SET status = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, status);
            statement.setInt(2, applicationId);
            statement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteApplication(int applicationId) {
        try {
            Connection connection = DatabaseConnection.getConnection();
            String sql = "DELETE FROM sep2.tenancy_application WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, applicationId);
            statement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
