package Shakkipeli;

import javafx.scene.image.Image;

public class Knight extends Piece {
    public Knight(int side, int[][] coords, int size){
        setSide(side);
        setCoords(coords);
        setSize(size);
        Image img = new Image(getClass().getResourceAsStream("assets/bkn.png"));
        setIv(img);

    }
}
