package Persistence;

import Model.Client;
import Model.Date;
import Model.User;

import java.util.ArrayList;

public class TestDatabase
{
  public static void main(String[] args)
  {
    ClientDAO clientDAO= new ClientDAO();

    User user= new User("testuser","qwerty");
    String username= user.getUsername();
    String password=user.getPassword();
    Date dob= new Date(1,1,2000);
    Client client= new Client("Leon","de Kuijper","abc@gmail.com","3623072500",username,password,dob,"male","danish");
    clientDAO.createClient(client);

    ArrayList<Client> clients= clientDAO.getAllClients();
    for(Client c: clients){
      System.out.println(c);
    }
  }
}
