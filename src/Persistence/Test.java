package SEP2.SEP2.src.Persistence;

import java.sql.Connection;

public class Test
{
  public static void main(String[] args)
  {
    try
    {
      Connection connection =
          DatabaseConnection.getConnection();

      System.out.println(
          "Connected successfully!");

      connection.close();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }
}
