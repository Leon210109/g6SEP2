package SEP2.SEP2.src.ViewModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

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
