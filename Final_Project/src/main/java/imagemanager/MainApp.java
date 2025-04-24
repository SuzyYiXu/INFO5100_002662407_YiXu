// This is the main application class for the Image Management Tool.
package imagemanager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

// Below initializes the JavaFX application and loads the main view.
//The main view is defined in the FXML file located at /imagemanager/main_view.fxml.
public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/imagemanager/main_view.fxml"));
        Scene scene = new Scene(loader.load());
        primaryStage.setTitle("Image Management Tool");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
