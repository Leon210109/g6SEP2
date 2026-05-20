package ViewModel;

import Model.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * ViewModel for the login screen.
 * Tracks which user type the user has selected before entering the app.
 */
public class LoginViewModel implements PropertyChangeListener
{
  private RentalModel model;
  // input fields
  private StringProperty username;
  private StringProperty password;
  //ui state
  private StringProperty errorMessage;
  private User currentUser;
  private BooleanProperty loginStatus;

  public LoginViewModel(RentalModel model)
  {
    this.model = model;
    username = new SimpleStringProperty();
    password = new SimpleStringProperty();
    loginStatus = new SimpleBooleanProperty(false);
    // this errorMessage will later bind to errorLabel to show error while login
    this.errorMessage = new SimpleStringProperty();
    model.addPropertyChangeListener(this);
  }

  //login action
  public void login()
  {
    errorMessage.set("");
    model.login(username.get(), password.get());
  }

  //model events
  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    switch (evt.getPropertyName())
    {
      case "LOGIN_SUCCESS":
        currentUser = (User) evt.getNewValue();
        loginStatus.set(true);

        System.out.println("Login Successful");
        break;
      case "ERROR":
        errorMessage.set((String) evt.getNewValue());
        break;
    }
  }
  // ───────────────── PROPERTIES ─────────────────

  public StringProperty usernameProperty()
  {
    return username;
  }

  public StringProperty passwordProperty()
  {
    return password;
  }

  public StringProperty errorMessageProperty()
  {
    return errorMessage;
  }

  public BooleanProperty loginStatusProperty()
  {
    return loginStatus;
  }



}
