package Tests;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.*;

public class MainFx extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/AfficherNote.fxml"));

        // hello
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Add User");
        stage.show();
    }

}

