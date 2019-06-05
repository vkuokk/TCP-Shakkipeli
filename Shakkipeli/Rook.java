package Shakkipeli;

import javafx.scene.image.Image;

import java.util.HashMap;
//luokka tornille, koordinaatit lukuparina

public class Rook extends Piece{
    public Rook(int side, int[][] coords, int size){
        setSide(side);
        setCoords(coords);
        setSize(size);
        Image img;
        if(side == 0) {
            img = new Image(getClass().getResourceAsStream("assets/br.png"));
        }
        else{
            img = new Image(getClass().getResourceAsStream("assets/wr.png"));
        }
        setIv(img);

    }
}
