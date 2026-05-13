package Model;


public class Client
{
  private String FirstName;
  private String LastName;
  private String Email;
  private String PhoneNumber;
  private Date  DOB;
  private String Gender;
  private String Nationality;
  private int ID;
  private String username;
  private String password;



  public Client(int ID, String firstName, String lastName, String email, String phoneNumber,
       String username,String password, Date DOB, String gender, String nationality)
  {
    this.ID = ID;
    this.FirstName = FirstName;
    this.LastName = LastName;
    this.Email = Email;
    this.PhoneNumber = PhoneNumber;
    this.username=username;
    this.password=password;
    this.DOB = DOB;
    this.Gender = Gender;
    this.Nationality = Nationality;
  }

  public Client(String firstName, String lastName, String email,
      String phoneNumber, String username, String password,Date DOB,
      String gender, String nationality)
  {
    FirstName = firstName;
    LastName = lastName;
    Email = email;
    PhoneNumber = phoneNumber;
    this.username = username;
    this.password = password;
    this.DOB = DOB;
    Gender = gender;
    Nationality = nationality;
  }

  public String getUsername()
  {
    return username;
  }

  public String getPassword()
  {
    return password;
  }


  public int getID()
  {
    return ID;
  }

  public String getEmail()
  {
    return Email;
  }

  public Date getDOB()
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

  public String getNationality()
  {
    return Nationality;
  }

  public String getPhoneNumber()
  {
    return PhoneNumber;
  }

  public User getUser()
  {
    return new User(getUsername(),getPassword());

  }

  @Override public String toString()
  {
    return "Client{" + "FirstName='" + FirstName + '\'' + ", LastName='"
        + LastName + '\'' + ", Email='" + Email + '\'' + ", PhoneNumber='"
        + PhoneNumber + '\'' + ", DOB=" + DOB + ", Gender='" + Gender + '\''
        + ", Nationality='" + Nationality + '\'' + ", ID=" + ID + ", username='"
        + username + '\'' + ", password='" + password + '\'' + '}';
  }
}
