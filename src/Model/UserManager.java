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
  public void registerClient(String userName,String password,int id, String firstname,String lastname,String gender,
      String nationality,String homeaddress,String email,int phonenumber,String dob){
    if(findUserName(userName)){
      throw new IllegalArgumentException("UserName already exists");
    }
    User user= new User(userName,password);
    Client client= new Client(
        id,
        firstname,
        lastname,
        gender,
        nationality,
        homeaddress,
        email,
        phonenumber,
        dob,
        user
    );
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
