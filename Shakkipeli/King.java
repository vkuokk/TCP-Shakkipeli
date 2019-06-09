package Shakkipeli;

import javafx.scene.image.Image;

public class King extends Piece {
    public King(int side, int[][] coords, int size, String name){
        setName(name);
        setSide(side);
        setCoords(coords);
        setSize(size);
        Image img;
        if(side == 0) {
            img = new Image(getClass().getResourceAsStream("assets/bk.png"),size,size,true,true);
        }
        else{
            img = new Image(getClass().getResourceAsStream("assets/wk.png"),size,size,true,true);
        }


        setIv(img);


    }
}
