package Shakkipeli;

import javafx.scene.image.Image;

public class King extends Piece {
    public King(int side, int[][] coords, int size, String name){
        setName(name);
        setSide(side);
        setCoords(coords);
        setSize(size);
        setPieceType("king");
        setMovable(new String[]{"KING"});
        Image img;
        //ClassLoader cl = getClass().getClassLoader();
        if(side == 0) {
            //String imgurl = cl.getResource("assets/bk.png").toExternalForm();
            //img = new Image(imgurl,size,size,true,true);
            img = new Image(getClass().getResourceAsStream("assets/bk.png"),size,size,true,true);
        }
        else{
            //String imgurl = cl.getResource("assets/wk.png").toExternalForm();
            //img = new Image(imgurl,size,size,true,true);
            img = new Image(getClass().getResourceAsStream("assets/wk.png"),size,size,true,true);
        }

        setIv(img);


    }

}
