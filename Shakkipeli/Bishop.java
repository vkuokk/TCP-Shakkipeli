package Shakkipeli;

import javafx.scene.image.Image;

public class Bishop extends Piece {

    public Bishop(int side, int[][] coords, int size, String name){
        setName(name);
        setSide(side);
        setCoords(coords);
        setSize(size);
        Image img;
        if(side == 0) {
            img = new Image(getClass().getResourceAsStream("assets/bb.png"),size,size,true,true);
        }
        else{
            img = new Image(getClass().getResourceAsStream("assets/wb.png"),size,size,true,true);

        }
        setIv(img);

    }
}
