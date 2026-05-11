package Model;

public class City
{
  private String name;
  private int postalCode;

  public City(String name, int postalCode) {
    this.name = name;
    this.postalCode = postalCode;
  }

  public int getPostalCode()
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

  public void setPostalCode(int postalCode)
  {
    this.postalCode = postalCode;
  }
  public String toString()
  {
    return "Name: " + name + "\n Postal code" + postalCode;
  }
}
