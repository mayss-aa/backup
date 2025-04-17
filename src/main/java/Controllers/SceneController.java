package Controllers;

import com.sun.net.httpserver.HttpServer;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SceneController {
    private static Stage primaryStage;
    private static List<HttpServer> httpServers = new ArrayList<>();

    // Call this first in your main class
    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static void loadSignupScene() throws IOException {
        Parent root = FXMLLoader.load(SceneController.class.getResource("/add.fxml"));
        Platform.runLater( () -> root.requestFocus() );
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Sign Up");
    }

    public static void loadLoginScene() throws IOException {
        Parent root = FXMLLoader.load(SceneController.class.getResource("/login.fxml"));
        Platform.runLater( () -> root.requestFocus() );
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("LoginController");
    }

    public static void loadProfilePageScene() throws IOException {
        Parent root = FXMLLoader.load(SceneController.class.getResource("/Profile.fxml"));
        Platform.runLater( () -> root.requestFocus() );
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Profile Page");
    }

    public static void loadProfileEditScene() throws IOException {
        Parent root = FXMLLoader.load(SceneController.class.getResource("/EditProfile.fxml"));
        Platform.runLater( () -> root.requestFocus() );
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Profile Page");
    }
    public static void loadAdressScene() throws IOException {
        Parent root = FXMLLoader.load(SceneController.class.getResource("/Adress" +
                ".fxml"));
        Platform.runLater( () -> root.requestFocus() );
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Profile Page");
    }

    public static void loadBackToProfileScene() throws IOException {
        Parent root = FXMLLoader.load(SceneController.class.getResource("/Profile.fxml"));
        Platform.runLater( () -> root.requestFocus() );
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Profile Page");
    }

    public static void loadNextToProfileScene() throws IOException {
        Parent root = FXMLLoader.load(SceneController.class.getResource("/AdressSingup.fxml"));
        Platform.runLater( () -> root.requestFocus() );
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Profile Page");
    }

    public static void loadForgotPasswordScene() throws IOException {
        Parent root = FXMLLoader.load(SceneController.class.getResource("/ForgotPassword.fxml"));
        Platform.runLater( () -> root.requestFocus() );
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Profile Page");
    }
    public static void loadAdminDashScene() throws IOException {
        Parent root = FXMLLoader.load(SceneController.class.getResource("/AdminDash.fxml"));
        Platform.runLater( () -> root.requestFocus() );
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Profile Page");
    }



   /* public static void loadPage(String page) throws IOException {
        Parent root = FXMLLoader.load(SceneController.class.getResource(page));
        Platform.runLater( () -> root.requestFocus() );
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Profile Page");
    }*/

    public static void registerHttpServer(HttpServer server) {
        httpServers.add(server);
    }

    public static void stopAllHttpServers() {
        for (HttpServer server : httpServers) {
            if (server != null) {
                server.stop(0); // Stop immediately
                System.out.println("✅ HTTP Server stopped.");
            }
        }
        httpServers.clear();
    }
    public static void loadPage(String fxmlPath) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(SceneController.class.getResource(fxmlPath)));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
}