package SEP2.SEP2.src.Model;

public class Date
{
  private int day;
  private int month;
  private int year;

  public Date(int day, int month, int year)
  {

    this.day = day;
    this.month = month;
    this.year = year;
  }

  public int getYear()
  {
    return year;
  }
  public int getMonth()
  {
    return month;
  }
  public int getDay()
  {
    return day;
  }

   public String toString()
  {
     return day+"/"+month+"/"+year;
  }
}
