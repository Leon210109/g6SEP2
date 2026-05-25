package ViewModel;

import Model.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class ListingViewModel implements PropertyChangeListener
{
  private RentalModel model;
  private ObservableList<Listing> listings;
  private ObjectProperty<Listing> selectedListing;


  private StringProperty errorMessage;

  public ListingViewModel(RentalModel model)
  {
    this.model=model;
    listings= FXCollections.observableArrayList();
    selectedListing= new SimpleObjectProperty<>();
    errorMessage= new SimpleStringProperty();
    model.addPropertyChangeListener(this);

  }

  public void loadListings()
  {
    model.getAllListings();
  }

  public void removeListing()
  {
    if(selectedListing.get()==null)
    {
      errorMessage.set("Select a list first");
      return;
    }
    model.removeListing(selectedListing.get().getId());
  }

  // property change event
  public void propertyChange(PropertyChangeEvent evt)
  {
    switch(evt.getPropertyName())
    {
      case "GET_ALL_LISTINGS":
        listings.clear();
        listings.addAll(
            (ArrayList<Listing>)evt.getNewValue()
        );
        break;
      case "ListingAdded":
        Listing listing=(Listing) evt.getNewValue();
        listings.add(listing);
        break;

      case "ListingRemoved":
        int listingid=(int)evt.getNewValue();
        listings.removeIf(l->l.getId()==listingid);
        break;

      case "ListingUpdated":
        Listing updatedListing=(Listing)evt.getNewValue();
        for(int i=0; i <listings.size();i++)
        {
          if(listings.get(i).getId()==updatedListing.getId()){
            listings.set(i,updatedListing);
           break;
          }
          break;
        }

      case "ERROR":
        errorMessage.set((String)
        evt.getNewValue());
        break;

    }
  }
  public ObservableList<Listing>
  getListings()
  {
    return listings;
  }

  public ObjectProperty<Listing>
  selectedListingProperty()
  {
    return selectedListing;
  }

  public StringProperty
  errorMessageProperty()
  {
    return errorMessage;
  }
}
