package ViewModel;

import Model.RentalModel;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/** ViewModel for the Client user type — navigation + account deletion. */
public class ClientViewModel implements AppViewModel, PropertyChangeListener
{
    private static final String HOME = "Client";
    private final StringProperty  currentSection = new SimpleStringProperty(HOME);
    private final BooleanProperty accountDeleted = new SimpleBooleanProperty(false);
    private final RentalModel model;

    public ClientViewModel()
    {
        model = ViewModelFactory.getInstance().getModel();
        model.addPropertyChangeListener(this);
    }

    @Override public StringProperty currentSectionProperty() { return currentSection; }
    @Override public String getCurrentSection()              { return currentSection.get(); }
    @Override public void   navigateTo(String section)       { currentSection.set(section); }
    @Override public void   navigateHome()                   { currentSection.set(HOME); }

    public void deleteAccount(int clientId)          { model.deleteClient(clientId); }
    public BooleanProperty accountDeletedProperty()  { return accountDeleted; }

    @Override
    public void propertyChange(PropertyChangeEvent evt)
    {
        if ("ClientDeleted".equals(evt.getPropertyName()))
            Platform.runLater(() -> accountDeleted.set(true));
    }
}