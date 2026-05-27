package ViewModel;

import Model.RentalModel;
import Model.TenancyApplication;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class TenancyViewModel implements PropertyChangeListener
{
  private final RentalModel model;
  private final ObservableList<TenancyApplication> applications;
  private final StringProperty errorMessage;
  private final BooleanProperty submissionSuccess;
  private final Gson gson;

  public TenancyViewModel(RentalModel model)
  {
    this.model             = model;
    this.applications      = FXCollections.observableArrayList();
    this.errorMessage      = new SimpleStringProperty();
    this.submissionSuccess = new SimpleBooleanProperty(false);
    this.gson              = new Gson();
    model.addPropertyChangeListener(this);
  }

  public void submitApplication(TenancyApplication app)   { model.addTenancyApplication(app); }
  public void loadApplicationsByOwner(int ownerId)        { model.getTenancyApplicationsByOwner(ownerId); }
  public void loadApplicationsByClient(int clientId)      { model.getTenancyApplicationsByClient(clientId); }
  public void deleteApplication(int id)                   { model.deleteTenancyApplication(id); }
  public void updateApplicationStatus(int id, String s)   { model.updateTenancyApplicationStatus(id, s); }

  @Override
  public void propertyChange(PropertyChangeEvent evt)
  {
    switch (evt.getPropertyName()) {
      case "TenancyApplicationAdded": {
        Platform.runLater(() -> submissionSuccess.set(true));
        break;
      }
      case "TENANCY_APPS_BY_OWNER":
      case "TENANCY_APPS_BY_CLIENT": {
        Type t = new TypeToken<ArrayList<TenancyApplication>>(){}.getType();
        ArrayList<TenancyApplication> list = gson.fromJson(gson.toJson(evt.getNewValue()), t);
        Platform.runLater(() -> {
          applications.clear();
          applications.addAll(list);
        });
        break;
      }
      case "TenancyApplicationDeleted": {
        int id = (int) evt.getNewValue();
        Platform.runLater(() -> applications.removeIf(a -> a.getId() == id));
        break;
      }
      case "TenancyApplicationStatusUpdated": {
        // Server only returns the id; reload to get fresh status from server
        // Callers may choose to refresh by calling loadApplicationsByOwner/Client again
        Platform.runLater(() -> {
          // signal vy temporarily toggling — controllers can listen to the list
        });
        break;
      }
      case "ERROR": {
        Platform.runLater(() -> errorMessage.set((String) evt.getNewValue()));
        break;
      }
    }
  }

  public ObservableList<TenancyApplication> getApplications() { return applications; }
  public StringProperty  errorMessageProperty()               { return errorMessage; }
  public BooleanProperty submissionSuccessProperty()          { return submissionSuccess; }
  public void resetSubmissionSuccess()                        { submissionSuccess.set(false); }
}
