package TestDao;

import Model.AuthenticationService;
import Model.AuthenticationService.AuthenticationResult;
import Persistence.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Simple test to verify database connectivity and authentication service.
 * Run this class to check if your database is accessible.
 */
public class TestAuthentication {
    
    public static void main(String[] args) {
        System.out.println("=== Database Connection Test ===\n");
        
        // Test 1: Database connectivity
        testDatabaseConnection();
        
        // Test 2: Authentication service
        testAuthentication();
    }
    
    private static void testDatabaseConnection() {
        System.out.println("Testing database connection...");
        try {
            Connection connection = DatabaseConnection.getConnection();
            if (connection != null && !connection.isClosed()) {
                System.out.println("✓ Database connection successful!");
                System.out.println("  Connected to: " + connection.getMetaData().getURL());
                connection.close();
            } else {
                System.out.println("✗ Failed to establish database connection");
            }
        } catch (SQLException e) {
            System.out.println("✗ Database connection failed:");
            System.out.println("  Error: " + e.getMessage());
            System.out.println("\nPossible issues:");
            System.out.println("  1. PostgreSQL server is not running");
            System.out.println("  2. Database 'roomrental' does not exist");
            System.out.println("  3. Username/password incorrect");
            System.out.println("  4. PostgreSQL is not listening on localhost:5432");
        }
        System.out.println();
    }
    
    private static void testAuthentication() {
        System.out.println("Testing authentication service...");
        
        AuthenticationService authService = new AuthenticationService();
        
        // Test with sample credentials (you'll need to adjust these)
        String[][] testCredentials = {
            {"testclient", "password123", "Client"},
            {"testowner", "password123", "Property Owner"},
            {"testadmin", "admin123", "Admin"},
            {"invalid", "wrong", null}  // Should fail
        };
        
        for (String[] creds : testCredentials) {
            String username = creds[0];
            String password = creds[1];
            String expectedType = creds[2];
            
            try {
                System.out.println("\nAttempting login: " + username);
                AuthenticationResult result = authService.authenticate(username, password);
                
                if (result.isSuccess()) {
                    System.out.println("✓ Login successful!");
                    System.out.println("  User type: " + result.getUserType());
                    System.out.println("  User object: " + result.getUser());
                } else {
                    System.out.println("✗ Login failed: " + result.getErrorMessage());
                }
            } catch (Exception e) {
                System.out.println("✗ Error during authentication: " + e.getMessage());
            }
        }
    }
}
