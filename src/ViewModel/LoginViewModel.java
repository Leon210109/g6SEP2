package ViewModel;

import Model.Admin;
import Model.Client;
import Model.PropertyOwner;
import Model.RentalModel;
import javafx.application.Platform;
import javafx.beans.property.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class LoginViewModel implements PropertyChangeListener {

    private final RentalModel model;

    private final StringProperty username         = new SimpleStringProperty("");
    private final StringProperty password         = new SimpleStringProperty("");
    private final StringProperty errorMessage     = new SimpleStringProperty("");
    private final StringProperty loggedInUserType = new SimpleStringProperty("");
    private final ObjectProperty<Object> loggedInUser = new SimpleObjectProperty<>(null);

    public LoginViewModel(RentalModel model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }

    public void login() {
        model.login(username.get(), password.get());
    }

    public void resetLoginState() {
        loggedInUserType.set("");
        loggedInUser.set(null);
        password.set("");
    }

    public void clearError() { errorMessage.set(""); }

    public StringProperty usernameProperty()         { return username; }
    public StringProperty passwordProperty()         { return password; }
    public StringProperty errorMessageProperty()     { return errorMessage; }
    public StringProperty loggedInUserTypeProperty() { return loggedInUserType; }
    public ObjectProperty<Object> loggedInUserProperty() { return loggedInUser; }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "LOGIN_SUCCESS_CLIENT" -> Platform.runLater(() -> {
                loggedInUser.set(evt.getNewValue());
                loggedInUserType.set("Client");
            });
            case "LOGIN_SUCCESS_OWNER" -> Platform.runLater(() -> {
                loggedInUser.set(evt.getNewValue());
                loggedInUserType.set("Property Owner");
            });
            case "LOGIN_SUCCESS_ADMIN" -> Platform.runLater(() -> {
                loggedInUser.set(evt.getNewValue());
                loggedInUserType.set("Admin");
            });
            case "ERROR" -> Platform.runLater(() ->
                errorMessage.set(evt.getNewValue() != null ? evt.getNewValue().toString() : "Login failed.")
            );
        }
    }
}
