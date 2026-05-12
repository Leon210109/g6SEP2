package Model;

public class Address
{
  private String city;
  private String street;
  private String postalCode;
  private String country;
  private String region;
  private int StreetNumber;
  private int floor;
  private int roomNumber;


  public Address( String city, String street, String postalCode, String country, String region, int StreetNumber, int floor, int roomNumber)
  {
    this.city = city;
    this.street = street;
    this.postalCode = postalCode;
    this.country = country;
    this.region = region;
    this.StreetNumber = StreetNumber;
    this.floor = floor;
    this.roomNumber = roomNumber;
  }
  public String getCity()
  {
    return city;
  }

  public String getCountry()
  {
    return country;
  }

  public String getPostalCode()
  {
    return postalCode;
  }
  public String getStreet(){
    return street;
  }
  public String getRegion(){
    return region;
  }
  public int getStreetNumber(){
    return StreetNumber;
  }
  public int getFloor()
  {
    return floor;
  }
  public int getRoomNumber()
  {
    return roomNumber;
  }

}
