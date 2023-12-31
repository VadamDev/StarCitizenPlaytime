package net.vadamdev.scplaytime;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author VadamDev
 * @since 08/12/2023
 */
public class Main extends Application {
    protected static final ExecutorService asyncExecutor = Executors.newSingleThreadExecutor();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        final Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("scene.fxml"));

        primaryStage.setOnCloseRequest(event -> asyncExecutor.shutdown());
        primaryStage.setTitle("Star Citizen Playtime");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
