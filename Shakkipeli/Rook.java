package Shakkipeli;

import javafx.scene.image.Image;

// Ville Kuokkanen 2.7.2019
// Rook -luokka kuvaa tornin yleispiirteet

public class Rook extends Piece{
    public Rook(int side, int size, String name){
        setName(name);
        setSide(side);
        setSize(size);
        setPieceType("rook");
        setMovable(new String[]{"FORWARD","SIDEWAYS AND BACKWARDS"});
        Image img;
        if(side == 0) {
            img = new Image(getClass().getResourceAsStream("assets/br.png"),size,size,true,true);
        }
        else{
            img = new Image(getClass().getResourceAsStream("assets/wr.png"),size,size,true,true);
        }
        setIv(img);

    }
}
