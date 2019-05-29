package Shakkipeli;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private GridPane cb;
    private int  sd;
    @FXML private Queen queen;

    //
    private double mouseX, mouseY;
    private double oldX, oldY;

    //puolet 0 = musta, 1 = valkoinen
    public Game(GridPane newChessBoard, int puoli) {

        cb = newChessBoard;
        sd = puoli;

    }

    //Luodaan nappulat pelilaudalle
    public void spawn() {




        int [][] spaces = new int[7][7];
        int size = cb.heightProperty().intValue()/8;

        Queen q = new Queen(sd, spaces, size);
        q.setStyle("-fx-background-color: transparent ");
        queen = q;
        //queen.setHeight(35);
        //queen.setWidth(35);
        cb.add(queen,5,5);


        queen.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                oldX = mouseEvent.getSceneX();
                oldY = mouseEvent.getSceneY();
                mouseX = ((Button)(mouseEvent.getSource())).getTranslateX();
                mouseY = ((Button)(mouseEvent.getSource())).getTranslateY();
            }
        });

        // uus
        queen.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                System.out.println("päästit irti");
                //laudan korkeus ja leveys yhtäsuuret
                double cb_h = cb.getHeight();
                double space = cb_h/16;

                double[] centerxy = new double[64];


                //TODO
                //nappulan jättäminen ruudun keskikohtaan

                for(int i = 1; i<8; i++){
                    for(int j = 1; j<8; j++){
                        //
                        if(i%2 != 0 && j%2 != 0){

                        }
                    }
                }
            }
            });


        queen.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                double offsetX = t.getSceneX() - oldX;
                double offsetY = t.getSceneY() - oldY;
                double newTranslateX = mouseX + offsetX;
                double newTranslateY = mouseY + offsetY;





                ((Button)(t.getSource())).setTranslateX(newTranslateX);
                ((Button)(t.getSource())).setTranslateY(newTranslateY);

            }
        });





/*
        queen.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                dragDetected(event);
            }
        });

        cb.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent dragEvent) {
                dragOver(dragEvent);
            }
        });

        cb.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent dragEvent) {
                dragDropped(dragEvent);
            }
        });

        cb.setOnDragDone(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent dragEvent) {
                dragDone(dragEvent);
            }
        });

*/
    }

    private void dragDone(DragEvent event){
        TransferMode modeUsed = event.getTransferMode();
        event.consume();
    }

    private void dragDropped(DragEvent event){
        Dragboard db = event.getDragboard();
        cb.add(queen,1,1);
        event.setDropCompleted(true);
        event.consume();
    }

    private void dragOver(DragEvent event){
        Dragboard db = event.getDragboard();
        event.acceptTransferModes(TransferMode.MOVE);
        event.consume();
    }


    private void dragDetected(MouseEvent event){
        Dragboard db = queen.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent content = new ClipboardContent();
        content.putString("kuningatar");
        db.setContent(content);
        event.consume();
    }
}
