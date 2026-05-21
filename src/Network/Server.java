package Network;

import Model.ModelManager;
import Model.RentalModel;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{
  private ServerSocket serverSocket;
  private RentalModel model;

  public Server()
  {
    this.model = new ModelManager();
  }
  public void startServer()
  {
    try{
      serverSocket= new ServerSocket(5432);
      System.out.println("Server started");
      while(true)
      {
        Socket socket= serverSocket.accept();
        System.out.println("Client connected");

        ClientHandler clientHandler= new ClientHandler(socket, model);
        Thread thread = new Thread(clientHandler);
        thread.start();
      }
    }
    catch(IOException e){
      e.printStackTrace();
    }
  }
}
