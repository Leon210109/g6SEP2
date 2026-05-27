package ViewModel;

import Model.OwnerApplication;
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
import java.util.ArrayList;

/** ViewModel for the Admin user type — navigation + owner-application management. */
public class AdminViewModel implements AppViewModel, PropertyChangeListener
{
    private static final String HOME = "Admin";
    private final StringProperty currentSection = new SimpleStringProperty(HOME);

    private final RentalModel model;
    private final ObservableList<OwnerApplication> applications = FXCollections.observableArrayList();
    private final StringProperty statusMessage = new SimpleStringProperty("");

    public AdminViewModel()
    {
        model = ViewModelFactory.getInstance().getModel();
        model.addPropertyChangeListener(this);
    }

    // ── AppViewModel ──────────────────────────────────────────────────────────
    @Override public StringProperty currentSectionProperty() { return currentSection; }
    @Override public String getCurrentSection()              { return currentSection.get(); }
    @Override public void   navigateTo(String section)       { currentSection.set(section); }
    @Override public void   navigateHome()                   { currentSection.set(HOME); }

    // ── Admin operations ──────────────────────────────────────────────────────
    public void loadApplications()                       { model.getAllOwnerApplications(); }
    public void approveApplication(int id, int adminId)  { model.approveOwnerApplication(id, adminId); }
    public void rejectApplication(int id)                { model.rejectOwnerApplication(id); }
    public void removeApplication(int id)                { model.removeOwnerApplication(id); }

    public ObservableList<OwnerApplication> getApplications() { return applications; }
    public StringProperty statusMessageProperty()             { return statusMessage; }

    @Override
    public void propertyChange(PropertyChangeEvent evt)
    {
        switch (evt.getPropertyName())
        {
            case "GET_ALL_OWNER_APPLICATIONS" ->
            {
                Gson gson = new Gson();
                ArrayList<OwnerApplication> apps = gson.fromJson(
                    gson.toJson(evt.getNewValue()),
                    new TypeToken<ArrayList<OwnerApplication>>(){}.getType());
                Platform.runLater(() ->
                {
                    applications.setAll(apps);
                    statusMessage.set(apps.isEmpty()
                        ? "No owner applications found."
                        : apps.size() + " application(s) total");
                });
            }
            case "OwnerApplicationApproved", "OwnerApplicationRejected", "OwnerApplicationRemoved" ->
                Platform.runLater(this::loadApplications);
            case "ERROR" ->
                Platform.runLater(() -> statusMessage.set("Error: " + evt.getNewValue()));
        }
    }
}