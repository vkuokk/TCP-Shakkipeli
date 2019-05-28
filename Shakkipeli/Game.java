package Shakkipeli;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Game {

    private GridPane cb;
    private int  sd;
    @FXML private Queen queen;

    //puolet 0 = musta, 1 = valkoinen
    public Game(GridPane newChessBoard, int puoli) {

        cb = newChessBoard;
        sd = puoli;

    }

    //Luodaan nappulat pelilaudalle
    public void spawn() {
        int [][] spaces = new int[7][7];
        Queen q = new Queen(sd, spaces);
        q.setStyle("-fx-background-color: #ff0000; ");
        cb.add(q,0,5);
        queen = q;
        EventHandler<MouseEvent> evh = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println("painoit nappia");
            }
        };
        queen.addEventFilter(MouseEvent.MOUSE_CLICKED,evh);



    }


}
