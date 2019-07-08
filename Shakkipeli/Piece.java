package Shakkipeli;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

// Ville Kuokkanen 2.7.2019
// Nappula-luokka peritään edelleen nappulasta, koska nappulaan on helppo lisätä siirtäminen ja painalluksien käsittely javafx:ssä
// Ominaisuuksia ovat mm. koordinaattien asettaminen ja saaminen, korostuksen lisääminen ja poistaminen sekä kuvan koon
// määrittäminen.

public class Piece extends Button {
    private int side;
    private int X;
    private int Y;
    private int size;
    private ImageView iv;
    private String name;
    private String pieceType;
    private boolean hasMoved = false;
    private String[] movable;

    public Piece(){
        this.setPickOnBounds(true);
        this.setStyle("-fx-background-color: transparent;");
    }

    public void setName(String name){
        this.name = name;
    }
    public void setSide(int side) {
        this.side = side;
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


    public Piece(int side){
        this.side = side;
        this.setPickOnBounds(true);
    }

    public void removeHighlight(){
        iv.setStyle(null);
    }
    public void setHighlight(){
        iv.setStyle("-fx-effect: dropshadow(gaussian, #ea2a15, 2, 1.0, 0, 0);");
    }
    public void setX(int x){
        this.X = x;
    }
    public void setY(int y){
        this.Y = y;
    }
    public int getX(){
        return this.X;
    }
    public int getY(){
        return this.Y;
    }
    public void setPieceType(String s){this.pieceType = s;}
    public void setMovable(String[] s){
        this.movable = s;
    }

    public String getPieceType(){
        return this.pieceType;
    }

    public boolean gethasMoved(){
        return this.hasMoved;
    }
    public void setHasMoved(){
        this.hasMoved = true;
    }
    public String[] getMoves(){
        return this.movable;
    }
}



