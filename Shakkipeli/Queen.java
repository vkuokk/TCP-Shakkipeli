package Shakkipeli;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;

import java.net.URISyntaxException;

public class Queen extends Piece {

    public Queen(int side, int[][] coords, int size, String name){
        setName(name);
        setSide(side);
        setCoords(coords);
        setSize(size);
        setPieceType("queen");
        Image img;
        if(side == 0) {
            img = new Image(getClass().getResourceAsStream("assets/bq2.png"),size,size,true,true);
        }
        else{
            img = new Image(getClass().getResourceAsStream("assets/wq.png"),size,size,true,true);
        }
        setIv(img);

    }




}
