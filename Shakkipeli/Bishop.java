package Shakkipeli;

import javafx.scene.image.Image;

public class Bishop extends Piece {

    public Bishop(int side, int[][] coords, int size, String name){
        setName(name);
        setSide(side);
        setCoords(coords);
        setSize(size);
        setPieceType("bishop");
        setMovable(new String[]{"DIAGONAL"});
        Image img;
        //ClassLoader cl = getClass().getClassLoader();
        if(side == 0) {
            //String imgurl = cl.getResource("assets/bb.png").toExternalForm();
            //img = new Image(imgurl,size,size,true,true);
            img = new Image(getClass().getResourceAsStream("assets/bb.png"),size,size,true,true);
        }
        else{
            //String imgurl = cl.getResource("assets/wb.png").toExternalForm();
            //img = new Image(imgurl,size,size,true,true);

            img = new Image(getClass().getResourceAsStream("assets/wb.png"),size,size,true,true);

        }
        setIv(img);

    }

}
