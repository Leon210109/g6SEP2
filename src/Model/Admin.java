package SEP2.SEP2.src.Model;
import SEP2.SEP2.src.Model.User;


public class Admin
{
  private String ID;
  private User logIn;

  public Admin(String ID, User logIn)
  {
    this.ID = ID;
    this.logIn=logIn;
  }

  public String getID()
  {
    return ID;
  }

  public User getLogIn()
  {
    return logIn;
  }

  public void setID(String ID)
  {
    this.ID = ID;
  }

  public void setLogIn(User logIn)
  {
    this.logIn = logIn;
  }
  public String toString()
  {
    return "Username " + logIn.getUsername() + "\n Password" + logIn.getPassword() + "\n ID: " + ID;
  }
}
