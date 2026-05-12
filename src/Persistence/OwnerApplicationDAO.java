package Persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

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
}
