package Shakkipeli;

import javafx.scene.image.Image;

public class Bishop extends Piece {

    public Bishop(int side, int[][] coords, int size){
        setSide(side);
        setCoords(coords);
        setSize(size);
        Image img;
        if(side == 0) {
            img = new Image(getClass().getResourceAsStream("assets/bb.png"));
        }
        else{
            img = new Image(getClass().getResourceAsStream("assets/wb.png"));
        }
        setIv(img);

    }
}
