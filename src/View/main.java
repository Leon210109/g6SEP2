package SEP2.SEP2.src.View;

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

import java.util.LinkedHashMap;
import java.util.Map;

public class main extends Application
{
    private StackPane contentArea;
    private Text navLabel;

    @Override
    public void start(Stage primaryStage)
    {
        // ── Content panels ──────────────────────────────────────────
        Map<String, TableView<Object>> views = new LinkedHashMap<>();
        views.put("Home",            createTable(new String[]{}));
        views.put("Users",           createTable(new String[]{"First Name", "Last Name", "Email", "Phone", "Username", "DOB"}));
        views.put("Property Owners", createTable(new String[]{"First Name", "Last Name", "Email", "Username", "Listings"}));
        views.put("Admins",          createTable(new String[]{"First Name", "Last Name", "Email", "Username"}));
        views.put("Listings",        createTable(new String[]{"ID", "Title", "Price", "Address", "Owner"}));
        views.put("Bookings",        createTable(new String[]{"ID", "Listing", "Guest", "Start Date", "End Date"}));
        views.put("Addresses",       createTable(new String[]{"Street", "City", "Zip Code", "Country"}));
        views.put("Dates",           createTable(new String[]{"Day", "Month", "Year"}));

        contentArea = new StackPane();
        contentArea.setStyle("-fx-background-color: #F5F0E8;");
        contentArea.setPadding(new Insets(16));

        // Add all tables, only first visible
        boolean first = true;
        for (TableView<Object> tv : views.values())
        {
            tv.setVisible(first);
            tv.setManaged(first);
            contentArea.getChildren().add(tv);
            first = false;
        }

        // ── Title (home button) ──────────────────────────────────────
        Text title = new Text("Booking via VIA");
        title.setFont(Font.font("Palatino Linotype", FontWeight.BOLD, FontPosture.ITALIC, 28));
        title.setStyle("-fx-fill: #143D29;");
        title.setOnMouseEntered(e -> title.setStyle("-fx-fill: #1A5F3F; -fx-cursor: hand;"));
        title.setOnMouseExited(e  -> title.setStyle("-fx-fill: #143D29;"));
        title.setOnMouseClicked(e -> showView(views, "Home"));

        Text subtitle = new Text("  Home page");
        subtitle.setFont(Font.font("Cambria", FontWeight.NORMAL, 15));
        subtitle.setStyle("-fx-fill: #1A5F3F;");

        HBox titleBox = new HBox(title, subtitle);
        titleBox.setAlignment(Pos.CENTER_LEFT);

        // ── Navigation label (shows current section) ────────────────
        navLabel = new Text("Home");
        navLabel.setFont(Font.font("Cambria", FontWeight.NORMAL, 13));
        navLabel.setStyle("-fx-fill: #1A5F3F;");

        // ── Dropdown menu ────────────────────────────────────────────
        MenuButton navMenu = new MenuButton("Navigate ▾");
        navMenu.setStyle(
            "-fx-background-color: #1A5F3F;" +
            "-fx-text-fill: #F5F0E8;" +
            "-fx-font-family: 'Cambria';" +
            "-fx-font-size: 13px;" +
            "-fx-background-radius: 6;" +
            "-fx-cursor: hand;"
        );

        String[] sections = {"Users", "Property Owners", "Admins", "Listings", "Bookings", "Addresses", "Dates"};
        for (String section : sections)
        {
            MenuItem item = new MenuItem(section);
            item.setStyle("-fx-font-family: 'Cambria'; -fx-font-size: 13px;");
            item.setOnAction(e -> showView(views, section));
            navMenu.getItems().add(item);
        }

        HBox rightBox = new HBox(8, navLabel, navMenu);
        rightBox.setAlignment(Pos.CENTER_RIGHT);

        // ── Header bar ───────────────────────────────────────────────
        HBox header = new HBox(titleBox, rightBox);
        HBox.setHgrow(titleBox, Priority.ALWAYS);
        HBox.setHgrow(rightBox, Priority.NEVER);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(14, 20, 12, 20));
        header.setStyle(
            "-fx-background-color: #F5F0E8;" +
            "-fx-border-color: #1A5F3F;" +
            "-fx-border-width: 0 0 3 0;"
        );

        // ── Root layout ──────────────────────────────────────────────
        VBox root = new VBox(header, contentArea);
        VBox.setVgrow(contentArea, Priority.ALWAYS);
        root.setStyle("-fx-background-color: #F5F0E8;");

        Scene scene = new Scene(root, 960, 620);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        primaryStage.setTitle("Booking via VIA - Data Viewer");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showView(Map<String, TableView<Object>> views, String name)
    {
        for (Map.Entry<String, TableView<Object>> entry : views.entrySet())
        {
            boolean show = entry.getKey().equals(name);
            entry.getValue().setVisible(show);
            entry.getValue().setManaged(show);
        }
        navLabel.setText(name);
    }

    private TableView<Object> createTable(String[] columns)
    {
        TableView<Object> table = new TableView<>();
        table.setPlaceholder(new Label("No data yet"));

        for (String col : columns)
        {
            TableColumn<Object, String> column = new TableColumn<>(col);
            column.setPrefWidth(140);
            table.getColumns().add(column);
        }
        return table;
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}

