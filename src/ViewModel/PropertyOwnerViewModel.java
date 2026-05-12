package SEP2.SEP2.src.ViewModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PropertyOwnerViewModel implements AppViewModel
{
  private static final String HOME = "Property Owner";
  private final StringProperty currentSection = new SimpleStringProperty(HOME);

  @Override public StringProperty currentSectionProperty() { return currentSection; }
  @Override public String getCurrentSection()              { return currentSection.get(); }
  @Override public void   navigateTo(String section)       { currentSection.set(section); }
  @Override public void   navigateHome()                   { currentSection.set(HOME); }

  public void loadMyListings()   { /* TODO: fetch from ListingDAO */ }
  public void loadApplications() { /* TODO: fetch from OwnerApplicationDAO */ }
}
