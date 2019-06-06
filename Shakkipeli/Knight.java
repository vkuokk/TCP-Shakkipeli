package Shakkipeli;

import javafx.scene.image.Image;

public class Knight extends Piece {
    public Knight(int side, int[][] coords, int size){
        setSide(side);
        setCoords(coords);
        setSize(size);
        Image img;
        if(side == 0) {
            img = new Image(getClass().getResourceAsStream("assets/bkn.png"),size,size,true,true);
        }
        else{
            img = new Image(getClass().getResourceAsStream("assets/wkn.png"),size,size,true,true);
        }
        setIv(img);

    }
}
