package ViewModel;

import javafx.beans.property.StringProperty;

/**
 * Common interface for all main-screen ViewModels.
 * The View binds to currentSectionProperty() and calls navigateTo /
 * navigateHome
 * without needing to know the concrete ViewModel type.
 */
public interface AppViewModel {
    StringProperty currentSectionProperty();

    String getCurrentSection();

    void navigateTo(String section);

    void navigateHome();
}
