package Client;

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

public class ClientHandler implements Runnable, PropertyChangeListener {
  private final Socket socket;
  private final RentalModel model;
  private BufferedReader in;
  private PrintWriter out;
  private final Gson gson;
  private boolean running;

  public ClientHandler(Socket socket, RentalModel model) {
    this.socket = socket;
    this.model = model;
    this.gson = new Gson();
    this.running = true;
    model.addPropertyChangeListener(this);
    try {
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      out = new PrintWriter(socket.getOutputStream(), true);
      System.out.println("Client connected");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void run() {
    try {
      String message;
      while (running && (message = in.readLine()) != null) {
        System.out.println("Received JSON: " + message);
        Request request = gson.fromJson(message, Request.class);
        processRequest(request);
      }
    } catch (Exception e) {
      System.out.println("Client disconnected: " + e.getMessage());
    } finally {
      closeConnection();
    }
  }

  // ── REQUEST ROUTER ────────────────────────────────────────────────────────

  private void processRequest(Request request) {
    try {
      switch (request.getRequestType()) {
        case Login:
          handleLogin(request);
          break;
        case Register_Client:
          handleRegisterClient(request);
          break;
        case Get_All_Listings:
          model.getAllListings();
          break;
        case Get_Available_Listings:
          model.getAvailableListings();
          break;
        case Get_Listings_By_Owner:
          handleGetListingsByOwner(request);
          break;
        case Add_Listing:
          handleAddListing(request);
          break;
        case Remove_Listing:
          handleRemoveListing(request);
          break;
        case Update_Listing:
          handleUpdateListing(request);
          break;
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
          model.getAllBookings();
          break;
        case Get_Bookings_By_Client:
          handleGetBookingsByClient(request);
          break;
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
          model.getAllOwnerApplications();
          break;
        case Approve_Owner_Application:
          handleApproveOwnerApplication(request);
          break;
        case Reject_Owner_Application:
          handleRejectOwnerApplication(request);
          break;
        case Get_Favorites_By_Client:
          handleGetFavoritesByClient(request);
          break;
        case Add_Favorite:
          handleAddFavorite(request);
          break;
        case Remove_Favorite:
          handleRemoveFavorite(request);
          break;
        case Submit_Tenancy_Application:
          handleSubmitTenancyApplication(request);
          break;
        case Get_Tenancy_Apps_By_Owner:
          handleGetTenancyAppsByOwner(request);
          break;
        case Get_Tenancy_Apps_By_Client:
          handleGetTenancyAppsByClient(request);
          break;
        case Delete_Tenancy_Application:
          handleDeleteTenancyApp(request);
          break;
        case Update_Tenancy_Application_Status:
          handleUpdateTenancyStatus(request);
          break;
        case Delete_Client:
          handleDeleteClient(request);
          break;
        case Delete_Property_Owner:
          handleDeletePropertyOwner(request);
          break;
        case Get_Owner_By_Id:
          handleGetOwnerById(request);
          break;
        default:
          sendResponse(new Response(false, "ERROR", "Unknown request type"));
      }
    } catch (Exception e) {
      sendResponse(new Response(false, "ERROR", e.getMessage()));
    }
  }

  // ── AUTH ──────────────────────────────────────────────────────────────────

  private void handleLogin(Request r) {
    model.login((String) r.getArgs()[0], (String) r.getArgs()[1]);
  }

  private void handleRegisterClient(Request r) {
    Client client = gson.fromJson(gson.toJson(r.getArgs()[0]), Client.class);
    model.registerClient(client);
  }

  // ── LISTINGS ──────────────────────────────────────────────────────────────

  private void handleAddListing(Request r) {
    Listing listing = gson.fromJson(gson.toJson(r.getArgs()[0]), Listing.class);
    model.addListing(listing);
  }

  private void handleRemoveListing(Request r) {
    model.removeListing(((Double) r.getArgs()[0]).intValue());
  }

  private void handleUpdateListing(Request r) {
    Listing listing = gson.fromJson(gson.toJson(r.getArgs()[0]), Listing.class);
    model.updateListing(listing);
  }

  private void handleGetListingsByOwner(Request r) {
    model.getListingsByOwner(((Double) r.getArgs()[0]).intValue());
  }

  // ── BOOKINGS ──────────────────────────────────────────────────────────────

  private void handleAddBooking(Request r) {
    Booking booking = gson.fromJson(gson.toJson(r.getArgs()[0]), Booking.class);
    model.addBooking(booking);
  }

  private void handleRemoveBooking(Request r) {
    int clientId = ((Double) r.getArgs()[0]).intValue();
    int listingId = ((Double) r.getArgs()[1]).intValue();
    model.removeBooking(clientId, listingId);
  }

  private void handleUpdateBooking(Request r) {
    Booking booking = gson.fromJson(gson.toJson(r.getArgs()[0]), Booking.class);
    model.updateBooking(booking);
  }

  private void handleGetBookingsByClient(Request r) {
    model.getBookingsByClient(((Double) r.getArgs()[0]).intValue());
  }

  // ── OWNER APPLICATIONS ────────────────────────────────────────────────────

  private void handleAddOwnerApplication(Request r) {
    OwnerApplication app = gson.fromJson(gson.toJson(r.getArgs()[0]), OwnerApplication.class);
    model.addOwnerApplication(app);
  }

  private void handleRemoveOwnerApplication(Request r) {
    model.removeOwnerApplication(((Double) r.getArgs()[0]).intValue());
  }

  private void handleUpdateOwnerApplication(Request r) {
    OwnerApplication app = gson.fromJson(gson.toJson(r.getArgs()[0]), OwnerApplication.class);
    model.updateOwnerApplication(app);
  }

  private void handleApproveOwnerApplication(Request r) {
    int applicationId = ((Double) r.getArgs()[0]).intValue();
    int adminId = r.getArgs().length > 1 ? ((Double) r.getArgs()[1]).intValue() : 0;
    model.approveOwnerApplication(applicationId, adminId);
  }

  private void handleRejectOwnerApplication(Request r) {
    model.rejectOwnerApplication(((Double) r.getArgs()[0]).intValue());
  }

  // ── FAVORITES ─────────────────────────────────────────────────────────────

  private void handleGetFavoritesByClient(Request r) {
    model.getFavoritesByClient(((Double) r.getArgs()[0]).intValue());
  }

  private void handleAddFavorite(Request r) {
    int clientId = ((Double) r.getArgs()[0]).intValue();
    int listingId = ((Double) r.getArgs()[1]).intValue();
    model.addFavorite(clientId, listingId);
  }

  private void handleRemoveFavorite(Request r) {
    int clientId = ((Double) r.getArgs()[0]).intValue();
    int listingId = ((Double) r.getArgs()[1]).intValue();
    model.removeFavorite(clientId, listingId);
  }

  // ── TENANCY APPLICATIONS ──────────────────────────────────────────────────

  private void handleSubmitTenancyApplication(Request r) {
    TenancyApplication app = gson.fromJson(gson.toJson(r.getArgs()[0]), TenancyApplication.class);
    model.addTenancyApplication(app);
  }

  private void handleGetTenancyAppsByOwner(Request r) {
    model.getTenancyApplicationsByOwner(((Double) r.getArgs()[0]).intValue());
  }

  private void handleGetTenancyAppsByClient(Request r) {
    model.getTenancyApplicationsByClient(((Double) r.getArgs()[0]).intValue());
  }

  private void handleDeleteTenancyApp(Request r) {
    model.deleteTenancyApplication(((Double) r.getArgs()[0]).intValue());
  }

  private void handleUpdateTenancyStatus(Request r) {
    int id = ((Double) r.getArgs()[0]).intValue();
    String status = (String) r.getArgs()[1];
    model.updateTenancyApplicationStatus(id, status);
  }

  // ── ACCOUNT ───────────────────────────────────────────────────────────────

  private void handleDeleteClient(Request r) {
    model.deleteClient(((Double) r.getArgs()[0]).intValue());
  }

  private void handleDeletePropertyOwner(Request r) {
    model.deletePropertyOwner(((Double) r.getArgs()[0]).intValue());
  }

  // ── LOOKUP ────────────────────────────────────────────────────────────────

  private void handleGetOwnerById(Request r) {
    model.getOwnerById(((Double) r.getArgs()[0]).intValue());
  }

  // ── MODEL EVENT BROADCAST ─────────────────────────────────────────────────

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    sendResponse(new Response(true, evt.getPropertyName(), evt.getNewValue()));
  }

  // ── HELPERS ───────────────────────────────────────────────────────────────

  private void sendResponse(Response response) {
    String json = gson.toJson(response);
    out.println(json);
    System.out.println("Sent JSON: " + json);
  }

  private void closeConnection() {
    running = false;
    model.removePropertyChangeListener(this);
    try {
      if (socket != null)
        socket.close();
      if (in != null)
        in.close();
      if (out != null)
        out.close();
      System.out.println("Connection closed");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
