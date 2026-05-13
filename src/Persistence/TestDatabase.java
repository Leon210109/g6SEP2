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
    Date dob= new Date(1,1,2000);
    Client client= new Client("John","Doe","male","danish","Horsens","abc@gmail.com",36230725,dob,user,1243);
    clientDAO.createClient(client);

    ArrayList<Client> clients= clientDAO.getAllClients();
    for(Client c: clients){
      System.out.println(c);
    }
  }
}
