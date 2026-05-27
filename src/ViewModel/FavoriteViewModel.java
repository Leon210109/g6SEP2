package ViewModel;

import Model.Listing;
import Model.RentalModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class FavoriteViewModel implements PropertyChangeListener
{
  private final RentalModel model;
  private final ObservableList<Listing> favorites;
  private final StringProperty errorMessage;

  public FavoriteViewModel(RentalModel model)
  {
    this.model        = model;
    this.favorites    = FXCollections.observableArrayList();
    this.errorMessage = new SimpleStringProperty();
    model.addPropertyChangeListener(this);
  }

  public void loadFavoritesForClient(int clientId)
  {
    model.getFavoritesByClient(clientId);
  }

  public void addFavorite(int clientId, int listingId)
  {
    model.addFavorite(clientId, listingId);
  }

  public void removeFavorite(int clientId, int listingId)
  {
    model.removeFavorite(clientId, listingId);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt)
  {
    switch (evt.getPropertyName()) {
      case "FAVORITES_BY_CLIENT": {
        ArrayList<Listing> list =
            (ArrayList<Listing>) evt.getNewValue();
        Platform.runLater(() -> {
          favorites.clear();
          favorites.addAll(list);
        });
        break;
      }
      case "FavoriteRemoved": {
        // Optimistically remove from list using the int[] {clientId, listingId}
        int[] ids = (int[]) evt.getNewValue();
        Platform.runLater(() -> favorites.removeIf(l -> l.getId() == ids[1]));
        break;
      }
      case "ERROR": {
        Platform.runLater(() -> errorMessage.set((String) evt.getNewValue()));
        break;
      }
    }
  }

  public ObservableList<Listing> getFavorites()      { return favorites; }
  public StringProperty errorMessageProperty()       { return errorMessage; }
}
