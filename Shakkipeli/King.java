package Shakkipeli;

import javafx.scene.image.Image;

public class King extends Piece {
    public King(int side, int[][] coords, int size){
        setSide(side);
        setCoords(coords);
        setSize(size);
        Image img = new Image(getClass().getResourceAsStream("assets/bk.png"));
        setIv(img);

    }
}
