package Persistence;

import Model.Client;
import Model.Date;
import Model.User;

import java.sql.Connection;
import java.util.ArrayList;

public class TestDatabase
{
  public static void main(String[] args)
  {
    System.out.println("=== Testing Database Operations ===\n");
    
    // First, test the connection
    System.out.println("Step 1: Testing database connection...");
    try {
      Connection conn = DatabaseConnection.getConnection();
      if (conn != null && !conn.isClosed()) {
        System.out.println("✓ Database connection successful!\n");
        conn.close();
      }
    } catch (Exception e) {
      System.out.println("✗ Database connection failed!");
      System.out.println("Error: " + e.getMessage());
      System.out.println("\nPlease run DatabaseDiagnostic.java for more details.");
      return; // Exit if connection fails
    }
    
    // Now test the DAO operations
    try {
      ClientDAO clientDAO = new ClientDAO();

      System.out.println("Step 2: Creating new client...");
      User user = new User("testuser", "qwerty");
      String username = user.getUsername();
      String password = user.getPassword();
      Date dob = new Date(1, 1, 2000);
      Client client = new Client("Leon", "de Kuijper", "abc@gmail.com", 
                                 "3623072500", username, password, dob, 
                                 "male", "danish");
      
      clientDAO.createClient(client);
      System.out.println("✓ Client created successfully!\n");

      System.out.println("Step 3: Retrieving all clients from database...");
      ArrayList<Client> clients = clientDAO.getAllClients();
      System.out.println("✓ Found " + clients.size() + " client(s)\n");
      
      System.out.println("Client Details:");
      System.out.println("─────────────────────────────────────────");
      for (Client c : clients) {
        System.out.println(c);
      }
      System.out.println("\n✓ All tests completed successfully!");
      
    } catch (Exception e) {
      System.out.println("\n✗ Database operation failed!");
      System.out.println("Error: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
