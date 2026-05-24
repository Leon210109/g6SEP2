package View;

import ViewModel.AdminViewModel;
import ViewModel.AppViewModel;
import ViewModel.ClientViewModel;
import ViewModel.PropertyOwnerViewModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import java.io.IOException;

public class MainController {

    @FXML
    private Text titleText;
    @FXML
    private Text headerSub;
    @FXML
    private Text welcomeText;
    @FXML
    private Text sectionLabel;
    @FXML
    private Button backToLoginBtn;
    @FXML
    private MenuButton navMenu;
    @FXML
    private StackPane contentArea;
    @FXML
    private VBox homeView;

    private AppViewModel vm;
    private String userType;
    private Object currentUser; // Client, PropertyOwner, or Admin object

    @FXML
    private void initialize() {
        backToLoginBtn.setOnMouseEntered(e -> backToLoginBtn.setStyle(
                "-fx-background-color: #143D29;" +
                        "-fx-text-fill: #F5F0E8;" +
                        "-fx-font-family: 'Cambria'; -fx-font-size: 13px; -fx-font-weight: bold;" +
                        "-fx-background-radius: 6; -fx-padding: 6 20; -fx-cursor: hand;"));
        backToLoginBtn.setOnMouseExited(e -> backToLoginBtn.setStyle(
                "-fx-background-color: #1A5F3F;" +
                        "-fx-text-fill: #F5F0E8;" +
                        "-fx-font-family: 'Cambria'; -fx-font-size: 13px; -fx-font-weight: bold;" +
                        "-fx-background-radius: 6; -fx-padding: 6 20; -fx-cursor: hand;"));
    }

    /** Called by LoginController after the FXML has been loaded. */
    public void init(String userType, Object user) {
        this.userType = userType;
        this.currentUser = user;

        vm = switch (userType) {
            case "Property Owner" -> new PropertyOwnerViewModel();
            case "Admin" -> new AdminViewModel();
            default -> new ClientViewModel();
        };

        welcomeText.setText(userType);

        // Populate nav menu items per user type
        if (vm instanceof ClientViewModel clientVM) {
            for (String section : new String[] { "Bookings", "Available Listings" }) {
                MenuItem item = new MenuItem(section);
                item.setStyle("-fx-font-family: 'Cambria'; -fx-font-size: 13px;");
                item.setOnAction(e -> clientVM.navigateTo(section));
                navMenu.getItems().add(item);
            }
        } else if (vm instanceof PropertyOwnerViewModel ownerVM) {
            for (String section : new String[] { "My Listings", "My Bookings" }) {
                MenuItem item = new MenuItem(section);
                item.setStyle("-fx-font-family: 'Cambria'; -fx-font-size: 13px;");
                item.setOnAction(e -> ownerVM.navigateTo(section));
                navMenu.getItems().add(item);
            }
        }
        // Admin: no nav items yet

        // React to section changes
        vm.currentSectionProperty().addListener((obs, oldVal, newVal) -> {
            boolean isHome = newVal.equals(userType);

            welcomeText.setText(isHome ? userType : newVal);
            welcomeText.setStyle("-fx-fill: #143D29;" +
                    "-fx-font-family: 'Palatino Linotype';" +
                    "-fx-font-size: " + (isHome ? "72" : "48") + "px;" +
                    "-fx-font-weight: bold;");

            sectionLabel.setText(isHome ? "" : userType);
            headerSub.setText("  " + (isHome ? "Home" : newVal));

            backToLoginBtn.setVisible(isHome);
            backToLoginBtn.setManaged(isHome);

            contentArea.getChildren().setAll(isHome ? homeView : loadSectionView(newVal));
        });
    }

    // ── Event handlers ────────────────────────────────────────────────────────

    @FXML
    private void handleTitleClick() {
        if (vm != null)
            vm.navigateHome();
    }

    @FXML
    private void handleTitleEnter() {
        titleText.setStyle("-fx-fill: #1A5F3F; -fx-cursor: hand;" +
                "-fx-font-family: 'Palatino Linotype'; -fx-font-size: 28px;" +
                "-fx-font-weight: bold; -fx-font-style: italic;");
    }

    @FXML
    private void handleTitleExit() {
        titleText.setStyle("-fx-fill: #143D29; -fx-cursor: hand;" +
                "-fx-font-family: 'Palatino Linotype'; -fx-font-size: 28px;" +
                "-fx-font-weight: bold; -fx-font-style: italic;");
    }

    @FXML
    private void handleBackToLogin() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginView.fxml"));
        Parent loginRoot = loader.load();
        backToLoginBtn.getScene().setRoot(loginRoot);
    }

    // ── Section view loader ───────────────────────────────────────────────────

    private Parent loadSectionView(String section) {
        String fxmlFile = null;
        if (vm instanceof ClientViewModel) {
            fxmlFile = switch (section) {
                case "Available Listings" -> "BrowseListingsView.fxml";
                case "Bookings" -> "MyBookingsView.fxml";
                default -> null;
            };
        } else if (vm instanceof PropertyOwnerViewModel) {
            fxmlFile = switch (section) {
                case "My Listings" -> "MyListingsView.fxml";
                case "My Bookings" -> "MyBookingsView.fxml";
                default -> null;
            };
        }
        if (fxmlFile == null)
            return homeView;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            
            // Pass user data to controller if needed
            Object controller = loader.getController();
            if (controller instanceof MyListingsController && currentUser instanceof Model.PropertyOwner) {
                ((MyListingsController) controller).setPropertyOwner((Model.PropertyOwner) currentUser);
            } else if (controller instanceof MyListingsController && currentUser == null) {
                // Admin bypass - no user object, can't show listings
                // This is okay for dev testing
            }
            
            return root;
        } catch (IOException e) {
            e.printStackTrace();
            return homeView;
        }
    }
}
