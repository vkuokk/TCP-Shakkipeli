package Shakkipeli;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;

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



        int size = cb.heightProperty().intValue() / 8;


        Queen q = new Queen(sd, spaces, size);
        King k = new King(sd,spaces,size);
        Bishop b1,b2;
        Rook r1,r2;
        Knight k1,k2;
        Pawn p1,p2,p3,p4,p5,p6,p7,p8;

        Piece[] blackPieces = new Piece[16];

        b1 = new Bishop(sd,spaces,size);
        b2 = new Bishop(sd,spaces,size);
        r1 = new Rook(sd,spaces,size);
        r2 = new Rook(sd,spaces,size);
        k1 = new Knight(sd,spaces,size);
        k2 = new Knight(sd,spaces,size);

        p1 = new Pawn(sd,spaces,size);
        p2 = new Pawn(sd,spaces,size);
        p3 = new Pawn(sd,spaces,size);
        p4 = new Pawn(sd,spaces,size);
        p5 = new Pawn(sd,spaces,size);
        p6 = new Pawn(sd,spaces,size);
        p7 = new Pawn(sd,spaces,size);
        p8 = new Pawn(sd,spaces,size);

        blackPieces[0] = r1;
        blackPieces[1] = k1;
        blackPieces[2] = b1;
        blackPieces[3] = q;
        blackPieces[4] = k;
        blackPieces[5] = b2;
        blackPieces[6] = k2;
        blackPieces[7] = r2;
        blackPieces[8] = p1;
        blackPieces[9] = p2;
        blackPieces[10] = p3;
        blackPieces[11] = p4;
        blackPieces[12] = p5;
        blackPieces[13] = p6;
        blackPieces[14] = p7;
        blackPieces[15] = p8;


        cb.setGridLinesVisible(true);
        cb.getColumnConstraints().add(new ColumnConstraints(0));
        cb.getRowConstraints().add(new RowConstraints(0));
        for(Piece i : blackPieces){
            setPieceListener(i);
        }



        for(int i = 0; i<8; i++){
            for(int j = 0; j<8; j++){
                Rectangle r = (Rectangle)cb.getChildren().get(i*8+j);
                //rct[i*j] = r;
                //r.setFill(Color.BLACK);
                setListener(r);
            }
    }

        for(Piece i : blackPieces){
            cb.add(i,0,0);
        }

        //Runlater, jotta neliöiden rajat ovat asettuneet, ilman tätä ei jostain syystä toimi
        Platform.runLater(()-> {
            int n = 0;
            for(int i = 0;i<2;i++){
                for(int j = 0; j<8; j++) {
                    move(blackPieces[n], getRectangle(i, j, cb));

                    n++;
                }
            }
        });

        double cb_h = cb.getHeight();
        double space = cb_h / 16;


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

        Bounds b = r.getBoundsInParent();
        System.out.println(b);

        p.setTranslateX(r.localToParent(r.getX(),r.getY()).getX());
        p.setTranslateY(r.localToParent(r.getX(),r.getY()).getY());
    }

    public Rectangle getRectangle(int rivi, int sarake, GridPane gp){
        Node nd = null;
        ObservableList<Node> nds = gp.getChildren();
        for(Node node : nds){
            if(gp.getRowIndex(node) == rivi && gp.getColumnIndex(node) == sarake){
                nd = node;
                break;
            }
        }
        return (Rectangle)nd;
    }
}


