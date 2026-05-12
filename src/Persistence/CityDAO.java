package Persistence;

import Model.City;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CityDAO
{
  public void createCity(City city){
    try{
      Connection connection= DatabaseConnection.getConnection();
      String sql= """
          Insert into city(postal_code,name)
          values(?,?)          
          """;
      PreparedStatement statement=connection.prepareStatement(sql);
      statement.setString(1,city.getPostalCode());
      statement.setString(2,city.getName());
      statement.executeUpdate();
      connection.close();
    }
    catch (SQLException e)
    {
      throw new RuntimeException(e);
    }
  }

}
