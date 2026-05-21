package ViewModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/** ViewModel for the Client user type. */
public class ClientViewModel implements AppViewModel
{
    private static final String HOME = "Client";
    private final StringProperty currentSection = new SimpleStringProperty(HOME);

    @Override public StringProperty currentSectionProperty() { return currentSection; }
    @Override public String getCurrentSection()              { return currentSection.get(); }
    @Override public void   navigateTo(String section)       { currentSection.set(section); }
    @Override public void   navigateHome()                   { currentSection.set(HOME); }

    public void loadBookings()          { /* TODO: fetch from BookingDAO */ }
    public void loadAvailableListings() { /* TODO: fetch from ListingDAO */ }
}