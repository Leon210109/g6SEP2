package SEP2.SEP2.src.Model;
import SEP2.SEP2.src.Model.User;


public class Admin
{
  private int ID;
  private User logIn;
  private String adminName;

  public Admin(int ID, User logIn, String adminName
  )
  {
    this.ID = ID;
    this.logIn=logIn;
    this.adminName = adminName;
  }

  public int getID()
  {
    return ID;
  }

  public User getUser()
  {
    return logIn;
  }

  public void setID(int ID)
  {
    this.ID = ID;
  }

  public void setLogIn(User logIn)
  {
    this.logIn = logIn;
  }
  public String getAdminName()
  {
    return adminName;
  }
  public void setAdminName(String adminName)
  {
    this.adminName=adminName;
  }
  public String toString()
  {
    return "Username " + logIn.getUsername() + "\n Password" + logIn.getPassword() + "\n ID: " + ID + adminName  + "AdminName";
  }
}
