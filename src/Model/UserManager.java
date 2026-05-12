package SEP2.SEP2.src.Model;

import java.util.ArrayList;

public class UserManager
{
  private ArrayList<User> users;
  private ArrayList<Client> clients;

  public UserManager(){
    users=new ArrayList<>();
    clients= new ArrayList<>();
  }
  public void addUser(User user){
    users.add(user);
  }

  public User login(String username, String password){
    for(User user:users){
      if(user.getUsername().equals(username) && user.getPassword().equals(password)){
        return user;
      }
    }
    return null;
  }
  public void registerClient(User user,Client client){
    if(findUserName(user.getUsername())){
      throw new IllegalArgumentException("UserName already exists");
    }
    users.add(user);
    clients.add(client);
  }
  public boolean findUserName(String username){
    for(User user:users){
      if(user.getUsername().equals(username)){
        return true;
      }
    }
    return false;
  }

}

