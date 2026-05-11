package SEP2.SEP2.src.Model;

public class Listing
{
  private int id;
  private Address address;
  private int numberOfRooms;
  private int numberOfBathrooms;
  private boolean balcony;
  private float surfaceArea;
  private  int price;
  private Date lastRenovated;
  private int ownerId;
  private int maxNumberOfPeople;
  private boolean isBooked;

  public Listing(int id, Address address, int numberOfRooms, int numberOfBathrooms, boolean balcony, float surfaceArea, int price, Date lastRenovated, int ownerId, int maxNumberOfPeople)
  {
    this.id = id;
    this.address = address;
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
}
