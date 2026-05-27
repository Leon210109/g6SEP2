package Server;

import java.sql.Connection;
import java.sql.DatabaseMetaData;

public class TestConnection {
  public static void main(String[] args) {
    System.out.println("=== Database Connection Test ===\n");

    try {
      System.out.println("Attempting to connect to database...");
      System.out.println("URL: jdbc:postgresql://localhost:5432/roomrental");
      System.out.println();

      Connection connection = DatabaseConnection.getConnection();

      if (connection != null && !connection.isClosed()) {
        System.out.println("✓ Database connected successfully!\n");

        // Print connection details
        DatabaseMetaData metaData = connection.getMetaData();
        System.out.println("Database Information:");
        System.out.println("  Product: " + metaData.getDatabaseProductName());
        System.out.println("  Version: " + metaData.getDatabaseProductVersion());
        System.out.println("  User: " + metaData.getUserName());
        System.out.println();

        connection.close();
        System.out.println("✓ Connection closed successfully");
      } else {
        System.out.println("✗ Connection is null or closed");
      }
    } catch (Exception e) {
      System.out.println("✗ Database connection failed!\n");
      System.out.println("Error Type: " + e.getClass().getSimpleName());
      System.out.println("Error Message: " + e.getMessage());
      System.out.println();

      // Provide helpful guidance
      System.out.println("Troubleshooting:");
      if (e.getMessage().contains("No suitable driver")) {
        System.out.println("  → PostgreSQL JDBC driver not found");
        System.out.println("  → Run this from your IDE (IntelliJ/Eclipse)");
        System.out.println("  → Make sure postgresql-XX.jar is in your classpath");
      } else if (e.getMessage().contains("password")) {
        System.out.println("  → Check password in DatabaseConnection.java");
      } else if (e.getMessage().contains("Connection refused")) {
        System.out.println("  → PostgreSQL server is not running");
        System.out.println("  → Start PostgreSQL service");
      } else if (e.getMessage().contains("does not exist")) {
        System.out.println("  → Database 'roomrental' doesn't exist");
        System.out.println("  → Create it in DataGrip first");
      }

      System.out.println("\nFor detailed diagnosis, run DatabaseDiagnostic.java");
    }
  }
}