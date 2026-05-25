package Model;

import Persistence.ClientDAO;
import Persistence.PropertyOwnerDAO;
import Persistence.AdminDAO;

/**
 * Service class for handling user authentication across all user types.
 * Attempts to authenticate against Client, PropertyOwner, and Admin databases.
 */
public class AuthenticationService {
    
    private final ClientDAO clientDAO;
    private final PropertyOwnerDAO propertyOwnerDAO;
    private final AdminDAO adminDAO;
    
    public AuthenticationService() {
        this.clientDAO = new ClientDAO();
        this.propertyOwnerDAO = new PropertyOwnerDAO();
        this.adminDAO = new AdminDAO();
    }
    
    /**
     * Authenticates a user and returns their type if successful.
     * 
     * @param username The username to authenticate
     * @param password The password to authenticate
     * @return An AuthenticationResult containing the user type and user object
     */
    public AuthenticationResult authenticate(String username, String password) {
        if (username == null || username.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            return new AuthenticationResult(false, null, null, "Username and password are required");
        }
        
        try {
            // Try Client first
            Client client = clientDAO.getClientByUsername(username);
            if (client != null && client.getPassword().equals(password)) {
                return new AuthenticationResult(true, "Client", client, null);
            }
            
            // Try Property Owner
            PropertyOwner owner = propertyOwnerDAO.getPropertyOwnerByUsername(username);
            if (owner != null && owner.getPassword().equals(password)) {
                return new AuthenticationResult(true, "Property Owner", owner, null);
            }
            
            // Try Admin
            Admin admin = adminDAO.getAdminByUsername(username);
            if (admin != null && admin.getPassword().equals(password)) {
                return new AuthenticationResult(true, "Admin", admin, null);
            }
            
            // No match found
            return new AuthenticationResult(false, null, null, "Invalid username or password");
            
        } catch (Exception e) {
            // Database connection or other error
            return new AuthenticationResult(false, null, null, 
                "Unable to connect to database: " + e.getMessage());
        }
    }
    
    /**
     * Result class to encapsulate authentication outcome
     */
    public static class AuthenticationResult {
        private final boolean success;
        private final String userType;
        private final Object user;
        private final String errorMessage;
        
        public AuthenticationResult(boolean success, String userType, Object user, String errorMessage) {
            this.success = success;
            this.userType = userType;
            this.user = user;
            this.errorMessage = errorMessage;
        }
        
        public boolean isSuccess() {
            return success;
        }
        
        public String getUserType() {
            return userType;
        }
        
        public Object getUser() {
            return user;
        }
        
        public String getErrorMessage() {
            return errorMessage;
        }
    }
}
