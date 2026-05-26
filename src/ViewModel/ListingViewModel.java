package ViewModel;

import Model.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class ListingViewModel implements PropertyChangeListener {

    private final RentalModel model;
    private final Gson gson = new Gson();

    private final ObservableList<Listing> listings = FXCollections.observableArrayList();
    private final BooleanProperty addSuccess    = new SimpleBooleanProperty(false);
    private final BooleanProperty updateSuccess = new SimpleBooleanProperty(false);
    private final ObjectProperty<PropertyOwner> ownerResult = new SimpleObjectProperty<>(null);

    public ListingViewModel(RentalModel model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }

    private final StringProperty errorMessage = new SimpleStringProperty("");

    public ObservableList<Listing> getListings()     { return listings; }
    public BooleanProperty addSuccessProperty()      { return addSuccess; }
    public BooleanProperty updateSuccessProperty()   { return updateSuccess; }
    public ObjectProperty<PropertyOwner> ownerResultProperty() { return ownerResult; }
    public StringProperty errorMessageProperty()     { return errorMessage; }
    public void resetAddSuccess()    { addSuccess.set(false); }
    public void resetUpdateSuccess() { updateSuccess.set(false); }

    public void loadListings()                    { model.getAllListings(); }
    public void loadAvailableListings()            { model.getAvailableListings(); }
    public void loadListingsByOwner(int ownerId)   { model.getListingsByOwner(ownerId); }
    public void loadOwnerById(int ownerId)         { model.getOwnerById(ownerId); }
    public void addListing(Listing l)              { model.addListing(l); }
    public void updateListing(Listing l)           { model.updateListing(l); }
    public void removeListing(int listingId)       { model.removeListing(listingId); }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "GET_ALL_LISTINGS", "AVAILABLE_LISTINGS", "LISTINGS_BY_OWNER" -> {
                Type t = new TypeToken<ArrayList<Listing>>(){}.getType();
                ArrayList<Listing> list = gson.fromJson(gson.toJson(evt.getNewValue()), t);
                Platform.runLater(() -> { listings.clear(); listings.addAll(list); });
            }
            case "ListingAdded" -> {
                Listing l = gson.fromJson(gson.toJson(evt.getNewValue()), Listing.class);
                Platform.runLater(() -> { listings.add(l); addSuccess.set(true); });
            }
            case "ListingUpdated" -> {
                Listing updated = gson.fromJson(gson.toJson(evt.getNewValue()), Listing.class);
                Platform.runLater(() -> {
                    for (int i = 0; i < listings.size(); i++) {
                        if (listings.get(i).getId() == updated.getId()) { listings.set(i, updated); break; }
                    }
                    updateSuccess.set(true);
                });
            }
            case "ListingRemoved" -> {
                int id = gson.fromJson(gson.toJson(evt.getNewValue()), Integer.class);
                Platform.runLater(() -> listings.removeIf(l -> l.getId() == id));
            }
            case "OWNER_BY_ID" -> {
                PropertyOwner owner = gson.fromJson(gson.toJson(evt.getNewValue()), PropertyOwner.class);
                Platform.runLater(() -> ownerResult.set(owner));
            }
            case "ERROR" -> Platform.runLater(() -> errorMessage.set(evt.getNewValue() != null ? evt.getNewValue().toString() : "Error"));
        }
    }
}
