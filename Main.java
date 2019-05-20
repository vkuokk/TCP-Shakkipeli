package shakkipeli;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.TextArea;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;
import shakkipeli.Shakkicontroller;
import java.io.Console;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        final FXMLLoader root = new FXMLLoader(getClass().getResource("mainview.fxml"));
        final VBox box = root.load();

        Scene scene = new Scene(box);
        Shakkicontroller shc = root.getController();
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        shc.load();
        primaryStage.setScene(scene);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Shakkipeli");
        primaryStage.setResizable(false);
        primaryStage.show();
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
