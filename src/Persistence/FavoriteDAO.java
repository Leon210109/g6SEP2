package Persistence;

import Model.Date;
import Model.Listing;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class FavoriteDAO {

    public void addFavorite(int clientId, int listingId) {
        try {
            Connection connection = DatabaseConnection.getConnection();
            String sql = "INSERT INTO sep2.favorite (clientId, listingId) VALUES (?, ?) ON CONFLICT DO NOTHING";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, clientId);
            statement.setInt(2, listingId);
            statement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeFavorite(int clientId, int listingId) {
        try {
            Connection connection = DatabaseConnection.getConnection();
            String sql = "DELETE FROM sep2.favorite WHERE clientId = ? AND listingId = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, clientId);
            statement.setInt(2, listingId);
            statement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isFavorite(int clientId, int listingId) {
        try {
            Connection connection = DatabaseConnection.getConnection();
            String sql = "SELECT COUNT(*) FROM sep2.favorite WHERE clientId = ? AND listingId = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, clientId);
            statement.setInt(2, listingId);
            ResultSet rs = statement.executeQuery();
            boolean result = rs.next() && rs.getInt(1) > 0;
            connection.close();
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Listing> getFavoriteListingsByClientId(int clientId) {
        try {
            Connection connection = DatabaseConnection.getConnection();
            String sql = """
                SELECT l.* FROM sep2.listing l
                INNER JOIN sep2.favorite f ON l.id = f.listingId
                WHERE f.clientId = ?
                """;
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, clientId);
            ResultSet rs = statement.executeQuery();

            ArrayList<Listing> listings = new ArrayList<>();
            while (rs.next()) {
                LocalDate renovatedLocalDate = rs.getDate("last_Renovated").toLocalDate();
                Date lastRenovated = new Date(
                    renovatedLocalDate.getDayOfMonth(),
                    renovatedLocalDate.getMonthValue(),
                    renovatedLocalDate.getYear()
                );
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
                LocalTime checkIn = rs.getTime("check_in_time").toLocalTime();
                LocalTime checkOut = rs.getTime("check_out_time").toLocalTime();
                listing.setCheckInTime(checkIn);
                listing.setCheckOutTime(checkOut);
                listing.setListingType(rs.getString("listing_type"));
                listings.add(listing);
            }

            connection.close();
            return listings;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
