package TestDao;

import Model.AuthenticationService;
import Model.AuthenticationService.AuthenticationResult;
import Model.Client;
import Model.Date;
import Persistence.ClientDAO;
import Persistence.DatabaseConnection;

import java.sql.Connection;

/**
 * Test the complete registration and login flow
 */
public class TestRegistrationAndLogin {
    
    public static void main(String[] args) {
        System.out.println("=== REGISTRATION AND LOGIN TEST ===\n");
        
        // Step 1: Test database connection
        if (!testConnection()) {
            System.out.println("Database connection failed. Exiting test.");
            return;
        }
        
        // Step 2: Test registration (create a new client)
        String testUsername = "testuser_" + System.currentTimeMillis(); // Unique username
        testRegistration(testUsername);
        
        // Step 3: Test login with the newly created user
        testLogin(testUsername, "password123");
        
        // Step 4: Test login with existing users from sample data
        System.out.println("\n=== Testing Login with Sample Data ===");
        testLogin("testclient", "password123");
        testLogin("testowner", "password123");
        testLogin("testadmin", "admin123");
        
        System.out.println("\n✓ All tests completed!");
    }
    
    private static boolean testConnection() {
        System.out.println("Step 1: Testing database connection...");
        try {
            Connection conn = DatabaseConnection.getConnection();
            if (conn != null && !conn.isClosed()) {
                System.out.println("✓ Database connection successful!\n");
                conn.close();
                return true;
            }
            return false;
        } catch (Exception e) {
            System.out.println("✗ Connection failed: " + e.getMessage());
            return false;
        }
    }
    
    private static void testRegistration(String username) {
        System.out.println("Step 2: Testing registration...");
        try {
            ClientDAO clientDAO = new ClientDAO();
            
            // Create a new test client
            Date dob = new Date(15, 3, 1995);
            Client newClient = new Client(
                "Test",
                "User",
                "testuser@example.com",
                "+1234567890",
                username,
                "password123",
                dob,
                "Male",
                "USA"
            );
            
            clientDAO.createClient(newClient);
            System.out.println("✓ Registration successful!");
            System.out.println("  Username: " + username);
            System.out.println("  Password: password123\n");
            
        } catch (Exception e) {
            System.out.println("✗ Registration failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void testLogin(String username, String password) {
        System.out.println("\nTesting login for: " + username);
        try {
            AuthenticationService authService = new AuthenticationService();
            AuthenticationResult result = authService.authenticate(username, password);
            
            if (result.isSuccess()) {
                System.out.println("✓ Login successful!");
                System.out.println("  User Type: " + result.getUserType());
                System.out.println("  User: " + result.getUser());
            } else {
                System.out.println("✗ Login failed: " + result.getErrorMessage());
            }
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }
}
