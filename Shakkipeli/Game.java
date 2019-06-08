package Shakkipeli;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class Game {

    private GridPane cb;
    private int sd;
    private boolean turn = false;
    //@FXML
    //private Queen queen;
    private Piece currentpiece;
    private ArrayList<Piece> pieces = new ArrayList<>();
    private Shakkicontroller shc;

    int[][] spaces = new int[7][7];
    //
    private double mouseX, mouseY;
    private double oldX, oldY;
    private double newTranslateX;
    private double newTranslateY;

    //puolet 0 = musta, 1 = valkoinen
    public Game(GridPane newChessBoard, int puoli, Shakkicontroller shc) {

        cb = newChessBoard;
        sd = puoli;
        this.shc = shc;

    }

    //Luodaan nappulat pelilaudalle
    public void spawn() {



        int size = cb.heightProperty().intValue() / 8;
        cb.setGridLinesVisible(true);
        cb.getColumnConstraints().add(new ColumnConstraints(0));
        cb.getRowConstraints().add(new RowConstraints(0));


        //Lisätään nappulat pelilaudalle puolen mukaan 0 = musta, 1 = valkoinen
        spawnPieces(sd ,size);

        for(int i = 0; i<8; i++){
            for(int j = 0; j<8; j++){
                Rectangle r = (Rectangle)cb.getChildren().get(i*8+j);
                //rct[i*j] = r;
                //r.setFill(Color.BLACK);
                setListener(r);
            }


    }

        turn = true;

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

            if(turn) {
                Platform.runLater(() -> {
                    shc.sendMove(r.localToParent(r.getX(), r.getY()), currentpiece);
                });
            }

            System.out.println("uudet koordinaatit " + r.localToParent(r.getX(),r.getY()).getX() +"   " +r.localToParent(r.getX(),r.getY()).getY());


        });
    }

    public void move(Piece p, Rectangle r ){

        //System.out.println("alustetaan");

        Bounds b = r.getBoundsInParent();
        //System.out.println(b);

        p.setTranslateX(r.localToParent(r.getX(),r.getY()).getX());
        p.setTranslateY(r.localToParent(r.getX(),r.getY()).getY());

    }

    public void moveOpponent(Piece p, double xCoord, double yCoord){
        p.setTranslateX(xCoord);
        p.setTranslateY(yCoord);
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

    public void spawnPieces(int side, int size){


        //Mustat nappulat
        //(Yläpuolen nappulat)
        String[] sides = {"b","w"};
        Queen tq = new Queen(1-side, spaces, size, sides[1-side]+"q");
        King tk = new King(1-side,spaces,size, sides[1-side]+"k");
        Bishop tb1,tb2;
        Rook tr1,tr2;
        Knight tk1,tk2;
        Pawn tp1,tp2,tp3,tp4,tp5,tp6,tp7,tp8;

        Piece[] topPieces = new Piece[16];

        tb1 = new Bishop(1-side,spaces,size, sides[1-side]+"b1");
        tb2 = new Bishop(1-side,spaces,size, sides[1-side]+"b2");
        tr1 = new Rook(1-side,spaces,size, sides[1-side]+"r1");
        tr2 = new Rook(1-side,spaces,size, sides[1-side]+"r2");
        tk1 = new Knight(1-side,spaces,size, sides[1-side]+"k1");
        tk2 = new Knight(1-side,spaces,size, sides[1-side]+"k2");

        tp1 = new Pawn(1-side,spaces,size, sides[1-side]+"p1");
        tp2 = new Pawn(1-side,spaces,size, sides[1-side]+"p2");
        tp3 = new Pawn(1-side,spaces,size, sides[1-side]+"p3");
        tp4 = new Pawn(1-side,spaces,size, sides[1-side]+"p4");
        tp5 = new Pawn(1-side,spaces,size, sides[1-side]+"p5");
        tp6 = new Pawn(1-side,spaces,size, sides[1-side]+"p6");
        tp7 = new Pawn(1-side,spaces,size, sides[1-side]+"p7");
        tp8 = new Pawn(1-side,spaces,size, sides[1-side]+"p8");

        topPieces[0] = tr1;
        topPieces[1] = tk1;
        topPieces[2] = tb1;

        if(side == 1) {
            topPieces[3] = tq;
            topPieces[4] = tk;
        }
        if(side == 0){

            topPieces[3] = tk;
            topPieces[4] = tq;
        }

        topPieces[5] = tb2;
        topPieces[6] = tk2;
        topPieces[7] = tr2;
        topPieces[8] = tp1;
        topPieces[9] = tp2;
        topPieces[10] = tp3;
        topPieces[11] = tp4;
        topPieces[12] = tp5;
        topPieces[13] = tp6;
        topPieces[14] = tp7;
        topPieces[15] = tp8;










        //Alarivin nappulat
        Queen bq = new Queen(side, spaces, size, sides[side]+"q");
        King bk = new King(side,spaces,size, sides[side]+"k");
        Bishop bb1,bb2;
        Rook br1,br2;
        Knight bk1,bk2;
        Pawn bp1,bp2,bp3,bp4,bp5,bp6,bp7,bp8;

        Piece[] bottomPieces = new Piece[16];


        bb1 = new Bishop(side,spaces,size, sides[side]+"b1");
        bb2 = new Bishop(side,spaces,size, sides[side]+"b2");
        br1 = new Rook(side,spaces,size, sides[side]+"r1");
        br2 = new Rook(side,spaces,size, sides[side]+"r2");
        bk1 = new Knight(side,spaces,size, sides[side]+"k1");
        bk2 = new Knight(side,spaces,size, sides[side]+"k2");

        bp1 = new Pawn(side,spaces,size, sides[side]+"p1");
        bp2 = new Pawn(side,spaces,size, sides[side]+"p2");
        bp3 = new Pawn(side,spaces,size, sides[side]+"p3");
        bp4 = new Pawn(side,spaces,size, sides[side]+"p4");
        bp5 = new Pawn(side,spaces,size, sides[side]+"p5");
        bp6 = new Pawn(side,spaces,size, sides[side]+"p6");
        bp7 = new Pawn(side,spaces,size, sides[side]+"p7");
        bp8 = new Pawn(side,spaces,size, sides[side]+"p8");

        bottomPieces[0] = br1;
        bottomPieces[1] = bk1;
        bottomPieces[2] = bb1;
        if(side == 0) {
            bottomPieces[3] = bq;
            bottomPieces[4] = bk;
        }
        if(side == 1){
            bottomPieces[3] = bk;
            bottomPieces[4] = bq;
        }

        bottomPieces[5] = bb2;
        bottomPieces[6] = bk2;
        bottomPieces[7] = br2;
        bottomPieces[8] = bp1;
        bottomPieces[9] = bp2;
        bottomPieces[10] = bp3;
        bottomPieces[11] = bp4;
        bottomPieces[12] = bp5;
        bottomPieces[13] = bp6;
        bottomPieces[14] = bp7;
        bottomPieces[15] = bp8;



        for(int i =0; i<16; i++){
            setPieceListener(bottomPieces[i]);
            setPieceListener(topPieces[i]);
            pieces.add(bottomPieces[i]);
            pieces.add(topPieces[i]);
        }

        //Toimiva
        /*
        for(Piece i : blackPieces){
            setPieceListener(i);
        }
         for(Piece i : blackPieces){
            cb.add(i,0,0);
        }
         */

        for(int i = 0; i<16; i++){
            cb.add(topPieces[i],0,0);
            cb.add(bottomPieces[i],0,0);
        }



        //Runlater, jotta neliöiden rajat ovat asettuneet, ilman tätä ei jostain syystä toimi
        Platform.runLater(()-> {
            int n = 0;
            for(int i = 0;i<2;i++){
                for(int j = 0; j<8; j++) {
                    move(topPieces[n], getRectangle(i, j, cb));

                    n++;
                }
            }

            int m = 0;
            for(int i = 7;i>5;i--){
                for(int j = 7; j>-1; j--){
                    move(bottomPieces[m], getRectangle(i,j,cb));
                    m++;
                }
            }
        });
    }

    //Haetaan nappula nimen perusteella, jotta voidaan siirtää oikeaa vastustajan nappulaa
    public Piece getByName(String name){
        Piece pc = null;
        for(Piece p : pieces){
            if(p.getName().equals(name)) pc = p;
        }
        return pc;
    }
}


