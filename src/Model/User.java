package Model;

public class User
{

  private String Username;
  private String Password;

  public User(String Username, String Password)
  {
    this.Username = Username;
    this.Password = Password;
  }

  public String getPassword()
  {
    return Password;
  }

  public String getUsername()
  {
    return Username;
  }

  public void setUsername(String username)
  {
    Username = username;
  }

  public void setPassword(String password)
  {
    Password = password;
  }
  public String toString() {
    return ("Username: "+ Username + "Password: " + Password);
  }
}
