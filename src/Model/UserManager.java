package Model;

import Persistence.ClientDAO;
import Persistence.AdminDAO;
import Persistence.PropertyOwnerDAO;
import java.util.ArrayList;

public class UserManager
{
  private ClientDAO clientDAO;
  private AdminDAO adminDAO;
  private PropertyOwnerDAO propertyOwnerDAO;

  public UserManager(){
    clientDAO = new ClientDAO();
    adminDAO = new AdminDAO();
    propertyOwnerDAO = new PropertyOwnerDAO();
  }

  public void registerClient(Client client){
    if(findUserName(client.getUser().getUsername())){
      throw new IllegalArgumentException("UserName already exists");
    }
    clientDAO.createClient(client);
  }

  public void registerPropertyOwner(PropertyOwner owner){
    if(findUserName(owner.getLogIn().getUsername())){
      throw new IllegalArgumentException("UserName already exists");
    }
    propertyOwnerDAO.CreatePropertyOwner(owner);
  }

  public void createAdmin(Admin admin){
    if(findUserName(admin.getUser().getUsername())){
      throw new IllegalArgumentException("UserName already exists");
    }
    adminDAO.CreateAdmin(admin);
  }

  public User login(String username, String password){
    // Try to find client
    Client client = clientDAO.getClientByUsername(username);
    if(client != null && client.getUser().getPassword().equals(password)){
      return client.getUser();
    }
    
    // Try to find property owner
    PropertyOwner owner = propertyOwnerDAO.getPropertyOwnerByUsername(username);
    if(owner != null && owner.getLogIn().getPassword().equals(password)){
      return owner.getLogIn();
    }
    
    // Try to find admin
    Admin admin = adminDAO.getAdminByUsername(username);
    if(admin != null && admin.getUser().getPassword().equals(password)){
      return admin.getUser();
    }
    
    return null;
  }

  public boolean findUserName(String username){
    // Check in clients
    if(clientDAO.getClientByUsername(username) != null){
      return true;
    }
    
    // Check in property owners
    if(propertyOwnerDAO.getPropertyOwnerByUsername(username) != null){
      return true;
    }
    
    // Check in admins
    if(adminDAO.getAdminByUsername(username) != null){
      return true;
    }
    
    return false;
  }

  public Client getClientByUsername(String username){
    return clientDAO.getClientByUsername(username);
  }

  public PropertyOwner getPropertyOwnerByUsername(String username){
    return propertyOwnerDAO.getPropertyOwnerByUsername(username);
  }

  public Admin getAdminByUsername(String username){
    return adminDAO.getAdminByUsername(username);
  }

  public ArrayList<Client> getAllClients(){
    return clientDAO.getAllClients();
  }

  public ArrayList<PropertyOwner> getAllPropertyOwners(){
    return propertyOwnerDAO.getAllPropertyOwners();
  }

  public ArrayList<Admin> getAllAdmins(){
    return adminDAO.getAllAdmins();
  }
}

