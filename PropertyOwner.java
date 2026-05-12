package Model;
public class PropertyOwner
{
  private String FirstName;
  private String LastName;
  private String Email;
  private String PhoneNumber;
  private Date DOB;
  private String Gender;
  private String Nationality;
  private int ID;
  private User logIn;
  private int NumberOflistings;

  public PropertyOwner(String FirstName, String LastName, String Email, String PhoneNumber, Date DOB, User logIn,String gender,String nationality, int ID)
  {
    this.ID = ID;
    this.FirstName=FirstName;
    this.LastName=LastName;
    this.Email=Email;
    this.PhoneNumber=PhoneNumber;
    this.logIn=logIn;
    this.DOB=DOB;
    this.Gender=gender;
    this.Nationality=nationality;
    this.NumberOflistings = 0;
  }

  public String getFirstName()
  {
    return FirstName;
  }

  public String getLastName()
  {
    return LastName;
  }

  public String getPhoneNumber()
  {
    return PhoneNumber;
  }

  public String getGender()
  {
    return Gender;
  }

  public Date getDOB()
  {
    return DOB;
  }

  public int getID()
  {
    return ID;
  }

  public String getEmail()
  {
    return Email;
  }

  public User getLogIn()
  {
    return logIn;
  }

  public int getNumberOflistings()
  {
    return NumberOflistings;
  }

  public String getNationality()
  {
    return Nationality;
  }

  public void setFirstName(String firstName)
  {
    FirstName = firstName;
  }

  public void setLastName(String lastName)
  {
    LastName = lastName;
  }

  public void setEmail(String email)
  {
    Email = email;
  }

  public void setPhoneNumber(String phoneNumber)
  {
    PhoneNumber = phoneNumber;
  }

  public void setDOB(Date DOB)
  {
    this.DOB = DOB;
  }

  public void setGender(String gender)
  {
    Gender = gender;
  }

  public void setNationality(String nationality)
  {
    Nationality = nationality;
  }

  public void setID(int ID)
  {
    this.ID = ID;
  }

  public void setLogIn(User logIn)
  {
    this.logIn = logIn;
  }

  public void setNumberOflistings(int numberOflistings)
  {
    NumberOflistings = numberOflistings;
  }
  public String toString() {
    return ("First name: "+ FirstName + "Last name: "+ LastName + "Email: "+ Email + "Phone number: " + PhoneNumber + "Date of birth: "+ DOB + "Gender: " + Gender + "Nationality: " + Nationality + "ID: " + ID + "Log in:" + logIn + "Number of listings: " + NumberOflistings);
  }
}
