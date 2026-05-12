package SEP2.SEP2.src.View;

import SEP2.SEP2.src.ViewModel.AdminViewModel;
import SEP2.SEP2.src.ViewModel.AppViewModel;
import SEP2.SEP2.src.ViewModel.ClientViewModel;
import SEP2.SEP2.src.ViewModel.LoginViewModel;
import SEP2.SEP2.src.ViewModel.PropertyOwnerViewModel;
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
import java.io.File;

public class Main extends Application {
  private Stage primaryStage;
  private final LoginViewModel loginVM = new LoginViewModel();
  private static final String CSS_URI = new File("src/View/styles.css").toURI().toString();

  @Override
  public void start(Stage primaryStage) {
    this.primaryStage = primaryStage;
    primaryStage.setTitle("Booking via VIA");
    primaryStage.setScene(buildLoginScene());
    primaryStage.show();
  }

  // ── Login Scene ──────────────────────────────────────────────────────────
  private Scene buildLoginScene() {
    Text title = new Text("Booking via VIA");
    title.setFont(Font.font("Palatino Linotype", FontWeight.BOLD, FontPosture.ITALIC, 36));
    title.setStyle("-fx-fill: #143D29;");

    Text subtitle = new Text("Select your account type to continue");
    subtitle.setFont(Font.font("Cambria", FontWeight.NORMAL, 15));
    subtitle.setStyle("-fx-fill: #1A5F3F;");

    ToggleGroup group = new ToggleGroup();

    RadioButton clientBtn = new RadioButton("Client");
    RadioButton ownerBtn = new RadioButton("Property Owner");
    RadioButton adminBtn = new RadioButton("Admin");

    for (RadioButton rb : new RadioButton[] { clientBtn, ownerBtn, adminBtn }) {
      rb.setToggleGroup(group);
      rb.setFont(Font.font("Cambria", FontWeight.NORMAL, 15));
      rb.setStyle("-fx-text-fill: #143D29;");
    }
    clientBtn.setSelected(true);

    group.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
      if (newVal instanceof RadioButton rb)
        loginVM.setSelectedUserType(rb.getText());
    });

    VBox radioBox = new VBox(12, clientBtn, ownerBtn, adminBtn);
    radioBox.setAlignment(Pos.CENTER_LEFT);
    radioBox.setPadding(new Insets(8, 0, 8, 0));

    String btnStyle = "-fx-background-color: #1A5F3F; -fx-text-fill: #F5F0E8;" +
        "-fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 8 28;";
    String btnHoverStyle = "-fx-background-color: #143D29; -fx-text-fill: #F5F0E8;" +
        "-fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 8 28;";

    Button enterBtn = new Button("Enter");
    enterBtn.setFont(Font.font("Cambria", FontWeight.BOLD, 15));
    enterBtn.setStyle(btnStyle);
    enterBtn.setOnMouseEntered(e -> enterBtn.setStyle(btnHoverStyle));
    enterBtn.setOnMouseExited(e -> enterBtn.setStyle(btnStyle));
    enterBtn.setOnAction(e -> primaryStage.setScene(buildMainScene(loginVM.getSelectedUserType())));

    VBox card = new VBox(18, title, subtitle, radioBox, enterBtn);
    card.setAlignment(Pos.CENTER_LEFT);
    card.setPadding(new Insets(40, 48, 40, 48));
    card.setMaxWidth(440);
    card.setStyle(
        "-fx-background-color: #EDE8DC; -fx-background-radius: 12;" +
            "-fx-border-color: #1A5F3F; -fx-border-radius: 12; -fx-border-width: 2;");

    StackPane loginRoot = new StackPane(card);
    loginRoot.setStyle("-fx-background-color: #F5F0E8;");

    Scene scene = new Scene(loginRoot, 960, 620);
    scene.getStylesheets().add(CSS_URI);
    return scene;
  }

  // ── Main Scene ───────────────────────────────────────────────────────────
  private Scene buildMainScene(String userType) {
    AppViewModel vm = switch (userType) {
      case "Property Owner" -> new PropertyOwnerViewModel();
      case "Admin" -> new AdminViewModel();
      default -> new ClientViewModel();
    };

    // ── Big centred label ────────────────────────────────────────
    Text welcomeText = new Text(userType);
    welcomeText.setFont(Font.font("Palatino Linotype", FontWeight.BOLD, FontPosture.ITALIC, 72));
    welcomeText.setStyle("-fx-fill: #143D29;");

    Text sectionLabel = new Text("  " + userType);
    sectionLabel.setFont(Font.font("Cambria", FontWeight.NORMAL, 15));
    sectionLabel.setStyle("-fx-fill: #1A5F3F;");

    // Bind UI to ViewModel – any navigateTo / navigateHome call updates both texts
    vm.currentSectionProperty().addListener((obs, oldVal, newVal) -> {
      welcomeText.setText(newVal);
      boolean isHome = newVal.equals(userType);
      welcomeText.setFont(Font.font("Palatino Linotype", FontWeight.BOLD, FontPosture.ITALIC,
          isHome ? 72 : 52));
      sectionLabel.setText("  " + newVal);
    });

    StackPane contentArea = new StackPane(welcomeText);
    contentArea.setStyle("-fx-background-color: #F5F0E8;");
    contentArea.setPadding(new Insets(16));

    // ── Title (home button) ──────────────────────────────────────
    Text title = new Text("Booking via VIA");
    title.setFont(Font.font("Palatino Linotype", FontWeight.BOLD, FontPosture.ITALIC, 28));
    title.setStyle("-fx-fill: #143D29;");
    title.setOnMouseEntered(e -> title.setStyle("-fx-fill: #1A5F3F; -fx-cursor: hand;"));
    title.setOnMouseExited(e -> title.setStyle("-fx-fill: #143D29;"));
    title.setOnMouseClicked(e -> vm.navigateHome());

    HBox titleBox = new HBox(title, sectionLabel);
    titleBox.setAlignment(Pos.CENTER_LEFT);

    // ── Dropdown menu ─────────────────────────────────────────────
    MenuButton navMenu = new MenuButton("Navigate ▾");
    navMenu.getStyleClass().add("nav-menu");
    navMenu.setStyle(
        "-fx-background-color: #1A5F3F; -fx-text-fill: #F5F0E8;" +
            "-fx-font-family: 'Cambria'; -fx-font-size: 13px;" +
            "-fx-background-radius: 6; -fx-cursor: hand;");

    if (vm instanceof ClientViewModel clientVM) {
      for (String section : new String[] { "Bookings", "Available Listings" }) {
        MenuItem item = new MenuItem(section);
        item.setStyle("-fx-font-family: 'Cambria'; -fx-font-size: 13px;");
        item.setOnAction(ev -> clientVM.navigateTo(section));
        navMenu.getItems().add(item);
      }
    }
    // PropertyOwnerViewModel and AdminViewModel: no menu items yet

    HBox rightBox = new HBox(8, navMenu);
    rightBox.setAlignment(Pos.CENTER_RIGHT);

    // ── Header bar ────────────────────────────────────────────────
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

    Scene scene = new Scene(root, 960, 620);
    scene.getStylesheets().add(CSS_URI);
    return scene;
  }

  public static void main(String[] args) {
    launch(args);
  }
}

