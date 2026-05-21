package Model;

import Network.SocketClient;
import Shared.Request;
import Shared.RequestType;
import Shared.Response;

import java.util.ArrayList;

public class ClientModelManager
{
  private SocketClient socketClient;

  public ClientModelManager(SocketClient socketClient)
  {
    this.socketClient = socketClient;
    socketClient.connect();
  }

  public User login(
      String username,String password
  )
  {
    Request request= new Request(RequestType.Login,username,password);
    Response response= socketClient.sendRequest(request);

    if(response.isSuccess()){
      return (User) response.getObject();
    }
    throw new RuntimeException(response.getMessage());
  }
  public ArrayList<Listing> getAllListings()
  {
    Request request= new Request(RequestType.Get_All_Listings);
    Response response= socketClient.sendRequest(request);

    if(response.isSuccess()){
      return (ArrayList<Listing>) response.getObject();
    }
    throw  new RuntimeException(response.getMessage());
  }
  public void createBooking(
      Booking booking)
  {
    Request request =
        new Request(
            RequestType.Make_Booking,
            booking);

    Response response= socketClient
        .sendRequest(request);
    if(!response.isSuccess())
    {
      throw new RuntimeException(
          response.getMessage());
    }
  }

  public void removeBooking(
      int bookingId)
  {
    Request request =
        new Request(
            RequestType.Remove_Booking,
            bookingId);

    Response response= socketClient
        .sendRequest(request);
    if(!response.isSuccess())
    {
      throw new RuntimeException(
          response.getMessage());
    }
  }
  public void registerClient(
      Client client)
  {
    Request request =
        new Request(
            RequestType.Register_Client,
            client);

    Response response= socketClient
        .sendRequest(request);
    if(!response.isSuccess()){
      throw new RuntimeException(response.getMessage());
    }
  }

}
