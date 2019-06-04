package Shakkipeli;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private GridPane cb;
    private int sd;
    //@FXML
    //private Queen queen;
    private Piece currentpiece;

    int[][] spaces = new int[7][7];
    //
    private double mouseX, mouseY;
    private double oldX, oldY;
    private double newTranslateX;
    private double newTranslateY;

    //puolet 0 = musta, 1 = valkoinen
    public Game(GridPane newChessBoard, int puoli) {

        cb = newChessBoard;
        sd = puoli;

    }

    //Luodaan nappulat pelilaudalle
    public void spawn() {


        //############################################
        //TESTATAAN JOS GRIDPANEEN LAITTAA PANEN JA SEN SISÄLLE NAPIN
        Pane pane1 = new Pane();
        Pane pane2 = new Pane();

        //setListener(pane1);
        //setListener(pane2);
        pane1.setStyle("-fx-background-color: #FFF888;");
        pane2.setStyle("-fx-background-color: #FFF888;");
        int size = cb.heightProperty().intValue() / 8;


        Queen q = new Queen(sd, spaces, size);
        Bishop b = new Bishop(sd,spaces,size);

        // currentpiece = q;
        //queen = q;

        cb.setGridLinesVisible(true);
        cb.getColumnConstraints().add(new ColumnConstraints(0));
        cb.getRowConstraints().add(new RowConstraints(0));
        setPieceListener(q);
        setPieceListener(b);

        for(int i = 0; i<8; i++){
            for(int j = 0; j<8; j++){
                Rectangle r = (Rectangle)cb.getChildren().get(i*8+j);
                Bounds boun = r.getBoundsInParent();
                double boofX = cb.parentToLocal(boun.getCenterX(),boun.getCenterY()).getX();
                double boofY = cb.parentToLocal(boun.getCenterX(),boun.getCenterY()).getY();

                System.out.println("bounds levelup " + boofX + " "+ boofY);
                //r.setFill(Color.BLACK);
                setListener(r);
            }
        }

        //cb.add(pane1, 1,1);
        //cb.add(pane2, 5, 5);
        cb.add(q, 0, 0);
        cb.add(b, 0,0);
        //move(q, (Rectangle)cb.getChildren().get(2));




        //Rectangle r = (Rectangle)cb.getChildren().get(1*8+1);
        //setListener(r);

        //#############################################



        //queen.setStyle("-fx-background-color: transparent ");
        //queen.setHeight(35);
        //queen.setWidth(35);
        //cb.add(queen,5,5);

        //pane2.getChildren().add(queen);
        //queen.setPrefWidth(size);
        //queen.setPrefHeight(size);

        //####################################
        //laudan korkeus ja leveys yhtäsuuret
        double cb_h = cb.getHeight();
        double space = cb_h / 16;

        double[][] centerxy = new double[64][2];
        //Pair<Double, Double> pairs = new Pair(8,8);

        //nappulan jättäminen ruudun keskikohtaan
        //ruudukon ruutujen keskikohtien koordinaattien laskeminen
        int h = 0;
        for (int i = 1; i < 16; i += 2) {
            for (int j = 1; j < 16; j += 2) {

                centerxy[h][0] = i * space;
                centerxy[h][1] = j * space;
                h++;
            }
        }






        cb.setOnMouseReleased(e -> {
            //System.out.println("cb mouse released");
        });

        cb.setOnMouseDragReleased(e -> {
            //e.getPickResult()
            //System.out.println("dropped to pane");
            //queen.setTranslateX(pane1.getTranslateX());
            //queen.setTranslateY(pane1.getTranslateY());

            //((Pane)dragged.getParent().getChildren().remove(dragged));


        });

        cb.setOnMouseDragOver(e -> {

            //System.out.println("hiiri yläpuolella");

            e.consume();

        });



    }

    public void setPieceListener(Piece p){
        p.setOnMousePressed(e -> {
            mouseX = e.getSceneX();
            mouseY = e.getSceneY();
            oldX = p.getTranslateX();
            oldY = p.getTranslateY();

            p.setMouseTransparent(true);
            e.consume();
        });

        p.setOnMouseReleased(e -> {

            Double d = cb.getMinHeight();
            p.setMouseTransparent(false);
            e.consume();

        });
        p.setOnDragDetected(e -> {

            p.startFullDrag();
            currentpiece = p;
            System.out.println("drag detected");
            p.setMouseTransparent(true);

            e.consume();

        });
        p.setOnMouseDragged(e -> {
            double offsetX = e.getSceneX() - mouseX;
            double offsetY = e.getSceneY() - mouseY;

            newTranslateX = oldX + offsetX;
            newTranslateY = oldY + offsetY;
            p.setMouseTransparent(true);

            p.setTranslateX(newTranslateX);
            p.setTranslateY(newTranslateY);
            e.consume();

        });
    }

    //lasketaan taulukosta lähin koordinaattipari
    public double[] nearest(double x, double y, double[][] s) {


        double dist = 10000.;
        double[] xy = new double[2];

        for (int i = 0; i < 64; i++) {
            double x1 = s[i][0];
            double y1 = s[i][1];

            double x1x = Math.abs(x1 - x);
            double y1y = Math.abs(y1 - y);
            if (Math.hypot(x1x, y1y) < dist) {
                dist = Math.hypot(x1x, y1y);
                xy[0] = x1;
                xy[1] = y1;
            }
        }
        return xy;
    }

    //Asetetaan kuuntelijat kaikille gridpane(cb):ssa oleville neliöille
    //ja hiiren ollessa sen kohdalla tiputetaan valittu nappula siihen
    public void setListener(Rectangle r) {
        r.setOnMouseDragOver(e -> {
            //System.out.println("hiiri laatikon yläpuolella");
        });
        r.setOnMouseDragReleased( e -> {
            System.out.println("mouse released above laatikko");

            System.out.println(currentpiece.toString());
            Point2D toParent = currentpiece.localToParent(currentpiece.getTranslateX(), currentpiece.getTranslateY());
            double oofX = toParent.getX();
            double oofY = toParent.getY();

            //currentpiece.setTranslateX(r.getScene().getX());
            //currentpiece.setTranslateY(r.getScene().getY());

            //Oikea translate 0,0 ruudun suhteen:
            currentpiece.setTranslateX(r.localToParent(r.getX(),r.getY()).getX());
            currentpiece.setTranslateY(r.localToParent(r.getX(),r.getY()).getY());

            System.out.println("uudet koordinaatit " + r.localToParent(r.getX(),r.getY()).getX() +"   " +r.localToParent(r.getX(),r.getY()).getY());


        });
    }

    public void move(Piece p, Rectangle r ){

        System.out.println("alustetaan");
        p.setTranslateX(r.localToParent(r.getX(),r.getY()).getX());
        p.setTranslateY(r.localToParent(r.getX(),r.getY()).getY());
    }
}


