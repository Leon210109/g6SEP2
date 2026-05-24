package Model;

import java.time.LocalTime;

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
  private String country;
  private String region;
  private String postal_code;
  private int floor;
  private String roomNumber;
  private LocalTime checkInTime;
  private LocalTime checkOutTime;
  private String listingType;

  @Override public String toString()
  {
    return "Listing{" + "id=" + id + ", numberOfRooms=" + numberOfRooms
        + ", numberOfBathrooms=" + numberOfBathrooms + ", balcony=" + balcony
        + ", surfaceArea=" + surfaceArea + ", price=" + price
        + ", lastRenovated=" + lastRenovated + ", ownerId=" + ownerId
        + ", maxNumberOfPeople=" + maxNumberOfPeople + ", isBooked=" + isBooked
        + ", street='" + street + '\'' + ", country='" + country + '\''
        + ", region='" + region + '\'' + ", postal_code='" + postal_code + '\''
        + ", floor=" + floor + ", roomNumber='" + roomNumber + '\'' + '}';
  }

  public Listing(int id,String street,String country,String region,int floor,
      String roomNumber, int numberOfRooms, int numberOfBathrooms,
      boolean balcony, float surfaceArea, int price, Date lastRenovated,
      int ownerId, int maxNumberOfPeople,String postalcode)
  {
    this.id = id;
    this.postal_code=postalcode;
    this.street=street;
    this.country= country;
    this.floor=floor;
    this.roomNumber=roomNumber;
    this.region=region;
    this.numberOfRooms = numberOfRooms;
    this.numberOfBathrooms = numberOfBathrooms;
    this.balcony = balcony;
    this.surfaceArea = surfaceArea;
    this.price = price;
    this.lastRenovated = lastRenovated;
    this.ownerId = ownerId;
    this.maxNumberOfPeople = maxNumberOfPeople;
    this.isBooked = false;
    this.checkInTime = LocalTime.of(15, 0); // Default 3:00 PM
    this.checkOutTime = LocalTime.of(11, 0); // Default 11:00 AM
    this.listingType = "SHORT_TERM";
  }
  public Listing(int numberOfRooms,int numberOfBathrooms, boolean balcony
      , float surfaceArea, int price, Date lastRenovated,
      int maxNumberOfPeople,String country,String region,
      String street,int floor, String roomNumber, String postalcode,int ownerId)
  {
    this.postal_code=postalcode;
    this.street=street;
    this.country= country;
    this.floor=floor;
    this.roomNumber=roomNumber;
    this.region=region;;
    this.numberOfRooms = numberOfRooms;
    this.numberOfBathrooms = numberOfBathrooms;
    this.balcony = balcony;
    this.surfaceArea = surfaceArea;
    this.price = price;
    this.lastRenovated = lastRenovated;
    this.ownerId = ownerId;
    this.maxNumberOfPeople = maxNumberOfPeople;
    this.isBooked = false;
    this.checkInTime = LocalTime.of(15, 0); // Default 3:00 PM
    this.checkOutTime = LocalTime.of(11, 0); // Default 11:00 AM
    this.listingType = "SHORT_TERM";
  }

  public boolean isBalcony()
  {
    return balcony;
  }

  public int getPrice()
  {
    return price;
  }


  public String getRoomNumber()
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
    return postal_code;
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



  public void setCountry(String country)
  {
    this.country = country;
  }

  public void setRegion(String region)
  {
    this.region = region;
  }


  public void setFloor(int floor)
  {
    this.floor = floor;
  }

  public void setRoomNumber(String roomNumber)
  {
    this.roomNumber = roomNumber;
  }

  public void setPostalcode(String postalcode)
  {
    this.postal_code = postalcode;
  }

  public LocalTime getCheckInTime()
  {
    return checkInTime;
  }

  public void setCheckInTime(LocalTime checkInTime)
  {
    this.checkInTime = checkInTime;
  }

  public LocalTime getCheckOutTime()
  {
    return checkOutTime;
  }

  public void setCheckOutTime(LocalTime checkOutTime)
  {
    this.checkOutTime = checkOutTime;
  }

  public String getListingType()
  {
    return listingType;
  }

  public void setListingType(String listingType)
  {
    this.listingType = listingType;
  }
}

