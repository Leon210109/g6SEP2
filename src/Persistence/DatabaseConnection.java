package Persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection
{
  private static final String url =
      "jdbc:postgresql://localhost:5432/roomrental";

  private static final String user =
      "postgres";

  private static final String password =
      "sonim";

  public static Connection getConnection()
      throws SQLException
  {
    return DriverManager.getConnection(
        url,
        user,
        password
    );
  }
}
