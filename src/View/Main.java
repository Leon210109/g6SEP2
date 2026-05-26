package View;

import Model.ClientModelManager;
import Network.Server;
import Network.SocketClient;
import ViewModel.ViewModelFactory;
import com.google.gson.Gson;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Start the server in a background daemon thread
        Thread serverThread = new Thread(() -> new Server().startServer());
        serverThread.setDaemon(true);
        serverThread.start();

        // Give the server a moment to open the ServerSocket before connecting
        Thread.sleep(500);

        // Initialise the client-server stack
        Gson gson = new Gson();
        SocketClient socketClient = new SocketClient(gson);
        ClientModelManager clientModelManager = new ClientModelManager(socketClient);
        ViewModelFactory.init(clientModelManager);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/LoginView.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/View/styles.css").toExternalForm());

        stage.setTitle("Booking via VIA");
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
