package SEP2.SEP2.src.View;

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
        showLoginScene();
        primaryStage.show();
    }

    // ── Login screen ──────────────────────────────────────────────────────────

    private void showLoginScene() {
        LoginViewModel loginVM = new LoginViewModel();

        // Title
        Text title = new Text("Booking via VIA");
        title.setFont(Font.font("Palatino Linotype", FontWeight.BOLD, FontPosture.ITALIC, 32));
        title.setStyle("-fx-fill: #143D29;");

        Text subtitle = new Text("Select your account type to continue");
        subtitle.setFont(Font.font("Cambria", FontWeight.NORMAL, 14));
        subtitle.setStyle("-fx-fill: #1A5F3F;");

        // Radio buttons
        ToggleGroup group = new ToggleGroup();

        RadioButton rbClient = new RadioButton("Client");
        RadioButton rbOwner = new RadioButton("Property Owner");
        RadioButton rbAdmin = new RadioButton("Admin");

        for (RadioButton rb : new RadioButton[] { rbClient, rbOwner, rbAdmin }) {
            rb.setToggleGroup(group);
            rb.setFont(Font.font("Cambria", FontWeight.NORMAL, 15));
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

        VBox radioBox = new VBox(12, rbClient, rbOwner, rbAdmin);
        radioBox.setAlignment(Pos.CENTER_LEFT);

        // Enter button
        Button enterBtn = new Button("Enter");
        enterBtn.setFont(Font.font("Cambria", FontWeight.BOLD, 14));
        enterBtn.setStyle(
                "-fx-background-color: #1A5F3F;" +
                        "-fx-text-fill: #F5F0E8;" +
                        "-fx-background-radius: 6;" +
                        "-fx-padding: 8 28;" +
                        "-fx-cursor: hand;");
        enterBtn.setOnMouseEntered(e -> enterBtn.setStyle(
                "-fx-background-color: #143D29;" +
                        "-fx-text-fill: #F5F0E8;" +
                        "-fx-background-radius: 6;" +
                        "-fx-padding: 8 28;" +
                        "-fx-cursor: hand;"));
        enterBtn.setOnMouseExited(e -> enterBtn.setStyle(
                "-fx-background-color: #1A5F3F;" +
                        "-fx-text-fill: #F5F0E8;" +
                        "-fx-background-radius: 6;" +
                        "-fx-padding: 8 28;" +
                        "-fx-cursor: hand;"));
        enterBtn.setOnAction(e -> showMainScene(loginVM.getSelectedUserType()));

        // Card
        VBox card = new VBox(20, title, subtitle, radioBox, enterBtn);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(40, 48, 40, 48));
        card.setMaxWidth(420);
        card.setStyle(
                "-fx-background-color: #EDE8DC;" +
                        "-fx-background-radius: 10;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.18), 14, 0, 0, 4);");

        StackPane loginRoot = new StackPane(card);
        loginRoot.setStyle("-fx-background-color: #F5F0E8;");

        Scene loginScene = new Scene(loginRoot, 960, 620);
        loginScene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        primaryStage.setScene(loginScene);
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
        }
        // Property Owner and Admin: no items yet

        HBox rightBox = new HBox(navMenu);
        rightBox.setAlignment(Pos.CENTER_RIGHT);

        HBox header = new HBox(titleBox, rightBox);
        HBox.setHgrow(titleBox, Priority.ALWAYS);
        HBox.setHgrow(rightBox, Priority.NEVER);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(14, 20, 12, 20));
        header.setStyle(
                "-fx-background-color: #F5F0E8;" +
                        "-fx-border-color: #1A5F3F;" +
                        "-fx-border-width: 0 0 3 0;");

        VBox root = new VBox(header, contentArea);
        VBox.setVgrow(contentArea, Priority.ALWAYS);
        root.setStyle("-fx-background-color: #F5F0E8;");

        Scene mainScene = new Scene(root, 960, 620);
        mainScene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        primaryStage.setScene(mainScene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
