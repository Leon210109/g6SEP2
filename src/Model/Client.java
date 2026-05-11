package SEP2.SEP2.src.Model;
import SEP2.SEP2.src.Model.User;

public class Client
{
  private String FirstName;
  private String LastName;
  private String Email;
  private int PhoneNumber;
  private String DOB;
  private String Gender;
  private String Nationality;
  private String HomeAddress;
  private int ID;
  private User logIn;

  public Client(String FirstName, String LastName,String Gender, String Nationality,String HomeAddress, String Email, int PhoneNumber,String DOB, User logIn, int ID)
  {
    this.ID = ID;
    this.FirstName = FirstName;
    this.LastName = LastName;
    this.Gender=Gender;
    this.Nationality=Nationality;
    this.HomeAddress=HomeAddress;
    this.Email = Email;
    this.PhoneNumber = PhoneNumber;
    this.DOB = DOB;
    this.logIn=logIn;
  }

  public User getLogIn()
  {
    return logIn;
  }

  public int getID()
  {
    return ID;
  }

  public String getEmail()
  {
    return Email;
  }

  public String getDOB()
  {
    return DOB;
  }

  public String getFirstName()
  {
    return FirstName;
  }

  public String getGender()
  {
    return Gender;
  }

  public String getLastName()
  {
    return LastName;
  }

  public String getHomeAddress()
  {
    return HomeAddress;
  }

  public String getNationality()
  {
    return Nationality;
  }

  public int getPhoneNumber()
  {
    return PhoneNumber;
  }
  public String toString()
  {
    return "Name: " + LastName + " " + FirstName + "\nUsername " + logIn.getUsername() + "\nPassword " + logIn.getPassword() + "\n Email " + Email + "\n Phone number " + PhoneNumber +"\nGender " + Gender + "\nDate of birth " + DOB + "\nNationality " + Nationality + "\nHome address " + HomeAddress + "\nID " + ID;
  }
}
