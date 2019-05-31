package Shakkipeli;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
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
    @FXML
    private Queen queen;

    int[][] spaces = new int[7][7];
    //
    private double mouseX, mouseY;
    private double oldX, oldY;
    private double newTranslateX;
    private double newTranslateY;
    private final DataFormat buttonFormat = new DataFormat("MyButton");

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

        setListener(pane1);
        //setListener(pane2);
        pane1.setStyle("-fx-background-color: #FFF888;");
        cb.add(pane1, 5, 5);
        cb.add(pane2, 1,1);



        //#############################################


        int size = cb.heightProperty().intValue() / 8;

        Queen q = new Queen(sd, spaces, size);
        queen = q;
        cb.setGridLinesVisible(true);
        cb.getColumnConstraints().add(new ColumnConstraints(0));
        cb.getRowConstraints().add(new RowConstraints(0));
        //queen.setStyle("-fx-background-color: transparent ");
        //queen.setHeight(35);
        //queen.setWidth(35);
        //cb.add(queen,5,5);

        pane2.getChildren().add(queen);
        //cb.add(queen, 0, 0);
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


        queen.setOnMousePressed(e -> {
            mouseX = e.getSceneX();
            mouseY = e.getSceneY();
            oldX = queen.getTranslateX();
            oldY = queen.getTranslateY();
            e.consume();
        });

        queen.setOnMouseReleased(e -> {
            //System.out.println("on drag dropped");
            Double d = cb.getMinHeight();
            System.out.println("cb minheight: " + d);
            double[] todrop = nearest(newTranslateX, newTranslateY, centerxy);
            //queen.setTranslateX(todrop[0]);
            //queen.setTranslateY(todrop[1]);
            queen.setMouseTransparent(false);
            pane2.getChildren().remove(queen);

        });




        //pane.addEventFilter(MouseEvent.ANY, e -> System.out.println( e));
        //queen.addEventFilter(MouseEvent.ANY, e-> System.out.println( e));

        queen.setOnDragDetected(e -> {
            queen.startFullDrag();
            System.out.println("drag detected");
            e.consume();

        });
        queen.setOnMouseDragged(e -> {
            double offsetX = e.getSceneX() - mouseX;
            double offsetY = e.getSceneY() - mouseY;

            newTranslateX = oldX + offsetX;
            newTranslateY = oldY + offsetY;
            //System.out.println(newTranslateX + " " + newTranslateY);

            queen.setTranslateX(newTranslateX);
            queen.setTranslateY(newTranslateY);
            queen.setMouseTransparent(true);
            e.consume();
        });

        /*
        queen.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                double offsetX = t.getSceneX() - mouseX;
                double offsetY = t.getSceneY() - mouseY;

                newTranslateX = oldX + offsetX;
                newTranslateY = oldY + offsetY;
                System.out.println(newTranslateX + " " + newTranslateY);

                queen.setTranslateX(newTranslateX);
                queen.setTranslateY(newTranslateY);
                t.consume();

            }
        });

         */


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


    public void setListener(Pane pane) {
        pane.setOnMouseDragReleased(e -> {
            System.out.println("dropped to pane");
            //queen.setTranslateX(pane.getScene().getX());
            //queen.setTranslateY(pane.getScene().getY());
            Queen other = new Queen(sd, spaces, (int) pane.getWidth());
            other.setPrefHeight(pane.getHeight());
            other.setPrefWidth(pane.getWidth());
            pane.getChildren().add(other);

            queen.setMouseTransparent(false);
            //pane.getChildren().add(queen);
        });
    }
}
/*
//System.out.println("päästit irti");
                //laudan korkeus ja leveys yhtäsuuret
                double cb_h = cb.getHeight();
                double space = cb_h/16;

                        double[][] centerxy = new double[64][2];
                        //Pair<Double, Double> pairs = new Pair(8,8);

                        //nappulan jättäminen ruudun keskikohtaan
                        //ruudukon ruutujen keskikohtien koordinaattien laskeminen
                        int h = 0;
                        for(int i = 1; i<16; i+=2){
                            for(int j = 1; j<16; j+=2){

                                centerxy[h][0] = i*space;
                                centerxy[h][1] = j*space;
                        h++;
                    }
                }

                Bounds b = cb.getBoundsInLocal();
                System.out.println("cb:n keskipiste: " + b.getCenterX() + " " +b.getCenterY());
                System.out.println("cb:n minimi ja maksimi: " + b.getMinX() + " " + b.getMinY() + " "+ b.getMaxX() + " " + b.getMaxY());

                Point2D board = cb.parentToLocal(mouseX,mouseY);
                System.out.println(board.getX() + " " + board.getY());

                Point2D local = queen.localToParent(board.getX(), board.getY());
                double lx = local.getX();
                double ly = local.getY();

                //System.out.println("local: " + mouseX + " " + mouseY);
                //System.out.println("queen.localtoparent: "+ locallocal.getX() + " " +locallocal.getY());
                //System.out.println("cb.queen.localtoparent: " + lx + " " + ly);



                //System.out.println("parent to local: " + lx + " " +ly);
                //double[] n = nearest(mouseX, mouseY, centerxy);
                double[] n = nearest(lx, ly, centerxy);



                Point2D s = cb.parentToLocal(n[0], n[1]);
                double xs = s.getX();
                double ys = s.getY();

                Point2D news = queen.parentToLocal(xs,ys);
                double newx = news.getX();
                double newy = news.getY();

                //System.out.println("alkup: " + n[0] +" "+ n[1]);
                //System.out.println("cb.parenttolocal: " + xs + " " + ys);
                //System.out.println("queen.cb.parenttolocal: "  + newx + " " + newy);

                //System.out.println("queen to local bounds width" + b.getWidth() + " " + b.getHeight());


                //((Button)(t.getSource())).setTranslateX(newx);
                //((Button)(t.getSource())).setTranslateY(newy);

            }
            });
 */

