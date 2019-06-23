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

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("mainview.fxml"));
        //        //final VBox box = (VBox)root.;

        Scene scene = new Scene(root, 800, 590);
        //Shakkicontroller shc = scene.getRoot();
        //scene.getStylesheets().add(getClass().getResource("Shakkicss.css").toExternalForm());
        //shc.load();
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



/*
        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println(newVal);
            int newSize = newVal.intValue();
            primaryStage.setWidth(newSize);
            primaryStage.setHeight(newSize);
        });

        primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println(newVal);
            int newSize = newVal.intValue();
            primaryStage.setWidth(newSize);
            primaryStage.setHeight(newSize);
        });
*/
    public static void main(String[] args) {
        launch(args);
    }
}
