package Shakkipeli;

import javafx.scene.image.Image;

public class Pawn extends Piece{
    public Pawn(int side, int[][] coords, int size){
        setSide(side);
        setCoords(coords);
        setSize(size);
        Image img;
        if(side == 0) {
            img = new Image(getClass().getResourceAsStream("assets/bp.png"));
        }
        else{
            img = new Image(getClass().getResourceAsStream("assets/wp.png"));
        }
        setIv(img);

    }
}
