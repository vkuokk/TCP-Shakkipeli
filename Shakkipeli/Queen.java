package Shakkipeli;

import javafx.scene.image.Image;
public class Queen extends Piece {

    public Queen(int side, int[][] coords, int size, String name){
        setName(name);
        setSide(side);
        setCoords(coords);
        setSize(size);
        setPieceType("queen");
        setMovable(new String[]{"FORWARD","DIAGONAL","SIDEWAYS AND BACKWARDS"});
        Image img;
        //Testausta varten
        //ClassLoader cl = getClass().getClassLoader();
        if(side == 0) {
            //String imgurl = cl.getResource("assets/bq2.png").toExternalForm();
            //img = new Image(imgurl,size,size,true,true);
            //Aikaisemmin toimiva kommentoitu pois
            img = new Image(getClass().getResourceAsStream("assets/bq2.png"),size,size,true,true);
        }
        else{
            img = new Image(getClass().getResourceAsStream("assets/wq.png"),size,size,true,true);
            //String imgurl = cl.getResource("assets/wq.png").toExternalForm();
            //img = new Image(imgurl,size,size,true,true);
        }
        setIv(img);

    }




}
