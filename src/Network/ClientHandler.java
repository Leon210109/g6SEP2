package Network;

import Model.*;

import Shared.Request;
import Shared.RequestType;
import Shared.Response;

import com.google.gson.Gson;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.Socket;

import java.util.ArrayList;

public class ClientHandler
    implements Runnable, PropertyChangeListener
{
  private Socket socket;

  private RentalModel model;

  private BufferedReader in;
  private PrintWriter out;

  private Gson gson;

  private boolean running;

  public ClientHandler(
      Socket socket,
      RentalModel model)
  {
    this.socket = socket;
    this.model = model;

    gson = new Gson();

    running = true;

    model.addPropertyChangeListener(this);

    try
    {
      in = new BufferedReader(
          new InputStreamReader(
              socket.getInputStream()));

      out = new PrintWriter(
          socket.getOutputStream(),
          true);

      System.out.println(
          "Client connected");
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  @Override
  public void run()
  {
    try
    {
      String message;

      while(running &&
          (message = in.readLine()) != null)
      {
        System.out.println(
            "Received JSON: "
                + message);

        Request request =
            gson.fromJson(
                message,
                Request.class);

        processRequest(request);
      }
    }
    catch (Exception e)
    {
      System.out.println(
          "Client disconnected");
    }
    finally
    {
      closeConnection();
    }
  }

  private void processRequest(
      Request request)
  {
    try
    {
      switch(request.getRequestType())
      {
        case Login:
          handleLogin(request);
          break;

        case Register_Client:
          handleRegisterClient(request);
          break;

        case Get_All_Listings:
          handleGetAllListings();
          break;

        case Make_Booking:
          handleMakeBooking(request);
          break;

        case Remove_Booking:
          handleRemoveBooking(request);
          break;

        default:
          sendResponse(
              new Response(
                  false,
                  "Unknown request",
                  null));
      }
    }
    catch (Exception e)
    {
      sendResponse(
          new Response(
              false,
              e.getMessage(),
              null));
    }
  }

  // ───────────────── LOGIN ─────────────────

  private void handleLogin(
      Request request)
  {
    String username =
        (String) request.getArgs()[0];

    String password =
        (String) request.getArgs()[1];

    User user =
        model.login(
            username,
            password);

    sendResponse(
        new Response(
            true,
            "Login successful",
            user));
  }

  // ───────────────── REGISTER CLIENT ─────────────────

  private void handleRegisterClient(
      Request request)
  {
    Client client =
        (Client) request.getArgs()[0];

    model.registerClient(client);

    sendResponse(
        new Response(
            true,
            "Client registered",
            null));
  }

  // ───────────────── GET LISTINGS ─────────────────

  private void handleGetAllListings()
  {
    ArrayList<Listing> listings =
        model.getAllListings();

    sendResponse(
        new Response(
            true,
            "Listings loaded",
            listings));
  }

  // ───────────────── BOOKING ─────────────────

  private void handleMakeBooking(
      Request request)
  {
    Booking booking =
        (Booking) request.getArgs()[0];

    model.addBooking(booking);

    sendResponse(
        new Response(
            true,
            "Booking created",
            null));
  }

  private void handleRemoveBooking(
      Request request)
  {
    int bookingId =
        ((Double) request.getArgs()[0])
            .intValue();

    model.removeBooking(bookingId);

    sendResponse(
        new Response(
            true,
            "Booking removed",
            null));
  }

  // ───────────────── SEND RESPONSE ─────────────────

  private void sendResponse(
      Response response)
  {
    String json =
        gson.toJson(response);

    out.println(json);

    System.out.println(
        "Sent JSON: "
            + json);
  }

  // ───────────────── REALTIME EVENTS ─────────────────

  @Override
  public void propertyChange(
      PropertyChangeEvent evt)
  {
    Response response =
        new Response(
            true,
            evt.getPropertyName(),
            evt.getNewValue());

    sendResponse(response);
  }

  // ───────────────── CLOSE CONNECTION ─────────────────

  private void closeConnection()
  {
    running = false;

    model.removePropertyChangeListener(
        this);

    try
    {
      if(socket != null)
      {
        socket.close();
      }

      if(in != null)
      {
        in.close();
      }

      if(out != null)
      {
        out.close();
      }

      System.out.println(
          "Connection closed");
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}