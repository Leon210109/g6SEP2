package ViewModel;

import Model.RentalModel;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/** ViewModel for the Property Owner user type — navigation + account deletion. */
public class PropertyOwnerViewModel implements AppViewModel, PropertyChangeListener
{
    private static final String HOME = "Property Owner";
    private final StringProperty  currentSection = new SimpleStringProperty(HOME);
    private final BooleanProperty accountDeleted = new SimpleBooleanProperty(false);
    private final RentalModel model;

    public PropertyOwnerViewModel()
    {
        model = ViewModelFactory.getInstance().getModel();
        model.addPropertyChangeListener(this);
    }

    @Override public StringProperty currentSectionProperty() { return currentSection; }
    @Override public String getCurrentSection()              { return currentSection.get(); }
    @Override public void   navigateTo(String section)       { currentSection.set(section); }
    @Override public void   navigateHome()                   { currentSection.set(HOME); }

    public void deleteAccount(int ownerId)           { model.deletePropertyOwner(ownerId); }
    public BooleanProperty accountDeletedProperty()  { return accountDeleted; }

    @Override
    public void propertyChange(PropertyChangeEvent evt)
    {
        if ("PropertyOwnerDeleted".equals(evt.getPropertyName()))
            Platform.runLater(() -> accountDeleted.set(true));
    }
}