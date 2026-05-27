package Server;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * Diagnostic tool to test database connection settings.
 * Run this from your IDE to verify your database connection.
 */
public class DatabaseDiagnostic {

    public static void main(String[] args) {
        System.out.println("=== DATABASE CONNECTION DIAGNOSTIC ===\n");

        // Print current settings
        printSettings();

        // Test connection
        testConnection();
    }

    private static void printSettings() {
        System.out.println("Current Database Settings:");
        System.out.println("  URL: jdbc:postgresql://192.168.1.107:5432/roomrental");
        System.out.println("  Database: roomrental");
        System.out.println("  Schema: sep2");
        System.out.println("  User: postgres");
        System.out.println("  Password: [hidden for security]");
        System.out.println();
    }

    private static void testConnection() {
        System.out.println("Testing connection...\n");

        try {
            // Attempt to load PostgreSQL driver
            Class.forName("org.postgresql.Driver");
            System.out.println("✓ PostgreSQL JDBC driver found!");

        } catch (ClassNotFoundException e) {
            System.out.println("✗ PostgreSQL JDBC driver NOT found!");
            System.out.println("  Solution: Make sure postgresql-XX.X.X.jar is in your classpath");
            System.out.println("  If using an IDE: Add the PostgreSQL driver to your project libraries");
            System.out.println();
            return;
        }

        try {
            // Attempt connection
            Connection connection = DatabaseConnection.getConnection();

            if (connection != null && !connection.isClosed()) {
                System.out.println("✓ Database connection SUCCESSFUL!\n");

                // Get database info
                DatabaseMetaData metaData = connection.getMetaData();
                System.out.println("Connection Details:");
                System.out.println("  Database Product: " + metaData.getDatabaseProductName());
                System.out.println("  Database Version: " + metaData.getDatabaseProductVersion());
                System.out.println("  Driver Name: " + metaData.getDriverName());
                System.out.println("  Driver Version: " + metaData.getDriverVersion());
                System.out.println("  URL: " + metaData.getURL());
                System.out.println("  Username: " + metaData.getUserName());

                connection.close();
                System.out.println("\n✓ Connection closed successfully");

            } else {
                System.out.println("✗ Connection is null or closed");
            }

        } catch (SQLException e) {
            System.out.println("✗ Database connection FAILED!\n");
            System.out.println("Error Details:");
            System.out.println("  Message: " + e.getMessage());
            System.out.println("  SQL State: " + e.getSQLState());
            System.out.println("  Error Code: " + e.getErrorCode());
            System.out.println();

            // Provide helpful suggestions based on error
            String message = e.getMessage().toLowerCase();
            if (message.contains("password")) {
                System.out.println("Possible Issue: Incorrect password");
                System.out.println("  → Check password in DatabaseConnection.java");
            } else if (message.contains("authentication")) {
                System.out.println("Possible Issue: Authentication failed");
                System.out.println("  → Verify username and password in DataGrip");
            } else if (message.contains("database") && message.contains("does not exist")) {
                System.out.println("Possible Issue: Database 'roomrental' doesn't exist");
                System.out.println("  → Create database 'roomrental' in DataGrip");
            } else if (message.contains("connection") || message.contains("connect")) {
                System.out.println("Possible Issue: PostgreSQL server not running");
                System.out.println("  → Make sure PostgreSQL is running on localhost:5432");
            }
        }
    }
}
