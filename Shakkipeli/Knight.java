package Shakkipeli;

import javafx.scene.image.Image;

public class Knight extends Piece {
    public Knight(int side, int[][] coords, int size, String name){
        setName(name);
        setSide(side);
        setCoords(coords);
        setSize(size);
        setPieceType("knight");
        setMovable(new String[]{"KNIGHT"});
        Image img;
        //ClassLoader cl = getClass().getClassLoader();
        if(side == 0) {
            //String imgurl = cl.getResource("assets/bkn.png").toExternalForm();
            //img = new Image(imgurl,size,size,true,true);
            img = new Image(getClass().getResourceAsStream("assets/bkn.png"),size,size,true,true);
        }
        else{
            //String imgurl = cl.getResource("assets/wkn.png").toExternalForm();
            //img = new Image(imgurl,size,size,true,true);
            img = new Image(getClass().getResourceAsStream("assets/wkn.png"),size,size,true,true);
        }
        setIv(img);

    }

}
