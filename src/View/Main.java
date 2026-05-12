package View;

import ViewModel.AdminViewModel;
import ViewModel.AppViewModel;
import ViewModel.ClientViewModel;
import ViewModel.LoginViewModel;
import ViewModel.PropertyOwnerViewModel;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        primaryStage.setTitle("Booking via VIA");
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint("");
        showLoginScene();
        primaryStage.show();
    }

    // ── Login screen ──────────────────────────────────────────────────────────

    private void showLoginScene() {
        LoginViewModel loginVM = new LoginViewModel();

        // Title
        Text title = new Text("Booking via VIA");
        title.setFont(Font.font("Palatino Linotype", FontWeight.BOLD, FontPosture.ITALIC, 56));
        title.setStyle("-fx-fill: #143D29;");

        Text subtitle = new Text("Select your account type to continue");
        subtitle.setFont(Font.font("Cambria", FontWeight.NORMAL, 22));
        subtitle.setStyle("-fx-fill: #1A5F3F;");

        // Radio buttons
        ToggleGroup group = new ToggleGroup();

        RadioButton rbClient = new RadioButton("Client");
        RadioButton rbOwner = new RadioButton("Property Owner");
        RadioButton rbAdmin = new RadioButton("Admin");

        for (RadioButton rb : new RadioButton[] { rbClient, rbOwner, rbAdmin }) {
            rb.setToggleGroup(group);
            rb.setFont(Font.font("Cambria", FontWeight.NORMAL, 24));
            rb.setStyle("-fx-text-fill: #143D29;");
        }
        rbClient.setSelected(true);

        group.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == rbClient)
                loginVM.setSelectedUserType("Client");
            else if (newVal == rbOwner)
                loginVM.setSelectedUserType("Property Owner");
            else
                loginVM.setSelectedUserType("Admin");
        });

        VBox radioBox = new VBox(20, rbClient, rbOwner, rbAdmin);
        radioBox.setAlignment(Pos.CENTER_LEFT);

        // Enter button
        Button enterBtn = new Button("Enter");
        enterBtn.setFont(Font.font("Cambria", FontWeight.BOLD, 22));
        enterBtn.setStyle(
                "-fx-background-color: #1A5F3F;" +
                        "-fx-text-fill: #F5F0E8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 14 48;" +
                        "-fx-cursor: hand;");
        enterBtn.setOnMouseEntered(e -> enterBtn.setStyle(
                "-fx-background-color: #143D29;" +
                        "-fx-text-fill: #F5F0E8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 14 48;" +
                        "-fx-cursor: hand;"));
        enterBtn.setOnMouseExited(e -> enterBtn.setStyle(
                "-fx-background-color: #1A5F3F;" +
                        "-fx-text-fill: #F5F0E8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 14 48;" +
                        "-fx-cursor: hand;"));
        enterBtn.setOnAction(e -> showMainScene(loginVM.getSelectedUserType()));

        // Card
        VBox card = new VBox(35, title, subtitle, radioBox, enterBtn);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(70, 85, 70, 85));
        card.setMaxWidth(700);
        card.setStyle(
                "-fx-background-color: #EDE8DC;" +
                        "-fx-background-radius: 10;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.18), 14, 0, 0, 4);");

        // Fullscreen exit hint in top-right corner
        Label exitHint = new Label("Press ESC to exit fullscreen");
        exitHint.setFont(Font.font("Cambria", FontWeight.NORMAL, 14));
        exitHint.setStyle("-fx-text-fill: #8B7355; -fx-padding: 15px;");
        StackPane.setAlignment(exitHint, Pos.TOP_RIGHT);

        // Container to maintain 9:16 aspect ratio (portrait)
        StackPane container = new StackPane(card);
        container.setMaxWidth(720);
        container.setMaxHeight(1280);
        container.setStyle("-fx-background-color: #F5F0E8;");

        StackPane loginRoot = new StackPane(container, exitHint);
        loginRoot.setStyle("-fx-background-color: #F5F0E8;");

        Scene loginScene = new Scene(loginRoot);
        loginScene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        primaryStage.setScene(loginScene);
        primaryStage.setFullScreen(true);
    }

    // ── Main screen ───────────────────────────────────────────────────────────

    private void showMainScene(String userType) {
        AppViewModel vm = switch (userType) {
            case "Property Owner" -> new PropertyOwnerViewModel();
            case "Admin" -> new AdminViewModel();
            default -> new ClientViewModel();
        };

        // ── Centre welcome text ──────────────────────────────────────
        Text welcomeText = new Text(userType);
        welcomeText.setFont(Font.font("Palatino Linotype", FontWeight.BOLD, 72));
        welcomeText.setStyle("-fx-fill: #143D29;");

        Text sectionLabel = new Text("");
        sectionLabel.setFont(Font.font("Cambria", FontWeight.NORMAL, 18));
        sectionLabel.setStyle("-fx-fill: #1A5F3F;");

        vm.currentSectionProperty().addListener((obs, oldVal, newVal) -> {
            boolean isHome = newVal.equals(userType);
            welcomeText.setText(isHome ? userType : newVal);
            welcomeText.setFont(Font.font("Palatino Linotype", FontWeight.BOLD, isHome ? 72 : 48));
            sectionLabel.setText(isHome ? "" : userType);
        });

        VBox centre = new VBox(8, welcomeText, sectionLabel);
        centre.setAlignment(Pos.CENTER);

        StackPane contentArea = new StackPane(centre);
        contentArea.setStyle("-fx-background-color: #F5F0E8;");

        // Listen to section changes and update content area
        vm.currentSectionProperty().addListener((obs, oldVal, newVal) -> {
            contentArea.getChildren().clear();
            
            // Property Owner sections
            if (vm instanceof PropertyOwnerViewModel) {
                switch (newVal) {
                    case "My Listings" -> contentArea.getChildren().add(createMyListingsView());
                    case "My Bookings" -> contentArea.getChildren().add(createMyBookingsView());
                    default -> contentArea.getChildren().add(centre);
                }
            }
            // Other user types show default centre view for now
            else {
                contentArea.getChildren().add(centre);
            }
        });

        // ── Header ───────────────────────────────────────────────────
        Text title = new Text("Booking via VIA");
        title.setFont(Font.font("Palatino Linotype", FontWeight.BOLD, FontPosture.ITALIC, 28));
        title.setStyle("-fx-fill: #143D29;");
        title.setOnMouseEntered(e -> title.setStyle("-fx-fill: #1A5F3F; -fx-cursor: hand;"));
        title.setOnMouseExited(e -> title.setStyle("-fx-fill: #143D29;"));
        title.setOnMouseClicked(e -> vm.navigateHome());

        Text headerSub = new Text("  Home");
        headerSub.setFont(Font.font("Cambria", FontWeight.NORMAL, 15));
        headerSub.setStyle("-fx-fill: #1A5F3F;");
        vm.currentSectionProperty().addListener(
                (obs, oldVal, newVal) -> headerSub.setText("  " + (newVal.equals(userType) ? "Home" : newVal)));

        HBox titleBox = new HBox(title, headerSub);
        titleBox.setAlignment(Pos.CENTER_LEFT);

        // ── Navigate dropdown ────────────────────────────────────────
        MenuButton navMenu = new MenuButton("Navigate ▾");
        navMenu.getStyleClass().add("nav-menu");
        navMenu.setStyle(
                "-fx-background-color: #1A5F3F;" +
                        "-fx-text-fill: #F5F0E8;" +
                        "-fx-font-family: 'Cambria';" +
                        "-fx-font-size: 13px;" +
                        "-fx-background-radius: 6;" +
                        "-fx-cursor: hand;");

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
        // Admin: no items yet

        // ── Back to Login button (center) ───────────────────────────
        Button backToLoginBtn = new Button("Back to Login");
        backToLoginBtn.setFont(Font.font("Cambria", FontWeight.BOLD, 13));
        backToLoginBtn.setStyle(
                "-fx-background-color: #1A5F3F;" +
                        "-fx-text-fill: #F5F0E8;" +
                        "-fx-background-radius: 6;" +
                        "-fx-padding: 6 20;" +
                        "-fx-cursor: hand;");
        backToLoginBtn.setOnMouseEntered(e -> backToLoginBtn.setStyle(
                "-fx-background-color: #143D29;" +
                        "-fx-text-fill: #F5F0E8;" +
                        "-fx-background-radius: 6;" +
                        "-fx-padding: 6 20;" +
                        "-fx-cursor: hand;"));
        backToLoginBtn.setOnMouseExited(e -> backToLoginBtn.setStyle(
                "-fx-background-color: #1A5F3F;" +
                        "-fx-text-fill: #F5F0E8;" +
                        "-fx-background-radius: 6;" +
                        "-fx-padding: 6 20;" +
                        "-fx-cursor: hand;"));
        backToLoginBtn.setOnAction(e -> showLoginScene());

        // Show/hide button based on current section (only visible on home page)
        vm.currentSectionProperty().addListener((obs, oldVal, newVal) -> {
            boolean isHome = newVal.equals(userType);
            backToLoginBtn.setVisible(isHome);
            backToLoginBtn.setManaged(isHome);
        });

        HBox centerBox = new HBox(backToLoginBtn);
        centerBox.setAlignment(Pos.CENTER);

        // Fullscreen exit hint
        Label exitHint = new Label("Press ESC to exit fullscreen");
        exitHint.setFont(Font.font("Cambria", FontWeight.NORMAL, 11));
        exitHint.setStyle("-fx-text-fill: #8B7355;");

        HBox rightBox = new HBox(12, exitHint, navMenu);
        rightBox.setAlignment(Pos.CENTER_RIGHT);

        HBox header = new HBox(titleBox, centerBox, rightBox);
        HBox.setHgrow(titleBox, Priority.ALWAYS);
        HBox.setHgrow(centerBox, Priority.ALWAYS);
        HBox.setHgrow(rightBox, Priority.ALWAYS);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(14, 20, 12, 20));
        header.setStyle(
                "-fx-background-color: #F5F0E8;" +
                        "-fx-border-color: #1A5F3F;" +
                        "-fx-border-width: 0 0 3 0;");

        VBox root = new VBox(header, contentArea);
        VBox.setVgrow(contentArea, Priority.ALWAYS);
        root.setStyle("-fx-background-color: #F5F0E8;");

        Scene mainScene = new Scene(root);
        mainScene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        primaryStage.setScene(mainScene);
        primaryStage.setFullScreen(true);
    }

    // ── Property Owner Sections ──────────────────────────────────────────────

    private VBox createMyListingsView() {
        Text title = new Text("My Listings");
        title.setFont(Font.font("Palatino Linotype", FontWeight.BOLD, 48));
        title.setStyle("-fx-fill: #143D29;");

        Text subtitle = new Text("Properties available for booking");
        subtitle.setFont(Font.font("Cambria", FontWeight.NORMAL, 16));
        subtitle.setStyle("-fx-fill: #1A5F3F;");

        // Placeholder for listings table/grid
        Label placeholder = new Label("Your available listings will appear here");
        placeholder.setFont(Font.font("Cambria", FontWeight.NORMAL, 14));
        placeholder.setStyle("-fx-text-fill: #8B7355; -fx-padding: 40px;");

        VBox content = new VBox(20, title, subtitle, placeholder);
        content.setAlignment(Pos.TOP_CENTER);
        content.setPadding(new Insets(40, 20, 20, 20));
        content.setStyle("-fx-background-color: #F5F0E8;");

        return content;
    }

    private VBox createMyBookingsView() {
        Text title = new Text("My Bookings");
        title.setFont(Font.font("Palatino Linotype", FontWeight.BOLD, 48));
        title.setStyle("-fx-fill: #143D29;");

        Text subtitle = new Text("Properties that have been booked by clients");
        subtitle.setFont(Font.font("Cambria", FontWeight.NORMAL, 16));
        subtitle.setStyle("-fx-fill: #1A5F3F;");

        // Placeholder for bookings table/grid
        Label placeholder = new Label("Bookings for your properties will appear here");
        placeholder.setFont(Font.font("Cambria", FontWeight.NORMAL, 14));
        placeholder.setStyle("-fx-text-fill: #8B7355; -fx-padding: 40px;");

        VBox content = new VBox(20, title, subtitle, placeholder);
        content.setAlignment(Pos.TOP_CENTER);
        content.setPadding(new Insets(40, 20, 20, 20));
        content.setStyle("-fx-background-color: #F5F0E8;");

        return content;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
