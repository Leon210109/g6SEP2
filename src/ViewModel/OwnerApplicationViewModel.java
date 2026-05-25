package ViewModel;

import Model.OwnerApplication;
import Model.RentalModel;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class OwnerApplicationViewModel implements PropertyChangeListener
{
    private final RentalModel model;
    private final StringProperty errorMessage = new SimpleStringProperty("");
    private final BooleanProperty submitted    = new SimpleBooleanProperty(false);

    public OwnerApplicationViewModel()
    {
        model = ViewModelFactory.getInstance().getModel();
        model.addPropertyChangeListener(this);
    }

    public void submitApplication(OwnerApplication application)
    {
        model.addOwnerApplication(application);
    }

    /** Call when the dialog closes to remove the model listener. */
    public void dispose()
    {
        model.removePropertyChangeListener(this);
    }

    public StringProperty errorMessageProperty() { return errorMessage; }
    public BooleanProperty submittedProperty()   { return submitted; }

    @Override
    public void propertyChange(PropertyChangeEvent evt)
    {
        switch (evt.getPropertyName())
        {
            case "OwnerApplicationAdded" -> Platform.runLater(() -> submitted.set(true));
            case "ERROR" -> Platform.runLater(() ->
                errorMessage.set(evt.getNewValue() != null ? evt.getNewValue().toString() : "Unknown error"));
        }
    }
}
