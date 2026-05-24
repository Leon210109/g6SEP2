package Model;

public class Listing
{
  private int id;
  private int numberOfRooms;
  private int numberOfBathrooms;
  private boolean balcony;
  private float surfaceArea;
  private  int price;
  private Date lastRenovated;
  private int ownerId;
  private int maxNumberOfPeople;
  private boolean isBooked;
  private String street;
  private String postalcode;
  private String country;
  private String region;
  private int StreetNumber;
  private int floor;
  private int roomNumber;

  public Listing(int id,String street,String country,String region,int StreetNumber, int floor, int roomNumber, City city, int numberOfRooms, int numberOfBathrooms, boolean balcony, float surfaceArea, int price, Date lastRenovated, int ownerId, int maxNumberOfPeople)
  {
    this.id = id;
    this.postalcode=postalcode;
    this.street=street;
    this.country= country;
    this.floor=floor;
    this.roomNumber=roomNumber;
    this.region=region;
    this.StreetNumber=StreetNumber;
    this.numberOfRooms = numberOfRooms;
    this.numberOfBathrooms = numberOfBathrooms;
    this.balcony = balcony;
    this.surfaceArea = surfaceArea;
    this.price = price;
    this.lastRenovated = lastRenovated;
    this.ownerId = ownerId;
    this.maxNumberOfPeople = maxNumberOfPeople;
    this.isBooked = false;
  }
  public Listing(int numberOfRooms,int numberOfBathrooms, boolean balcony, float surfaceArea, int price, Date lastRenovated,int maxNumberOfPeople,String country,String region, String street,int floor, int roomNumber, String postalcode,int ownerId)
  {
    this.postalcode=postalcode;
    this.street=street;
    this.country= country;
    this.floor=floor;
    this.roomNumber=roomNumber;
    this.region=region;
    this.StreetNumber=StreetNumber;
    this.numberOfRooms = numberOfRooms;
    this.numberOfBathrooms = numberOfBathrooms;
    this.balcony = balcony;
    this.surfaceArea = surfaceArea;
    this.price = price;
    this.lastRenovated = lastRenovated;
    this.ownerId = ownerId;
    this.maxNumberOfPeople = maxNumberOfPeople;
    this.isBooked = false;
  }

  public boolean isBalcony()
  {
    return balcony;
  }

  public int getPrice()
  {
    return price;
  }

  public int getStreetNumber()
  {
    return StreetNumber;
  }

  public int getRoomNumber()
  {
    return roomNumber;
  }

  public int getId()
  {
    return id;
  }

  public Date getLastRenovated()
  {
    return lastRenovated;
  }

  public boolean isBooked()
  {
    return isBooked;
  }


  public float getSurfaceArea()
  {
    return surfaceArea;
  }

  public String getCountry()
  {
    return country;
  }

  public int getFloor()
  {
    return floor;
  }

  public int getMaxNumberOfPeople()
  {
    return maxNumberOfPeople;
  }

  public int getNumberOfBathrooms()
  {
    return numberOfBathrooms;
  }

  public String getRegion()
  {
    return region;
  }

  public void setBalcony(boolean balcony)
  {
    this.balcony = balcony;
  }

  public String getStreet()
  {
    return street;
  }

  public String getPostalcode()
  {
    return postalcode;
  }

  public int getNumberOfRooms()
  {
    return numberOfRooms;
  }

  public int getOwnerId()
  {
    return ownerId;
  }

  public void setId(int id)
  {
    this.id = id;
  }

  public void setNumberOfRooms(int numberOfRooms)
  {
    this.numberOfRooms = numberOfRooms;
  }

  public void setNumberOfBathrooms(int numberOfBathrooms)
  {
    this.numberOfBathrooms = numberOfBathrooms;
  }

  public void setSurfaceArea(float surfaceArea)
  {
    this.surfaceArea = surfaceArea;
  }

  public void setPrice(int price)
  {
    this.price = price;
  }

  public void setLastRenovated(Date lastRenovated)
  {
    this.lastRenovated = lastRenovated;
  }

  public void setOwnerId(int ownerId)
  {
    this.ownerId = ownerId;
  }

  public void setMaxNumberOfPeople(int maxNumberOfPeople)
  {
    this.maxNumberOfPeople = maxNumberOfPeople;
  }

  public void setBooked(boolean booked)
  {
    isBooked = booked;
  }

  public void setStreet(String street)
  {
    this.street = street;
  }

  public void setCity(City city)
  {
    this.city = city;
  }

  public void setCountry(String country)
  {
    this.country = country;
  }

  public void setRegion(String region)
  {
    this.region = region;
  }

  public void setStreetNumber(int streetNumber)
  {
    StreetNumber = streetNumber;
  }

  public void setFloor(int floor)
  {
    this.floor = floor;
  }

  public void setRoomNumber(int roomNumber)
  {
    this.roomNumber = roomNumber;
  }
  public String toString() {
    return "ID: " + id + "Number of Rooms: " + numberOfRooms  + "Number of Bathrooms: " + numberOfBathrooms + "Balcony: " + balcony + "Surface Area: " + surfaceArea + "Price: "+ price + "Last Renovated: " + lastRenovated + "Owner Id: " + ownerId + "Max number of people: " + maxNumberOfPeople + "Is booked : " + isBooked  + "Street: " + street + "City: "+ city  + country + "Region: " + region + "Street Number "+ StreetNumber + "Floor: "  + floor + "Room Number: "+ roomNumber;
  }
}

