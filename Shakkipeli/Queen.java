package Shakkipeli;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;

import java.net.URISyntaxException;

public class Queen extends Button {
    private int side;
    private int[][] coords;
    private int size;

    public Queen(int side, int[][] coords, int size){
        this.side = side;
        this.coords = coords;
        //this.getStyleClass().add("icon_b_q");
        this.setStyle("-fx-background-color: #FFF888;");
        this.setPickOnBounds(true);
        //this.setHeight(size);
        //this.setWidth(size);


        Image img = new Image(getClass().getResourceAsStream("assets/bq2.png"));
        ImageView imgv = new ImageView(img);
        imgv.setFitHeight(size-5);
        imgv.setFitWidth(size-15);
        imgv.setPreserveRatio(true);
        this.setGraphic(imgv);



        //Image img = new Image("assets/bq.png");
        //this.setGraphic(new ImageView(new Image("assets/bq.png")));

    }




}
