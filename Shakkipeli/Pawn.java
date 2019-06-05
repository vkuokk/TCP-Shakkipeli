package Shakkipeli;

import javafx.scene.image.Image;

public class Pawn extends Piece{
    public Pawn(int side, int[][] coords, int size){
        setSide(side);
        setCoords(coords);
        setSize(size);
        Image img = new Image(getClass().getResourceAsStream("assets/bp.png"));
        setIv(img);

    }
}
