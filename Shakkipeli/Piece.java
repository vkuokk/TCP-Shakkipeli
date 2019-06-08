package Shakkipeli;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Piece extends Button {
    private int side;
    private int[][] coords;
    private int size;
    private ImageView iv;
    private String name;

    public Piece(){
        this.setPickOnBounds(true);
        this.setStyle("-fx-background-color: transparent;");
    };

    public void setName(String name){
        this.name = name;
    }
    public void setSide(int side) {
        this.side = side;
    }
    public void setCoords(int[][] coords){
        this.coords = coords;
    }
    public void setSize(int size){
        this.size = size;
    }
    public void setIv(Image img){
        iv = new ImageView(img);
        iv.setFitHeight(size-5);
        iv.setFitWidth(size-15);
        iv.setPreserveRatio(true);
        this.setGraphic(iv);

    }

    public String getName(){
        return this.name;
    }


    public Piece(int side, int[][]coords , int size){
        this.side = side;
        this.coords = coords;
        this.setPickOnBounds(true);
    }
}
