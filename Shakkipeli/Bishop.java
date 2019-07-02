package Shakkipeli;

import javafx.scene.image.Image;

// Ville Kuokkanen 2.7.2019
// Bishop -luokka perii Piecen.
// Tässä asetetaan lähetille mm. oikea kuva ja suunnat mihin voidaan liikkua

public class Bishop extends Piece {

    public Bishop(int side, int size, String name){
        setName(name);
        setSide(side);
        setSize(size);
        setPieceType("bishop");
        setMovable(new String[]{"DIAGONAL"});
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
