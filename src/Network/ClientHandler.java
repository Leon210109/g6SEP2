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

public class ClientHandler
    implements Runnable,
    PropertyChangeListener
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
    catch(IOException e)
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
    catch(Exception e)
    {
      System.out.println(
          "Client disconnected");
    }
    finally
    {
      closeConnection();
    }
  }

  // ───────────────── PROCESS REQUEST ─────────────────

  private void processRequest(
      Request request)
  {
    try
    {
      switch(request.getRequestType())
      {
        // ───────── CLIENT ─────────

        case Login:
          handleLogin(request);
          break;

        case Register_Client:
          handleRegisterClient(request);
          break;

        // ───────── LISTINGS ─────────

        case Add_Listing:
          handleAddListing(request);
          break;

        case Remove_Listing:
          handleRemoveListing(request);
          break;

        case Update_Listing:
          handleUpdateListing(request);
          break;

        case Get_All_Listings:
          handleGetAllListings();
          break;

        // ───────── BOOKINGS ─────────

        case Make_Booking:
          handleAddBooking(request);
          break;

        case Remove_Booking:
          handleRemoveBooking(request);
          break;

        case Update_Booking:
          handleUpdateBooking(request);
          break;

        case Get_All_Booking:
          handleGetAllBookings();
          break;

        // ───────── OWNER APPLICATIONS ─────────

        case Add_Owner_Application:
          handleAddOwnerApplication(request);
          break;

        case Remove_Owner_Application:
          handleRemoveOwnerApplication(request);
          break;

        case Update_Owner_Application:
          handleUpdateOwnerApplication(request);
          break;

        case Get_All_Owner_Applications:
          handleGetAllOwnerApplications();
          break;

        default:
          sendResponse(
              new Response(
                  false,
                  "ERROR",
                  "Unknown request"));
      }
    }
    catch(Exception e)
    {
      sendResponse(
          new Response(
              false,
              "ERROR",
              e.getMessage()));
    }
  }

  // ───────────────── CLIENT METHODS ─────────────────

  private void handleLogin(
      Request request)
  {
    String username =
        (String) request.getArgs()[0];

    String password =
        (String) request.getArgs()[1];

    model.login(
        username,
        password);
  }

  private void handleRegisterClient(
      Request request)
  {
    Client client =
        gson.fromJson(
            gson.toJson(
                request.getArgs()[0]),
            Client.class);

    model.registerClient(client);
  }

  // ───────────────── LISTING METHODS ─────────────────

  private void handleAddListing(
      Request request)
  {
    Listing listing =
        gson.fromJson(
            gson.toJson(
                request.getArgs()[0]),
            Listing.class);

    model.addListing(listing);
  }

  private void handleRemoveListing(
      Request request)
  {
    int listingId =
        ((Double) request.getArgs()[0])
            .intValue();

    model.removeListing(listingId);
  }

  private void handleUpdateListing(
      Request request)
  {
    Listing listing =
        gson.fromJson(
            gson.toJson(
                request.getArgs()[0]),
            Listing.class);

    model.updateListing(listing);
  }

  private void handleGetAllListings()
  {
    model.getAllListings();
  }

  // ───────────────── BOOKING METHODS ─────────────────

  private void handleAddBooking(
      Request request)
  {
    Booking booking =
        gson.fromJson(
            gson.toJson(
                request.getArgs()[0]),
            Booking.class);

    model.addBooking(booking);
  }

  private void handleRemoveBooking(
      Request request)
  {
    int bookingId =
        ((Double) request.getArgs()[0])
            .intValue();

    model.removeBooking(bookingId);
  }

  private void handleUpdateBooking(
      Request request)
  {
    Booking booking =
        gson.fromJson(
            gson.toJson(
                request.getArgs()[0]),
            Booking.class);

    model.updateBooking(booking);
  }

  private void handleGetAllBookings()
  {
    model.getAllBookings();
  }

  // ───────────────── OWNER APPLICATION METHODS ─────────────────

  private void handleAddOwnerApplication(
      Request request)
  {
    OwnerApplication ownerApplication =
        gson.fromJson(
            gson.toJson(
                request.getArgs()[0]),
            OwnerApplication.class);

    model.addOwnerApplication(
        ownerApplication);
  }

  private void handleRemoveOwnerApplication(
      Request request)
  {
    int applicationId =
        ((Double) request.getArgs()[0])
            .intValue();

    model.removeOwnerApplication(
        applicationId);
  }

  private void handleUpdateOwnerApplication(
      Request request)
  {
    OwnerApplication ownerApplication =
        gson.fromJson(
            gson.toJson(
                request.getArgs()[0]),
            OwnerApplication.class);

    model.updateOwnerApplication(
        ownerApplication);
  }

  private void handleGetAllOwnerApplications()
  {
    model.getAllOwnerApplications();
  }

  // ───────────────── MODEL EVENTS ─────────────────

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
    catch(IOException e)
    {
      e.printStackTrace();
    }
  }
}