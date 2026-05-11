package SEP2.SEP2.src.Model;

import java.util.ArrayList;

public class ListingManager
{
  private ArrayList<Listing> listings;


  public ListingManager(){
    listings= new ArrayList<>();
  }
  public void addListing(Listing listing){
    listings.add(listing);
  }
  public int getSize(){
    return listings.size();
  }
  public void removeListing(int listingId){
    for(Listing listing:listings){
      if(listing.getId()==listingId){
        listings.remove(listing);
      }
    }
  }
  public void updateListing(Listing listing){
    for(int i=0; i<listings.size();i++){
      if(listings.get(i).getId()== listing.getId()){
        listings.set(i,listing);
      }
    }
  }
}
