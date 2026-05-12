package Model;

import java.time.LocalTime;

public class Booking
{
  private int Id;
  private int clientId;
  private int listingId;
  private Date startDate;
  private Date endDate;
  private int number_of_people;
  private LocalTime check_in_time;
  private LocalTime check_out_time;

  public Booking(int clientId,int listingId,Date startDate, Date endDate, int number_of_people,
      LocalTime check_in_time, LocalTime check_out_time)
  {
    this.clientId=clientId;
    this.listingId=listingId;
    this.startDate = startDate;
    this.endDate = endDate;
    this.number_of_people = number_of_people;
    this.check_in_time = check_in_time;
    this.check_out_time = check_out_time;
  }

  public int getId()
  {
    return Id;
  }
  public void setStartDate(Date startDate)
  {
    this.startDate = startDate;
  }

  public void setEndDate(Date endDate)
  {
    this.endDate = endDate;
  }

  public void setNumber_of_people(int number_of_people)
  {
    this.number_of_people = number_of_people;
  }

  public void setCheck_in_time(LocalTime check_in_time)
  {
    this.check_in_time = check_in_time;
  }

  public void setCheck_out_time(LocalTime check_out_time)
  {
    this.check_out_time = check_out_time;
  }

  public Date getStartDate()
  {
    return startDate;
  }

  public Date getEndDate()
  {
    return endDate;
  }

  public int getNumber_of_people()
  {
    return number_of_people;
  }

  public LocalTime getCheck_in_time()
  {
    return check_in_time;
  }

  public LocalTime getCheck_out_time()
  {
    return check_out_time;
  }

  public int getClientId()
  {
    return clientId;
  }

  public int getListingId()
  {
    return listingId;
  }

  @Override public String toString()
  {
    return "Booking{" + "Id=" + Id + ", clientId=" + clientId + ", listingId="
        + listingId + ", startDate=" + startDate + ", endDate=" + endDate
        + ", number_of_people=" + number_of_people + ", check_in_time="
        + check_in_time + ", check_out_time=" + check_out_time + '}';
  }
}
