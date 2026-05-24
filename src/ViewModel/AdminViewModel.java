package ViewModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/** ViewModel for the Admin user type. */
public class AdminViewModel implements AppViewModel
{
    private static final String HOME = "Admin";
    private final StringProperty currentSection = new SimpleStringProperty(HOME);

    @Override public StringProperty currentSectionProperty() { return currentSection; }
    @Override public String getCurrentSection()              { return currentSection.get(); }
    @Override public void   navigateTo(String section)       { currentSection.set(section); }
    @Override public void   navigateHome()                   { currentSection.set(HOME); }

    public void loadClients()        { /* TODO: fetch from ClientDAO */ }
    public void loadPropertyOwners() { /* TODO: fetch from PropertyOwnerDAO */ }
}