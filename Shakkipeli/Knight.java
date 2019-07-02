package Shakkipeli;

import javafx.scene.image.Image;

// Ville Kuokkanen 2.7.2019
// Knight -luokassa asetetaan ratsulle sille kuuluvat ominaisuudet kuten kuva ja liiketyyppi
public class Knight extends Piece {
    public Knight(int side, int size, String name){
        setName(name);
        setSide(side);
        setSize(size);
        setPieceType("knight");
        setMovable(new String[]{"KNIGHT"});
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
