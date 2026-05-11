package Model;

public class Booking
{
  private int id;
  private int numberOfRooms;
  private int numberOfBathrooms;
  private boolean balcony;
  private int surfaceArea;
  private int price;
  private Date lastRenovated;
  private int maxNumberOfPeople;
  private int ownerId;

  public Booking(int id, int numberOfRooms,
      int numberOfBathrooms, boolean balcony, int surfaceArea, int price,
      Date lastRenovated, int maxNumberOfPeople, int ownerId)
  {
    this.id = id;
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

  public void setId(int id)
  {
    this.id = id;
  }

  public void setBalcony(boolean balcony)
  {
    this.balcony = balcony;
  }

  public void setNumberOfBathrooms(int numberOfBathrooms)
  {
    this.numberOfBathrooms = numberOfBathrooms;
  }

  public boolean isBalcony()
  {
    return balcony;
  }

  public void setLastRenovated(Date lastRenovated)
  {
    this.lastRenovated = lastRenovated;
  }

  public void setMaxNumberOfPeople(int maxNumberOfPeople)
  {
    this.maxNumberOfPeople = maxNumberOfPeople;
  }

  public void setNumberOfRooms(int numberOfRooms)
  {
    this.numberOfRooms = numberOfRooms;
  }

  public void setOwnerId(int ownerId)
  {
    this.ownerId = ownerId;
  }

  public void setPrice(int price)
  {
    this.price = price;
  }

  public void setSurfaceArea(int surfaceArea)
  {
    this.surfaceArea = surfaceArea;
  }
  public String toString() {
return("id"+ id+ "number of rooms"+ numberOfRooms+ "Balcony"+ balcony + "surface Area "+ surfaceArea + "price "+ price + "Last renovated "+ lastRenovated+ "Max number of people "+ maxNumberOfPeople + "owner Id "+ ownerId);  }
}





