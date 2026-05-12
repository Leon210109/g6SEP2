package SEP2.SEP2.src.ViewModel;

import javafx.beans.property.StringProperty;
public interface AppViewModel {
  StringProperty currentSectionProperty();

  String getCurrentSection();

  void navigateTo(String section);

  void navigateHome();
}