package Shakkipeli;

import javafx.scene.image.Image;

import java.util.ArrayList;

public class Pawn extends Piece{


    public Pawn(int side, int[][] coords, int size, String name){
        setName(name);
        setSide(side);
        setCoords(coords);
        setSize(size);
        setPieceType("pawn");
        setMovable(new String[]{"FORWARD","DIAGONAL"});

        ClassLoader cl = getClass().getClassLoader();
        Image img;
        if(side == 0) {
            String imgurl = cl.getResource("assets/bp.png").toExternalForm();
            img = new Image(imgurl,size,size,true,true);
            //img = new Image(getClass().getResourceAsStream("assets/bp.png"),size,size,true,true);
        }
        else{
            String imgurl = cl.getResource("assets/wp.png").toExternalForm();
            img = new Image(imgurl,size,size,true,true);
            //img = new Image(getClass().getResourceAsStream("assets/wp.png"),size,size,true,true);
        }
        setIv(img);

    }
/*
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

 */



}
