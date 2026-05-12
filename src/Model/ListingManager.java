package Model;

import Persistence.ListingDAO;
import java.util.ArrayList;

public class ListingManager
{
  private ListingDAO listingDAO;

  public ListingManager(){
    listingDAO = new ListingDAO();
  }

  public void addListing(Listing listing){
    listingDAO.CreateListing(listing);
  }

  public Listing getListingById(int listingId){
    return listingDAO.getListingById(listingId);
  }

  public ArrayList<Listing> getListingsByOwnerId(int ownerId){
    return listingDAO.getListingsByOwnerId(ownerId);
  }

  public ArrayList<Listing> getAllListings(){
    return listingDAO.getAllListings();
  }

  public ArrayList<Listing> getAvailableListings(){
    return listingDAO.getAvailableListings();
  }

  public int getSize(){
    return listingDAO.getAllListings().size();
  }

  // Note: Delete and update operations would need DELETE/UPDATE SQL methods in DAO
  // Keeping these as placeholders for now
  public void removeListing(int listingId){
    // TODO: Add deleteListing method to ListingDAO
    throw new UnsupportedOperationException("Delete operation not yet implemented in DAO");
  }

  public void updateListing(Listing listing){
    // TODO: Add updateListing method to ListingDAO
    throw new UnsupportedOperationException("Update operation not yet implemented in DAO");
  }
}
