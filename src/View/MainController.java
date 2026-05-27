package View;

import ViewModel.AdminViewModel;
import ViewModel.AppViewModel;
import ViewModel.ClientViewModel;
import ViewModel.PropertyOwnerViewModel;
import ViewModel.ViewModelFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
    private Button deleteAccountBtn;
    @FXML
    private MenuButton navMenu;
    @FXML
    private StackPane contentArea;
    @FXML
    private VBox homeView;

    private AppViewModel vm;
    private String userType;
    private Object currentUser;

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
            for (String section : new String[] { "Bookings", "Available Listings", "My Favourites", "My Apartments" }) {
                MenuItem item = new MenuItem(section);
                item.setStyle("-fx-font-family: 'Cambria'; -fx-font-size: 13px;");
                item.setOnAction(e -> clientVM.navigateTo(section));
                navMenu.getItems().add(item);
            }
        } else if (vm instanceof PropertyOwnerViewModel ownerVM) {
            for (String section : new String[] { "My Listings", "My Bookings", "Tenant Applications" }) {
                MenuItem item = new MenuItem(section);
                item.setStyle("-fx-font-family: 'Cambria'; -fx-font-size: 13px;");
                item.setOnAction(e -> ownerVM.navigateTo(section));
                navMenu.getItems().add(item);
            }
        } else if (vm instanceof AdminViewModel adminVM) {
            for (String section : new String[] { "Owner Applications", "Manage Listings" }) {
                MenuItem item = new MenuItem(section);
                item.setStyle("-fx-font-family: 'Cambria'; -fx-font-size: 13px;");
                item.setOnAction(e -> adminVM.navigateTo(section));
                navMenu.getItems().add(item);
            }
        }

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

            boolean showDelete = isHome && !(currentUser instanceof Model.Admin);
            deleteAccountBtn.setVisible(showDelete);
            deleteAccountBtn.setManaged(showDelete);

            if (isHome) {
                buildHomeView();
                contentArea.getChildren().setAll(homeView);
            } else {
                contentArea.getChildren().setAll(loadSectionView(newVal));
            }
        });

        // Bind delete-account success back to login
        if (vm instanceof ClientViewModel clientVM) {
            clientVM.accountDeletedProperty().addListener((obs, o, deleted) -> {
                if (deleted) navigateBackToLogin();
            });
        } else if (vm instanceof PropertyOwnerViewModel ownerVM) {
            ownerVM.accountDeletedProperty().addListener((obs, o, deleted) -> {
                if (deleted) navigateBackToLogin();
            });
        }

        // Show delete account button on home for non-admin users
        boolean showDelete = !(currentUser instanceof Model.Admin);
        deleteAccountBtn.setVisible(showDelete);
        deleteAccountBtn.setManaged(showDelete);

        // Build initial home view (adds Apply button for clients, etc.)
        buildHomeView();
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/LoginView.fxml"));
        Parent loginRoot = loader.load();
        backToLoginBtn.getScene().setRoot(loginRoot);
    }

    @FXML
    private void handleDeleteAccount() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Account");
        confirm.setHeaderText("Permanently delete your account?");
        confirm.setContentText("This cannot be undone. All your data will be removed.");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (vm instanceof ClientViewModel clientVM && currentUser instanceof Model.Client client) {
                    clientVM.deleteAccount(client.getID());
                } else if (vm instanceof PropertyOwnerViewModel ownerVM && currentUser instanceof Model.PropertyOwner owner) {
                    ownerVM.deleteAccount(owner.getID());
                }
            }
        });
    }

    private void navigateBackToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/LoginView.fxml"));
            Parent loginRoot = loader.load();
            deleteAccountBtn.getScene().setRoot(loginRoot);
        } catch (IOException e) { e.printStackTrace(); }
    }

    // ── Section view loader ───────────────────────────────────────────────────

    private Parent loadSectionView(String section) {
        String fxmlFile = null;
        if (vm instanceof ClientViewModel) {
            fxmlFile = switch (section) {
                case "Available Listings" -> "BrowseListingsView.fxml";
                case "Bookings" -> "MyBookingsView.fxml";
                case "My Favourites" -> "MyFavoritesView.fxml";
                case "My Apartments" -> "MyApartmentsView.fxml";
                default -> null;
            };
        } else if (vm instanceof PropertyOwnerViewModel) {
            fxmlFile = switch (section) {
                case "My Listings" -> "MyListingsView.fxml";
                case "My Bookings" -> "MyBookingsView.fxml";
                case "Tenant Applications" -> "TenantApplicationsView.fxml";
                default -> null;
            };
        } else if (vm instanceof AdminViewModel) {
            fxmlFile = switch (section) {
                case "Owner Applications" -> "AdminView.fxml";
                case "Manage Listings" -> "AdminListingsView.fxml";
                default -> null;
            };
        }
        if (fxmlFile == null)
            return homeView;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/" + fxmlFile));
            Parent root = loader.load();

            Object controller = loader.getController();
            if (controller instanceof MyListingsController && currentUser instanceof Model.PropertyOwner) {
                ((MyListingsController) controller).setPropertyOwner((Model.PropertyOwner) currentUser);
            } else if (controller instanceof BrowseListingsController && currentUser instanceof Model.Client) {
                ((BrowseListingsController) controller).setClient((Model.Client) currentUser);
            } else if (controller instanceof MyBookingsController && currentUser instanceof Model.Client) {
                ((MyBookingsController) controller).setClient((Model.Client) currentUser);
            } else if (controller instanceof TenantApplicationsController && currentUser instanceof Model.PropertyOwner) {
                ((TenantApplicationsController) controller).setPropertyOwner((Model.PropertyOwner) currentUser);
            } else if (controller instanceof MyFavoritesController && currentUser instanceof Model.Client) {
                ((MyFavoritesController) controller).setClient((Model.Client) currentUser);
            } else if (controller instanceof MyApartmentsController && currentUser instanceof Model.Client) {
                ((MyApartmentsController) controller).setClient((Model.Client) currentUser);
            } else if (controller instanceof AdminController && currentUser instanceof Model.Admin) {
                ((AdminController) controller).setAdmin((Model.Admin) currentUser);
            } else if (controller instanceof AdminListingsController && currentUser instanceof Model.Admin) {
                ((AdminListingsController) controller).setAdmin((Model.Admin) currentUser);
            }

            return root;
        } catch (IOException e) {
            e.printStackTrace();
            return homeView;
        }
    }

    // ── Home view builder (adds Apply button for Clients) ────────────────────

    private void buildHomeView() {
        homeView.getChildren().clear();
        homeView.getChildren().addAll(welcomeText, sectionLabel);

        if (currentUser instanceof Model.Client client) {
            javafx.scene.control.Button applyBtn = new javafx.scene.control.Button("Apply as Property Owner");
            applyBtn.setStyle(
                "-fx-background-color: #1A5F3F;" +
                "-fx-text-fill: white;" +
                "-fx-font-family: 'Cambria';" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 8;" +
                "-fx-padding: 12 28;" +
                "-fx-cursor: hand;"
            );
            applyBtn.setOnAction(e -> openOwnerApplicationForm(client));
            homeView.getChildren().add(applyBtn);
        }
    }

    private void openOwnerApplicationForm(Model.Client client) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/OwnerApplicationFormView.fxml"));
            Parent root = loader.load();
            OwnerApplicationFormController ctrl = loader.getController();
            ctrl.setClient(client);

            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.setTitle("Apply as Property Owner");
            stage.setScene(new javafx.scene.Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
