package Persistence;

import Model.OwnerApplication;
import Model.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class OwnerApplicationDAO
{
  public void createApplication(OwnerApplication ownerApplication){
    try{
      Connection connection= DatabaseConnection.getConnection();
      String sql= """
          INSERT INTO ownerApplication
          (clientId, adminId, 
          submissionDate, status,
           propertyAddress, propertyRegistrationNumber)
          VALUES (?,?,?,?,?,?)        
          """;
      PreparedStatement statement=connection.prepareStatement(sql);
      statement.setInt(1,ownerApplication.getClientId());
      statement.setInt(2,ownerApplication.getAdminId());
      statement.setDate(3,java.sql.Date.valueOf(
          LocalDate.of(
              ownerApplication.getSubmissionDate().getYear(),
              ownerApplication.getSubmissionDate().getMonth(),
              ownerApplication.getSubmissionDate().getDay()
          )

      ));
      statement.setString(4,ownerApplication.getStatus());
      statement.setString(
          5,ownerApplication.getPropertyAddress());
      statement.setString(6,ownerApplication.getPropertyRegistrationNumber());
      statement.executeUpdate();
      connection.close();
    }
    catch (SQLException e)
    {
      throw new RuntimeException(e);
    }
  }

  public OwnerApplication getApplicationById(int id) {
    try {
      Connection connection = DatabaseConnection.getConnection();
      String sql = "SELECT * FROM ownerApplication WHERE id = ?";
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setInt(1, id);
      ResultSet rs = statement.executeQuery();
      
      OwnerApplication application = null;
      if (rs.next()) {
        LocalDate submissionLocalDate = rs.getDate("submissionDate").toLocalDate();
        Date submissionDate = new Date(submissionLocalDate.getDayOfMonth(), submissionLocalDate.getMonthValue(), submissionLocalDate.getYear());
        
        application = new OwnerApplication(
            rs.getInt("id"),
            rs.getInt("clientId"),
            rs.getInt("adminId"),
            submissionDate,
            rs.getString("status"),
            rs.getString("propertyAddress"),
            rs.getString("propertyRegistrationNumber")
        );
      }
      
      connection.close();
      return application;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public ArrayList<OwnerApplication> getApplicationsByClientId(int clientId) {
    try {
      Connection connection = DatabaseConnection.getConnection();
      String sql = "SELECT * FROM ownerApplication WHERE clientId = ?";
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setInt(1, clientId);
      ResultSet rs = statement.executeQuery();
      
      ArrayList<OwnerApplication> applications = new ArrayList<>();
      while (rs.next()) {
        LocalDate submissionLocalDate = rs.getDate("submissionDate").toLocalDate();
        Date submissionDate = new Date(submissionLocalDate.getDayOfMonth(), submissionLocalDate.getMonthValue(), submissionLocalDate.getYear());
        
        OwnerApplication application = new OwnerApplication(
            rs.getInt("id"),
            rs.getInt("clientId"),
            rs.getInt("adminId"),
            submissionDate,
            rs.getString("status"),
            rs.getString("propertyAddress"),
            rs.getString("propertyRegistrationNumber")
        );
        applications.add(application);
      }
      
      connection.close();
      return applications;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public ArrayList<OwnerApplication> getApplicationsByStatus(String status) {
    try {
      Connection connection = DatabaseConnection.getConnection();
      String sql = "SELECT * FROM ownerApplication WHERE status = ?";
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setString(1, status);
      ResultSet rs = statement.executeQuery();
      
      ArrayList<OwnerApplication> applications = new ArrayList<>();
      while (rs.next()) {
        LocalDate submissionLocalDate = rs.getDate("submissionDate").toLocalDate();
        Date submissionDate = new Date(submissionLocalDate.getDayOfMonth(), submissionLocalDate.getMonthValue(), submissionLocalDate.getYear());
        
        OwnerApplication application = new OwnerApplication(
            rs.getInt("id"),
            rs.getInt("clientId"),
            rs.getInt("adminId"),
            submissionDate,
            rs.getString("status"),
            rs.getString("propertyAddress"),
            rs.getString("propertyRegistrationNumber")
        );
        applications.add(application);
      }
      
      connection.close();
      return applications;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public ArrayList<OwnerApplication> getAllApplications() {
    try {
      Connection connection = DatabaseConnection.getConnection();
      String sql = "SELECT * FROM ownerApplication";
      PreparedStatement statement = connection.prepareStatement(sql);
      ResultSet rs = statement.executeQuery();
      
      ArrayList<OwnerApplication> applications = new ArrayList<>();
      while (rs.next()) {
        LocalDate submissionLocalDate = rs.getDate("submissionDate").toLocalDate();
        Date submissionDate = new Date(submissionLocalDate.getDayOfMonth(), submissionLocalDate.getMonthValue(), submissionLocalDate.getYear());
        
        OwnerApplication application = new OwnerApplication(
            rs.getInt("id"),
            rs.getInt("clientId"),
            rs.getInt("adminId"),
            submissionDate,
            rs.getString("status"),
            rs.getString("propertyAddress"),
            rs.getString("propertyRegistrationNumber")
        );
        applications.add(application);
      }
      
      connection.close();
      return applications;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
