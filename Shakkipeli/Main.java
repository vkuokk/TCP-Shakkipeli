package Shakkipeli;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import Shakkipeli.Shakkicontroller;
import javafx.stage.WindowEvent;


// Ville Kuokkanen 2.7.2019
// Main -luokassa ladataan pääikkunan fxml-tiedosto mainview.fxml ja käynnistetään ohjelma
// Tässä käsitellään myös ohjelman sulkeminen

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("mainview.fxml"));
        Scene scene = new Scene(root, 800, 590);
        primaryStage.setScene(scene);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Shakkipeli");
        primaryStage.setResizable(false);
        primaryStage.show();

        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });

    }
    public static void main(String[] args) {
        launch(args);
    }
}
