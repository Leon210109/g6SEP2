package TestDao;

import Model.Client;
import Model.Date;
import Model.PropertyOwner;
import Model.User;
import Persistence.ClientDAO;
import Persistence.PropertyOwnerDAO;
import javafx.beans.property.Property;

import java.util.ArrayList;

public class TestPropertyDao
{
 public  static void main(String[] args)
  {
    User user= new User("Alex","qwerty");
    PropertyOwnerDAO propertyOwnerDAO= new PropertyOwnerDAO();
    String username= user.getUsername();
    String password=user.getPassword();
    Date dob= new Date(1,1,2000);
    PropertyOwner owner= new PropertyOwner("Leon123","de Kuijperiui","abc@gmail.com","3623072500",username,password,dob,"male","danish");
    propertyOwnerDAO.CreatePropertyOwner(owner);

    ArrayList<PropertyOwner> owners= propertyOwnerDAO.getAllPropertyOwners();
    for(PropertyOwner c: owners){
      System.out.println(c);
    }
  }
}
