package ViewModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * ViewModel for the login screen.
 * Tracks which user type the user has selected before entering the app.
 */
public class LoginViewModel {
    private final StringProperty selectedUserType = new SimpleStringProperty("Client");

    public StringProperty selectedUserTypeProperty() {
        return selectedUserType;
    }

    public String getSelectedUserType() {
        return selectedUserType.get();
    }

    public void setSelectedUserType(String type) {
        selectedUserType.set(type);
    }
}
