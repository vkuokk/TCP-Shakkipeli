package Shakkipeli;

import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Queen extends Button {
    private int side;
    private int[][] coords;

    public Queen(int side, int[][] coords){
        this.side = side;
        this.coords = coords;
    }



}
