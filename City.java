package Model;

public class City
{
  private String name;
  private String postalCode;

  public City(String name, String postalCode) {
    this.name = name;
    this.postalCode = postalCode;
  }

  public String getPostalCode()
  {
    return postalCode;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public void setPostalCode(String postalCode)
  {
    this.postalCode = postalCode;
  }
  public String toString()
  {
    return "Name: " + name + "\n Postal code" + postalCode;
  }
}
