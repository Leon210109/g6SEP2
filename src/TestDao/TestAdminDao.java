package TestDao;

import Model.*;
import Persistence.AdminDAO;

import java.util.ArrayList;

public class TestAdminDao
{
   public static void main(String[] args)
  {
    User user= new User("Micheal","Brownie");
    String username= user.getUsername();
    String password= user.getPassword();
    String adminName= "Micheal12";
    Admin admin= new Admin(adminName,username,password);

    // dao
    AdminDAO adminDAO= new AdminDAO();
    adminDAO.CreateAdmin(admin);
    ArrayList<Admin> admins= adminDAO.getAllAdmins();
    for(Admin a: admins){
      System.out.println(a);
    }


  }
}
