package ViewModel;

import Model.*;
import javafx.application.Platform;
import javafx.beans.property.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class RegistrationViewModel implements PropertyChangeListener
{
  private RentalModel model;

  // input fields
  private StringProperty firstname;
  private StringProperty lastname;
  private StringProperty email;
  private StringProperty phonenumber;

  private StringProperty username;
  private StringProperty password;
  private StringProperty confirmPassword;

  private ObjectProperty<Date> dob;
  private StringProperty gender;
  private StringProperty nationality;
  private StringProperty errorMessage;
  private BooleanProperty registrationSuccess;

  public RegistrationViewModel(RentalModel model)
  {
    this.model=model;
    firstname= new SimpleStringProperty();
    lastname= new SimpleStringProperty();
    phonenumber= new SimpleStringProperty();
    username= new SimpleStringProperty();
    password= new SimpleStringProperty();
    confirmPassword= new SimpleStringProperty();
    email= new SimpleStringProperty();
    dob= new SimpleObjectProperty<>();
    gender= new SimpleStringProperty();
    nationality= new SimpleStringProperty();
    registrationSuccess= new SimpleBooleanProperty(false);

    errorMessage= new SimpleStringProperty();
    model.addPropertyChangeListener(this);
  }

  // register action
  public void register()
  {
    errorMessage.set("");
    if(!ValidationCheck()){
      errorMessage.set("fill all required fields");
      return;
    }
     if(!password.get().equals(confirmPassword.get()))
    {
      errorMessage.set("Passwords" + "donot match");
      return;
    }
     String normalizedPhone = phonenumber.get() == null ? "" : phonenumber.get().replaceAll("[^0-9]", "");
     Client client= new Client(firstname.get(),
         lastname.get(), email.get(), normalizedPhone, username.get(),
         password.get(), dob.get(),gender.get(), nationality.get());
     model.registerClient(client);
  }

  private boolean ValidationCheck()
  {
    if(firstname.get().isEmpty()
    || lastname.get().isEmpty()
    || password.get().isEmpty()){
      return false;
    }
    return true;
  }
  @Override
  public void propertyChange(PropertyChangeEvent evt)
  {
    switch(evt.getPropertyName())
    {
      case "ClientRegistered":
        Platform.runLater(() -> registrationSuccess.set(true));
        break;
      case "ERROR":
        Platform.runLater(() -> errorMessage.set((String) evt.getNewValue()));
        break;
    }
  }
  //property events
  public StringProperty firstNameProperty()
  {
    return firstname;
  }

  public StringProperty lastNameProperty()
  {
    return lastname;
  }

  public StringProperty emailProperty()
  {
    return email;
  }

  public StringProperty phoneNumberProperty()
  {
    return phonenumber;
  }

  public StringProperty usernameProperty()
  {
    return username;
  }

  public StringProperty passwordProperty()
  {
    return password;
  }

  public StringProperty
  confirmPasswordProperty()
  {
    return confirmPassword;
  }

  public ObjectProperty<Date>
  dobProperty()
  {
    return dob;
  }

  public StringProperty genderProperty()
  {
    return gender;
  }

  public StringProperty
  nationalityProperty()
  {
    return nationality;
  }

  public StringProperty
  errorMessageProperty()
  {
    return errorMessage;
  }

  public BooleanProperty
  registrationSuccessProperty()
  {
    return registrationSuccess;
  }
}



