package TestDao;

import Model.*;
import Persistence.AdminDAO;
import Persistence.ClientDAO;
import Persistence.OwnerApplicationDAO;

import java.util.ArrayList;

public class TestOwnerApplicationDao
{
   static void main(String[] args)
  {
    ClientDAO clientDAO= new ClientDAO();

    Client client=  clientDAO.getClientById(1);
    AdminDAO adminDAO= new AdminDAO();
    Admin admin= adminDAO.getAdminById(1);
    Date sumbissionDate= new Date(21,05,2026);
    OwnerApplication ownerApplication= new OwnerApplication(client.getID(),
        admin.getID(),sumbissionDate,"sending","Horsens","CVC133");

    OwnerApplicationDAO ownerApplicationDAO=new OwnerApplicationDAO();
    ownerApplicationDAO.createApplication(ownerApplication);
    ArrayList<OwnerApplication> ownerApplications=ownerApplicationDAO.getAllApplications();
    for(OwnerApplication a: ownerApplications){
      System.out.println(a);
    }
  }

}
