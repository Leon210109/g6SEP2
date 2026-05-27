package Client;

import Shared.Request;
import Shared.RequestType;
import Shared.Response;
import Util.LocalTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalTime;

public class SocketClient implements Runnable {
  private Socket socket;
  private Gson gson;
  private BufferedReader in;
  private PrintWriter out;
  private PropertyChangeSupport support;
  private boolean running;

  public SocketClient(Gson gson) {
    this.gson = new GsonBuilder().registerTypeAdapter(LocalTime.class
    ,new LocalTimeAdapter()).create();
    support = new PropertyChangeSupport(this);
  }

  public void connect() {
    try {
      socket = new Socket("localhost", 1234);
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      out = new PrintWriter(socket.getOutputStream(), true);

      running = true;
      new Thread(this).start();
      System.out.println("Connect ed to server");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void sendRequest(Request request) {
    // here first we send request coming from the client model manager
    String json = gson.toJson(request);
    // send it to server
    out.println(json);

    System.out.println("request has been sent to server from SocketClient");
  }

  @Override
  public void run() {
    try {
      String message;
      while (running && (message = in.readLine()) != null) {
        System.out.println("Received json: " + message);

        Response response = gson.fromJson(message, Response.class);
        support.firePropertyChange(response.getMessage(), null, response.getObject());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void addPropertyChangeListener(
      PropertyChangeListener listener) {
    support.addPropertyChangeListener(
        listener);
  }

  public void removePropertyChangeListener(
      PropertyChangeListener listener) {
    support.removePropertyChangeListener(
        listener);
  }

  public void disconnect() {
    running = false;
    try {
      if (socket != null) {
        socket.close();
      }
      if (in != null) {
        in.close();
      }
      if (out != null) {
        out.close();
      }
      System.out.println("Disconnected from server");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
