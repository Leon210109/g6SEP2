package Server;

import Model.City;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CityDAO {
  public void createCity(City city) {
    try {
      Connection connection = DatabaseConnection.getConnection();
      String sql = """
          Insert into sep2.city(postal_code,name)
          values(?,?)
          """;
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setString(1, city.getPostalCode());
      statement.setString(2, city.getName());
      statement.executeUpdate();
      connection.close();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public City getCityByPostalCode(String postalCode) {
    try {
      Connection connection = DatabaseConnection.getConnection();
      String sql = "SELECT * FROM sep2.city WHERE postal_code = ?";
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setString(1, postalCode);
      ResultSet rs = statement.executeQuery();

      City city = null;
      if (rs.next()) {
        city = new City(rs.getString("name"), rs.getString("postal_code"));
      }

      connection.close();
      return city;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Convenience method to create city with postal code and name
   */
  public void createCity(String postalCode, String cityName) {
    City city = new City(cityName, postalCode);
    createCity(city);
  }

  public ArrayList<City> getAllCities() {
    try {
      Connection connection = DatabaseConnection.getConnection();
      String sql = "SELECT * FROM sep2.city";
      PreparedStatement statement = connection.prepareStatement(sql);
      ResultSet rs = statement.executeQuery();

      ArrayList<City> cities = new ArrayList<>();
      while (rs.next()) {
        City city = new City(rs.getString("name"), rs.getString("postal_code"));
        cities.add(city);
      }

      connection.close();
      return cities;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

}
