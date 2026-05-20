package Network;

import Shared.Request;
import Shared.RequestType;
import Shared.Response;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketClient
{
  private Socket socket;
  private Gson gson;
  private BufferedReader in;
  private PrintWriter out;

  public SocketClient(Gson gson)
  {
    this.gson = gson;
  }
  public void connect()
  {
    try
    {
      socket= new Socket("localhost",1234);
      in= new BufferedReader(new InputStreamReader(socket.getInputStream()));
      out=new PrintWriter(socket.getOutputStream(),true);

      System.out.println("Connected to server");
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  public Response sendRequest(Request request)
  {
    try
    {   // here first we send request coming from the client model manager
      String json = gson.toJson(request);
      // send it to server
      out.println(json);

      System.out.println("request has been sent to server from SocketClient");
      // wait for server response
      String responseJson = in.readLine();

      // convert json back to response object
      return gson.fromJson(responseJson, Response.class);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return new Response(false,"Communication error",null);
  }

  public void disconnect()
  {
    try
    {
      if (socket != null)
      {
        socket.close();
      }
      if (in != null)
      {
        in.close();
      }
      if (out != null)
      {
        out.close();
      }
      System.out.println("Disconnected from server");
    }
    catch(IOException e)
    {
      e.printStackTrace();
    }
  }
}
