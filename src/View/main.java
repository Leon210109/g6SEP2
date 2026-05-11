package View;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class main extends Application
{
    @Override
    public void start(Stage primaryStage)
    {
        TabPane tabPane = new TabPane();

        tabPane.getTabs().addAll(
            createTab("Users",         new String[]{"First Name", "Last Name", "Email", "Phone", "Username", "DOB"}),
            createTab("Property Owners", new String[]{"First Name", "Last Name", "Email", "Username", "Listings"}),
            createTab("Admins",        new String[]{"First Name", "Last Name", "Email", "Username"}),
            createTab("Listings",      new String[]{"ID", "Title", "Price", "Address", "Owner"}),
            createTab("Bookings",      new String[]{"ID", "Listing", "Guest", "Start Date", "End Date"}),
            createTab("Addresses",     new String[]{"Street", "City", "Zip Code", "Country"}),
            createTab("Dates",         new String[]{"Day", "Month", "Year"})
        );

        VBox root = new VBox(tabPane);
        Scene scene = new Scene(root, 900, 600);

        primaryStage.setTitle("SEP2 - Data Viewer");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Tab createTab(String name, String[] columns)
    {
        TableView<Object> table = new TableView<>();
        table.setPlaceholder(new Label("No data yet"));

        for (String col : columns)
        {
            TableColumn<Object, String> column = new TableColumn<>(col);
            column.setPrefWidth(130);
            table.getColumns().add(column);
        }

        Tab tab = new Tab(name, table);
        tab.setClosable(false);
        return tab;
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
