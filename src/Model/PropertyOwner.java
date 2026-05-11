package SEP2.SEP2.src.Model;

public class PropertyOwner extends User
{
  private int NumberOflistings;

  public PropertyOwner(String FirstName, String LastName, String Email, String PhoneNumber, String Username, String Password, String DOB)
  {
    super(FirstName, LastName, Email, PhoneNumber, Username, Password, DOB);
    this.NumberOflistings = 0;
  }
}
