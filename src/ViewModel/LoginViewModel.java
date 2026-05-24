package ViewModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * ViewModel for the login screen.
 * Tracks login credentials and user type selection.
 */
public class LoginViewModel {
    private final StringProperty username = new SimpleStringProperty("");
    private final StringProperty password = new SimpleStringProperty("");
    private final StringProperty errorMessage = new SimpleStringProperty("");
    private final StringProperty selectedUserType = new SimpleStringProperty("Client");

    public StringProperty usernameProperty() {
        return username;
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public StringProperty errorMessageProperty() {
        return errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage.get();
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage.set(errorMessage);
    }

    public void clearError() {
        this.errorMessage.set("");
    }

    public StringProperty selectedUserTypeProperty() {
        return selectedUserType;
    }

    public String getSelectedUserType() {
        return selectedUserType.get();
    }

    public void setSelectedUserType(String type) {
        selectedUserType.set(type);
    }

    public void clearFields() {
        username.set("");
        password.set("");
        errorMessage.set("");
    }
}
