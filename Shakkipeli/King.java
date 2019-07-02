package Shakkipeli;

import javafx.scene.image.Image;

// Ville Kuokkanen 2.7.2019
// King -luokassa asetetaan muiden nappuloiden kaltaisesti oikea kuva, koordinaatit, tyyppi ja muut kuninkaalle kuuluvat
// ominaisuudet
// Kuninkaan olemuksesta (tornitus) johtuen tälle on olemassa kokonaan oma liiketyyppinsä.
public class King extends Piece {
    public King(int side, int size, String name){
        setName(name);
        setSide(side);
        setSize(size);
        setPieceType("king");
        setMovable(new String[]{"KING"});
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
