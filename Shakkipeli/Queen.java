package Shakkipeli;

import javafx.scene.image.Image;


// Ville Kuokkanen 2.7.2019
// Queen-luokkassa määritellään kuningattaren toiminta, kuten liikesuunnat ja kuva

public class Queen extends Piece {
    public Queen(int side, int size, String name){
        setName(name);
        setSide(side);
        setSize(size);
        setPieceType("queen");
        setMovable(new String[]{"FORWARD","DIAGONAL","SIDEWAYS AND BACKWARDS"});
        Image img;
        if(side == 0) {
            img = new Image(getClass().getResourceAsStream("assets/bq2.png"),size,size,true,true);
        }
        else{
            img = new Image(getClass().getResourceAsStream("assets/wq.png"),size,size,true,true);
        }
        setIv(img);

    }




}
