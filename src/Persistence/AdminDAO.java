package Persistence;

import Model.Admin;
import Model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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

  public Admin getAdminById(int id) {
    try {
      Connection connection = DatabaseConnection.getConnection();
      String sql = "SELECT * FROM admin WHERE id = ?";
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setInt(1, id);
      ResultSet rs = statement.executeQuery();
      
      Admin admin = null;
      if (rs.next()) {
        User user = new User(rs.getString("username"), rs.getString("password"));
        admin = new Admin(rs.getInt("id"), user, rs.getString("adminName"));
      }
      
      connection.close();
      return admin;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public Admin getAdminByUsername(String username) {
    try {
      Connection connection = DatabaseConnection.getConnection();
      String sql = "SELECT * FROM admin WHERE username = ?";
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setString(1, username);
      ResultSet rs = statement.executeQuery();
      
      Admin admin = null;
      if (rs.next()) {
        User user = new User(rs.getString("username"), rs.getString("password"));
        admin = new Admin(rs.getInt("id"), user, rs.getString("adminName"));
      }
      
      connection.close();
      return admin;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public ArrayList<Admin> getAllAdmins() {
    try {
      Connection connection = DatabaseConnection.getConnection();
      String sql = "SELECT * FROM admin";
      PreparedStatement statement = connection.prepareStatement(sql);
      ResultSet rs = statement.executeQuery();
      
      ArrayList<Admin> admins = new ArrayList<>();
      while (rs.next()) {
        User user = new User(rs.getString("username"), rs.getString("password"));
        Admin admin = new Admin(rs.getInt("id"), user, rs.getString("adminName"));
        admins.add(admin);
      }
      
      connection.close();
      return admins;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
