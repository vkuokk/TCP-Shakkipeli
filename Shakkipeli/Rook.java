package Shakkipeli;

import javafx.scene.image.Image;

import java.util.HashMap;
//luokka tornille, koordinaatit lukuparina

public class Rook extends Piece{
    public Rook(int side, int[][] coords, int size, String name){
        setName(name);
        setSide(side);
        setCoords(coords);
        setSize(size);
        setPieceType("rook");
        setMovable(new String[]{"FORWARD","SIDEWAYS AND BACKWARDS"});
        Image img;
        //ClassLoader cl = getClass().getClassLoader();
        if(side == 0) {
            //String imgurl = cl.getResource("assets/br.png").toExternalForm();
            //img = new Image(imgurl,size,size,true,true);
            img = new Image(getClass().getResourceAsStream("assets/br.png"),size,size,true,true);
        }
        else{
            //String imgurl = cl.getResource("assets/wr.png").toExternalForm();
            //img = new Image(imgurl,size,size,true,true);
            img = new Image(getClass().getResourceAsStream("assets/wr.png"),size,size,true,true);
        }
        setIv(img);

    }
}
