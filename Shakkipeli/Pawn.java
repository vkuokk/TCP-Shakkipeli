package Shakkipeli;

import javafx.scene.image.Image;

public class Pawn extends Piece{
    private boolean hasMoved = false;

    public Pawn(int side, int[][] coords, int size, String name){
        setName(name);
        setSide(side);
        setCoords(coords);
        setSize(size);
        Image img;
        if(side == 0) {
            img = new Image(getClass().getResourceAsStream("assets/bp.png"),size,size,true,true);
        }
        else{
            img = new Image(getClass().getResourceAsStream("assets/wp.png"),size,size,true,true);
        }
        setIv(img);

    }

    public boolean validate(int X, int Y){
        if(!hasMoved && this.getY()-Y == 2){
            hasMoved = true;
            return true;
        }
        if(this.getY()-Y == 1){
            hasMoved = true;
            return true;
        }
        else return false;

    }
}
