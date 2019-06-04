package Shakkipeli;

import javafx.scene.image.Image;

public class Bishop extends Piece {

    public Bishop(int side, int[][] coords, int size){
        setSide(side);
        setCoords(coords);
        setSize(size);
        Image img = new Image(getClass().getResourceAsStream("assets/bb.png"));
        setIv(img);

    }
}
