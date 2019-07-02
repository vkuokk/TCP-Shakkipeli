package Shakkipeli;

import javafx.scene.image.Image;

import java.util.ArrayList;

// Ville Kuokkanen 2.7.2019
// Pawn -luokassa asetetaan sotilaalle tyypilliset ominaisuudet: puoli, liikesuunnat ja puoli

public class Pawn extends Piece{


    public Pawn(int side, int size, String name){
        setName(name);
        setSide(side);
        setSize(size);
        setPieceType("pawn");
        setMovable(new String[]{"FORWARD","DIAGONAL"});

        Image img;
        if(side == 0) {
            img = new Image(getClass().getResourceAsStream("assets/bp.png"),size,size,true,true);
        }
        else{
            img = new Image(getClass().getResourceAsStream("assets/wp.png"),size,size,true,true);
        }
        setIv(img);

    }

}
