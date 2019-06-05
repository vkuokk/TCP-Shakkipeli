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

        spawnForWhite(sd ,size);

        /*
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


*/
        for(int i = 0; i<8; i++){
            for(int j = 0; j<8; j++){
                Rectangle r = (Rectangle)cb.getChildren().get(i*8+j);
                //rct[i*j] = r;
                //r.setFill(Color.BLACK);
                setListener(r);
            }
    }

        /*
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

         */

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

    public void spawnForWhite(int side, int size){

        //Mustat nappulat
        Queen bq = new Queen(0, spaces, size);
        King bk = new King(0,spaces,size);
        Bishop bb1,bb2;
        Rook br1,br2;
        Knight bk1,bk2;
        Pawn bp1,bp2,bp3,bp4,bp5,bp6,bp7,bp8;

        Piece[] blackPieces = new Piece[16];

        bb1 = new Bishop(0,spaces,size);
        bb2 = new Bishop(0,spaces,size);
        br1 = new Rook(0,spaces,size);
        br2 = new Rook(0,spaces,size);
        bk1 = new Knight(0,spaces,size);
        bk2 = new Knight(0,spaces,size);

        bp1 = new Pawn(0,spaces,size);
        bp2 = new Pawn(0,spaces,size);
        bp3 = new Pawn(0,spaces,size);
        bp4 = new Pawn(0,spaces,size);
        bp5 = new Pawn(0,spaces,size);
        bp6 = new Pawn(0,spaces,size);
        bp7 = new Pawn(0,spaces,size);
        bp8 = new Pawn(0,spaces,size);

        blackPieces[0] = br1;
        blackPieces[1] = bk1;
        blackPieces[2] = bb1;
        blackPieces[3] = bq;
        blackPieces[4] = bk;
        blackPieces[5] = bb2;
        blackPieces[6] = bk2;
        blackPieces[7] = br2;
        blackPieces[8] = bp1;
        blackPieces[9] = bp2;
        blackPieces[10] = bp3;
        blackPieces[11] = bp4;
        blackPieces[12] = bp5;
        blackPieces[13] = bp6;
        blackPieces[14] = bp7;
        blackPieces[15] = bp8;










        //Valkoiset nappulat
        Queen wq = new Queen(1, spaces, size);
        King wk = new King(1,spaces,size);
        Bishop wb1,wb2;
        Rook wr1,wr2;
        Knight wk1,wk2;
        Pawn wp1,wp2,wp3,wp4,wp5,wp6,wp7,wp8;

        Piece[] whitePieces = new Piece[16];


        wb1 = new Bishop(1,spaces,size);
        wb2 = new Bishop(1,spaces,size);
        wr1 = new Rook(1,spaces,size);
        wr2 = new Rook(1,spaces,size);
        wk1 = new Knight(1,spaces,size);
        wk2 = new Knight(1,spaces,size);

        wp1 = new Pawn(1,spaces,size);
        wp2 = new Pawn(1,spaces,size);
        wp3 = new Pawn(1,spaces,size);
        wp4 = new Pawn(1,spaces,size);
        wp5 = new Pawn(1,spaces,size);
        wp6 = new Pawn(1,spaces,size);
        wp7 = new Pawn(1,spaces,size);
        wp8 = new Pawn(1,spaces,size);

        whitePieces[0] = wr1;
        whitePieces[1] = wk1;
        whitePieces[2] = wb1;
        whitePieces[3] = wq;
        whitePieces[4] = wk;
        whitePieces[5] = wb2;
        whitePieces[6] = wk2;
        whitePieces[7] = wr2;
        whitePieces[8] = wp1;
        whitePieces[9] = wp2;
        whitePieces[10] = wp3;
        whitePieces[11] = wp4;
        whitePieces[12] = wp5;
        whitePieces[13] = wp6;
        whitePieces[14] = wp7;
        whitePieces[15] = wp8;


        cb.setGridLinesVisible(true);
        cb.getColumnConstraints().add(new ColumnConstraints(0));
        cb.getRowConstraints().add(new RowConstraints(0));


        for(int i =0; i<16; i++){
            setPieceListener(whitePieces[i]);
            setPieceListener(blackPieces[i]);
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
            cb.add(blackPieces[i],0,0);
            cb.add(whitePieces[i],0,0);
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

            int m = 0;
            for(int i = 7;i>5;i--){
                for(int j = 7; j>-1; j--){
                    move(whitePieces[m], getRectangle(i,j,cb));
                    m++;
                }
            }
        });
    }
}


