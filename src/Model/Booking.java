package SEP2.SEP2.src.Model;

public class Booking
{
  private int id;
  private Address address;
  private int numberOfRooms;
  private int numberOfBathrooms;
  private boolean balcony;
  private int surfaceArea;
  private int price;
  private Date lastRenovated;
  private int maxNumberOfPeople;
  private int ownerId;

  public Booking(int id, Address address, int numberOfRooms,
      int numberOfBathrooms, boolean balcony, int surfaceArea, int price,
      Date lastRenovated, int maxNumberOfPeople, int ownerId)
  {
    this.id = id;
    this.address = address;
    this.numberOfRooms = numberOfRooms;
    this.numberOfBathrooms = numberOfBathrooms;
    this.balcony = balcony;
    this.surfaceArea = surfaceArea;
    this.price = price;
    this.lastRenovated = lastRenovated;
    this.maxNumberOfPeople = maxNumberOfPeople;
    this.ownerId = ownerId;
  }

    public int getId()
    {
      return id;
    }

    public Address getAddress()
    {
      return address;
    }

    public int getNumberOfRooms()
    {
      return numberOfRooms;
    }

    public int getNumberOfBathrooms()
    {
      return numberOfBathrooms;
    }

    public boolean hasBalcony()
    {
      return balcony;
    }

    public int getSurfaceArea()
    {
      return surfaceArea;
    }

    public int getPrice()
    {
      return price;
    }

    public Date getLastRenovated()
    {
      return lastRenovated;
    }

    public int getMaxNumberOfPeople()
    {
      return maxNumberOfPeople;
    }
    public int getOwnerId()
    {
      return ownerId;
    }

}





