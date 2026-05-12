package Persistence;

import Model.Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AdminDAO {
  public void CreateAdmin(Admin admin) {
    try {
      Connection connection = DatabaseConnection.getConnection();
      String sql = """
          Insert into admin(adminName, username, password)
              values (?,?,?)
          """;
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setString(1, admin.getAdminName());
      statement.setString(
          2,
          admin.getUser().getUsername());
      statement.setString(
          3,
          admin.getUser().getPassword());
      statement.executeUpdate();
      connection.close();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
