package Shakkipeli;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

import java.lang.reflect.Array;
import java.util.ArrayList;

// Ville Kuokkanen 2.7.2019
// Game-luokassa tapahtuu kaikki itse shakkiin liittyvä mm. nappuloiden alustus, nappuloiden siirrot ja niiden oikeellisuuden tarkistus.
// Peli toimii ohestalyöntiä ja itse shakki-tilannetta lukuunottamatta täysin shakin sääntöjen mukaisesti.
// Peli toimii niin, että ruudut ja nappulat ovat omissa 2-ulotteisissa taulukoissaan, jotka päivittyvät pelin siirtojen mukaisesti
// niin, että esim. pcs -taulukossa on aina ajantasainen tilanne nappuloista pelilaudalla
// Tässä käsitellään myös edellisen siirron korostaminen ja lisätään kuuntelijat kaikille shakkilaudan ruuduille ja nappuloille, jotta
// pelin tapahtumia voidaan käsitellä.
// Itse tehty siirto tarkastetaan jo ennen siirron suorittamista, jonka jälkeen siirto lähetetään Shakkicontrollerille, joka muuttaa
// koordinaatit siten, että siirretty nappula asettuu oikein vastustajan pelilaudalle

public class Game {

    private double moveStartx;
    private double moveStarty;
    private GridPane cb;
    private int sd;
    private boolean dragged = false;
    private boolean turn = false;
    private Piece highlightable;
    private Piece currentpiece;
    private ArrayList<Piece> pieces = new ArrayList<>();
    private ArrayList<Rectangle> possibilities = new ArrayList<>();
    private Shakkicontroller shc;

    Rectangle[][] rcts = new Rectangle[8][8];
    Piece[][] pcs = new Piece[8][8];
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
        cb.getColumnConstraints().add(new ColumnConstraints(0));
        cb.getRowConstraints().add(new RowConstraints(0));
        //Lisätään nappulat pelilaudalle puolen mukaan 0 = musta, 1 = valkoinen
        Platform.runLater(()->{
            spawnPieces(sd ,size);
        });

        for(int i = 0; i<8; i++){
            for(int j = 0; j<8; j++){
                Rectangle r = (Rectangle)cb.getChildren().get(i*8+j);
                setListener(r);
            }
        }

        if(sd == 1) turn = true;


    }

    private void setPieceListener(Piece p){
        p.setOnMousePressed(e -> {
            dragged = false;
            moveStartx = p.getTranslateX();
            moveStarty = p.getTranslateY();

            mouseX = e.getSceneX();
            mouseY = e.getSceneY();
            oldX = p.getTranslateX();
            oldY = p.getTranslateY();

            p.setMouseTransparent(true);


            e.consume();
        });

        p.setOnMouseReleased(e -> {
            Rectangle r = getRbyC(p.getTranslateX(),p.getTranslateY());

            if(!dragged || !possibilities.contains(r)){
                p.setTranslateX(moveStartx);
                p.setTranslateY(moveStarty);
            }
            if(p.getTranslateX() < -1 || p.getTranslateX() > 500 || p.getTranslateY() < -1 || p.getTranslateY() > 500){
                p.setTranslateX(moveStartx);
                p.setTranslateY(moveStarty);
            }

            for(Rectangle re : possibilities){
                re.setStrokeWidth(0);
            }
            p.setMouseTransparent(false);
            e.consume();

        });
        p.setOnDragDetected(e -> {
            dragged = true;
            p.startFullDrag();
            currentpiece = p;
            ArrayList<Rectangle> canBeMoved = getAvailable(currentpiece);
            possibilities = canBeMoved;
            for(Rectangle r : possibilities){
                r.setStrokeType(StrokeType.INSIDE);
                r.setStrokeWidth(4);
                r.setStroke(Color.GREEN);
            }

            currentpiece.toFront();
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

        p.setOnMouseDragReleased(e -> {
            String side;
            if(sd == 0) side = "b";
            else side = "w";

            //Tarkistetaan onko nappula, jonka päälle siirretään toinen, laillinen siirto, jotta highlight toimii oikein
            if(possibilities.contains(rcts[p.getY()][p.getX()]) && currentpiece.getName().startsWith(side) && !p.getName().startsWith(side)) {

                if(highlightable != null) highlightable.removeHighlight();
                highlightable = currentpiece;
                highlightable.setHighlight();
            }

            if(turn) {
                Platform.runLater(() -> {
                    Rectangle r = getRbyC(p.getTranslateX(),p.getTranslateY());
                    if(possibilities.contains(r)) {

                        currentpiece.setTranslateX(p.getTranslateX());
                        currentpiece.setTranslateY(p.getTranslateY());
                        int X = 0;
                        int Y = 0;
                        for (int i = 0; i < 8; i++) {
                            for (int j = 0; j < 8; j++) {
                                if (rcts[i][j] == r) {
                                    X = j;
                                    Y = i;
                                }
                            }
                        }
                        final int fX = X;
                        final int fY = Y;
                        shc.sendMove(fX, fY, currentpiece);

                        pcs[p.getY()][p.getX()] = currentpiece;
                        pcs[currentpiece.getY()][currentpiece.getX()] = null;
                        currentpiece.setX(p.getX());
                        currentpiece.setY(p.getY());
                        currentpiece.setHasMoved();
                        cb.getChildren().remove(p);
                        if(p.getPieceType() == "king"){
                            Platform.runLater(()-> {
                                shc.appendInfo("Voitit pelin");
                            });
                        }
                        turn = false;

                        for(Rectangle re : possibilities){
                            re.setStrokeWidth(0);
                        }
                        possibilities = new ArrayList<>();


                    }
                });
            }
        });
    }



    //Asetetaan kuuntelijat kaikille gridpane(cb):ssa oleville neliöille
    //ja hiiren ollessa sen kohdalla tiputetaan valittu nappula siihen
    private void setListener(Rectangle r) {
        r.setOnMouseDragReleased( e -> {
            if(turn && possibilities.contains(r)) {
                if(highlightable != null)highlightable.removeHighlight();
                int X = 0;
                int Y = 0;
                for(int i = 0; i<8;i++){
                    for(int j = 0; j<8;j++){
                        if(rcts[i][j] == r){
                            X = j;
                            Y = i;
                        }
                    }
                }
                final int fX = X;
                final int fY = Y;

                pcs[currentpiece.getY()][currentpiece.getX()] = null;
                pcs[Y][X] = currentpiece;

                currentpiece.setTranslateX(r.localToParent(r.getX(), r.getY()).getX());
                currentpiece.setTranslateY(r.localToParent(r.getX(), r.getY()).getY());
                currentpiece.setX(fX);
                currentpiece.setY(fY);
                highlightable = currentpiece;
                highlightable.setHighlight();

                //tornin siirto tornituksessa
                //tornitus oikealle valkoisilla
                if(currentpiece.getPieceType() == "king" && !currentpiece.gethasMoved() && X == 6 && Y == 7 && sd == 1){
                    Piece wrrook = pcs[7][7];
                    Rectangle wrcas = rcts[7][5];
                    pcs[7][7] = null;
                    pcs[7][5] = wrrook;
                    wrrook.setTranslateX(wrcas.localToParent(wrcas.getX(), wrcas.getY()).getX());
                    wrrook.setTranslateY(wrcas.localToParent(wrcas.getX(), wrcas.getY()).getY());
                    wrrook.setX(5);
                    wrrook.setY(7);
                }
                //tornitus oikealle mustalla
                if(currentpiece.getPieceType() == "king" && !currentpiece.gethasMoved() && X ==5 && Y ==7 && sd == 0){
                    Piece brrook = pcs[7][7];
                    Rectangle brcas = rcts[7][4];
                    pcs[7][7] = null;
                    pcs[7][4] = brrook;
                    brrook.setTranslateX(brcas.localToParent(brcas.getX(), brcas.getY()).getX());
                    brrook.setTranslateY(brcas.localToParent(brcas.getX(), brcas.getY()).getY());
                    brrook.setX(4);
                    brrook.setY(7);
                }
                //tornitus vasemmalle mustalla
                if(currentpiece.getPieceType() == "king" && !currentpiece.gethasMoved() && X == 1 && Y == 7 && sd == 0){
                    Piece lrrook = pcs[7][0];
                    Rectangle lrcas = rcts[7][2];
                    pcs[7][0] = null;
                    pcs[7][2] = lrrook;
                    lrrook.setTranslateX(lrcas.localToParent(lrcas.getX(), lrcas.getY()).getX());
                    lrrook.setTranslateY(lrcas.localToParent(lrcas.getX(), lrcas.getY()).getY());
                    lrrook.setX(2);
                    lrrook.setY(7);
                }
                //tornitus vasemmalle valkoisella
                if(currentpiece.getPieceType() == "king" && !currentpiece.gethasMoved() && X == 2 && Y == 7 && sd == 1){
                    Piece lwrook = pcs[7][0];
                    Rectangle blcas = rcts[7][3];
                    pcs[7][0] = null;
                    pcs[7][3] = lwrook;
                    lwrook.setTranslateX(blcas.localToParent(blcas.getX(), blcas.getY()).getX());
                    lwrook.setTranslateY(blcas.localToParent(blcas.getX(), blcas.getY()).getY());
                    lwrook.setX(3);
                    lwrook.setY(7);

                }

                currentpiece.setHasMoved();

                for(Rectangle re : possibilities){
                    re.setStrokeWidth(0);
                }

                Platform.runLater(() -> {
                    shc.sendMove(fX, fY, currentpiece);
                });
                turn = false;
            }
            else{
                currentpiece.setTranslateX(moveStartx);
                currentpiece.setTranslateY(moveStarty);
            }
        });
    }

    //Alustetaan nappulat oikeille paikoilleen
    private void move(Piece p, Rectangle r ){


        p.setTranslateX(r.localToParent(r.getX(),r.getY()).getX());
        p.setTranslateY(r.localToParent(r.getX(),r.getY()).getY());

        int X = 0;
        int Y = 0;
        for(int i = 0; i<8;i++){
            for(int j = 0; j<8;j++){
                if(rcts[i][j] == r){
                    X = j;
                    Y = i;
                }
            }
        }
        p.setX(X);
        p.setY(Y);

    }

    //Vastustajan tekemän liikkeen siirto
    public void moveOpponent(Piece p, int xCoord, int yCoord){
        Rectangle rec = rcts[yCoord][xCoord];
        p.setTranslateX(rec.localToParent(rec.getX(),rec.getY()).getX());
        p.setTranslateY(rec.localToParent(rec.getX(),rec.getY()).getY());
        if(highlightable != null)highlightable.removeHighlight();
        highlightable = p;
        highlightable.setHighlight();

        //vastustajan tornitus vasemmalle valkoisilla
        if(p.getPieceType() == "king" && !p.gethasMoved() && sd == 0 && xCoord == 1 && yCoord == 0 && !pcs[0][0].gethasMoved()){
            Piece lwrook = pcs[0][0];
            Rectangle lwr = rcts[0][2];
            lwrook.setTranslateX(lwr.localToParent(lwr.getX(), lwr.getY()).getX());
            lwrook.setTranslateY(lwr.localToParent(lwr.getX(), lwr.getY()).getY());
            pcs[0][2] = lwrook;
            pcs[0][0] = null;
            lwrook.setX(2);
            lwrook.setY(0);
        }
        //vastustajan tornitus oikealle valkoisilla
        if(p.getPieceType() == "king" && !p.gethasMoved() && sd == 0 && xCoord == 5 && yCoord == 0 && !pcs[0][7].gethasMoved()){
            Piece rwrook = pcs[0][7];
            Rectangle rwr = rcts[0][4];
            rwrook.setTranslateX(rwr.localToParent(rwr.getX(), rwr.getY()).getX());
            rwrook.setTranslateY(rwr.localToParent(rwr.getX(), rwr.getY()).getY());
            pcs[0][4] = rwrook;
            pcs[0][7] = null;
            rwrook.setX(4);
            rwrook.setY(0);
        }
        //vastustajan tornitus vasemmalle mustalla
        if(p.getPieceType() == "king" && !p.gethasMoved() && sd == 1 && xCoord == 2 && yCoord == 0 && !pcs[0][0].gethasMoved()){
            Piece lbrook = pcs[0][0];
            Rectangle lbr = rcts[0][3];
            lbrook.setTranslateX(lbr.localToParent(lbr.getX(), lbr.getY()).getX());
            lbrook.setTranslateY(lbr.localToParent(lbr.getX(), lbr.getY()).getY());
            pcs[0][3] = lbrook;
            pcs[0][0] = null;
            lbrook.setX(3);
            lbrook.setY(0);
        }
        //vastustajan tornitus oikealle mustalla
        if(p.getPieceType() == "king" && !p.gethasMoved() && sd == 1 && xCoord == 6 && yCoord == 0 && !pcs[0][7].gethasMoved()){
            Piece rbrook = pcs[0][7];
            Rectangle rbr = rcts[0][5];
            rbrook.setTranslateX(rbr.localToParent(rbr.getX(), rbr.getY()).getX());
            rbrook.setTranslateY(rbr.localToParent(rbr.getX(), rbr.getY()).getY());
            pcs[0][5] = rbrook;
            pcs[0][7] = null;
            rbrook.setX(5);
            rbrook.setY(0);
        }

        for(Piece pi : pieces){
            if(pi.getX() == xCoord && pi.getY() == yCoord && p != pi){
                Platform.runLater(() -> {
                    cb.getChildren().remove(pi);
                    if(pi.getPieceType() == "king") shc.appendInfo("Hävisit pelin");
                });

            }
        }

        pcs[p.getY()][p.getX()] = null;
        pcs[yCoord][xCoord] = p;
        p.setX(xCoord);
        p.setY(yCoord);
        p.setHasMoved();
        turn = true;

    }

    // Haetaan ruutu rivin ja sarakkeen perusteella
    private Rectangle getRectangle(int rivi, int sarake, GridPane gp){
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

    // Luodaan nappulat kentälle
    private void spawnPieces(int side, int size){
        //Yläpuolen nappulat
        String[] sides = {"b","w"};
        Queen tq = new Queen(1-side, size, sides[1-side]+"q");
        King tk = new King(1-side,size, sides[1-side]+"k");
        Bishop tb1,tb2;
        Rook tr1,tr2;
        Knight tk1,tk2;
        Pawn tp1,tp2,tp3,tp4,tp5,tp6,tp7,tp8;

        Piece[] topPieces = new Piece[16];

        tb1 = new Bishop(1-side,size, sides[1-side]+"b1");
        tb2 = new Bishop(1-side,size, sides[1-side]+"b2");
        tr1 = new Rook(1-side,size, sides[1-side]+"r1");
        tr2 = new Rook(1-side,size, sides[1-side]+"r2");
        tk1 = new Knight(1-side,size, sides[1-side]+"k1");
        tk2 = new Knight(1-side,size, sides[1-side]+"k2");

        tp1 = new Pawn(1-side,size, sides[1-side]+"p1");
        tp2 = new Pawn(1-side,size, sides[1-side]+"p2");
        tp3 = new Pawn(1-side,size, sides[1-side]+"p3");
        tp4 = new Pawn(1-side,size, sides[1-side]+"p4");
        tp5 = new Pawn(1-side,size, sides[1-side]+"p5");
        tp6 = new Pawn(1-side,size, sides[1-side]+"p6");
        tp7 = new Pawn(1-side,size, sides[1-side]+"p7");
        tp8 = new Pawn(1-side,size, sides[1-side]+"p8");

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
        Queen bq = new Queen(side,size, sides[side]+"q");
        King bk = new King(side,size, sides[side]+"k");
        Bishop bb1,bb2;
        Rook br1,br2;
        Knight bk1,bk2;
        Pawn bp1,bp2,bp3,bp4,bp5,bp6,bp7,bp8;

        Piece[] bottomPieces = new Piece[16];


        bb1 = new Bishop(side,size, sides[side]+"b1");
        bb2 = new Bishop(side,size, sides[side]+"b2");
        br1 = new Rook(side,size, sides[side]+"r1");
        br2 = new Rook(side,size, sides[side]+"r2");
        bk1 = new Knight(side,size, sides[side]+"k1");
        bk2 = new Knight(side,size, sides[side]+"k2");

        bp1 = new Pawn(side,size, sides[side]+"p1");
        bp2 = new Pawn(side,size, sides[side]+"p2");
        bp3 = new Pawn(side,size, sides[side]+"p3");
        bp4 = new Pawn(side,size, sides[side]+"p4");
        bp5 = new Pawn(side,size, sides[side]+"p5");
        bp6 = new Pawn(side,size, sides[side]+"p6");
        bp7 = new Pawn(side,size, sides[side]+"p7");
        bp8 = new Pawn(side,size, sides[side]+"p8");

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

        }


        for(int i = 0; i<16; i++){
            cb.add(topPieces[i],0,0);
            cb.add(bottomPieces[i],0,0);
        }



        //Runlater, jotta neliöiden rajat ovat asettuneet, ilman tätä ei jostain syystä toimi
        Platform.runLater(()-> {
            for(int i = 0; i<8; i++){
                for(int j = 0; j<8; j++){
                    Rectangle r = getRectangle(i,j,cb);
                    rcts[i][j] = r;
                }
            }

            int n = 0;
            for(int i = 0;i<2;i++){
                for(int j = 0; j<8; j++) {
                    move(topPieces[n], getRectangle(i, j, cb));
                    pcs[i][j] = topPieces[n];
                    n++;
                }
            }

            int m = 0;
            for(int i = 7;i>5;i--){
                for(int j = 7; j>-1; j--){
                    move(bottomPieces[m], getRectangle(i,j,cb));
                    pcs[i][j] = bottomPieces[m];
                    m++;
                }
            }
        });
        for(int i = 0; i<16;i++){
            pieces.add(bottomPieces[i]);
            pieces.add(topPieces[i]);
        }
    }

    //Haetaan nappula nimen perusteella, jotta voidaan siirtää oikeaa vastustajan nappulaa
    public Piece getByName(String name){
        Piece pc = null;
        for(Piece p : pieces){
            if(p.getName().equals(name)) pc = p;
        }
        return pc;
    }


    //Haetaan oikea neliö koordinaattien perusteella
    private Rectangle getRbyC(double x, double y){
        Rectangle re = null;
        for(int i = 0; i<8; i++){
            for(int j = 0; j<8; j++) {
                Rectangle r = rcts[i][j];

                if (r.localToParent(r.getX(), r.getY()).getX() == x && r.localToParent(r.getX(), r.getY()).getY() == y) {
                    re = rcts[i][j];
                }
            }
        }

        return re;
    }

    // Haetaan mahdolliset ruudut joihin nappula voidaan siirtää
    private ArrayList<Rectangle> getAvailable (Piece pc){
        ArrayList<Rectangle> available = new ArrayList<>();
        String side = "";
        if(sd == 0) side = "b";
        if(sd == 1) side = "w";

        if(!turn || !pc.getName().startsWith(side)) return available;
        for( String s : pc.getMoves()){
            switch(s){
                case "FORWARD": {
                    int initialX = pc.getX();
                    int initialY = pc.getY();
                    for (int i = initialY - 1; i > -1; i--) {
                        Piece inFront = pcs[i][initialX];
                        Rectangle next = rcts[i][initialX];
                        if(inFront == null )available.add(next);
                        if(inFront != null && inFront.getName().startsWith(side)) break;
                        if (pc.getPieceType().equals("pawn")) {
                            if (pc.gethasMoved()) break;
                            if (i - initialY == -2) break;
                        }
                        if(pc.getPieceType().equals("rook") || pc.getPieceType().equals("queen")){
                            if(inFront != null && !inFront.getName().startsWith(side)){
                                available.add(next);
                                break;
                            }
                        }
                    }
                }
                break;

                case "DIAGONAL": {
                    int initialX = pc.getX();
                    int initialY = pc.getY();
                    int pX = initialX;
                    int mX = initialX;
                    if (pc.getPieceType().equals("pawn") && initialY > 1) {
                        if (initialX > 0 && pcs[initialY - 1][initialX - 1] != null && !pcs[initialY - 1][initialX - 1].getName().startsWith(side)) {
                            available.add(rcts[initialY - 1][initialX - 1]);
                        }
                        if (initialX < 7 && pcs[initialY - 1][initialX + 1] != null && !pcs[initialY - 1][initialX + 1].getName().startsWith(side)) {
                            available.add(rcts[initialY - 1][initialX + 1]);
                        }

                    }
                    if (pc.getPieceType().equals("bishop") || pc.getPieceType().equals("queen")) {

                        //Ylävasen
                        for (int i = initialY - 1; i > -1; i--) {
                            mX = mX - 1;
                            if (mX > -1 && pcs[i][mX] == null) available.add(rcts[i][mX]);
                            if (mX > -1 && pcs[i][mX] != null && !pcs[i][mX].getName().startsWith(side)) {
                                available.add(rcts[i][mX]);
                                break;
                            }
                            if (mX > -1 && pcs[i][mX] != null && pcs[i][mX].getName().startsWith(side)) break;
                        }
                        mX = initialX;

                        //Yläoikea
                        for (int i = initialY - 1; i > -1; i--) {
                            pX = pX + 1;
                            if (pX < 8 && pcs[i][pX] == null) available.add(rcts[i][pX]);
                            if (pX < 8 && pcs[i][pX] != null && !pcs[i][pX].getName().startsWith(side)) {
                                available.add(rcts[i][pX]);
                                break;
                            }
                            if (pX < 8 && pcs[i][pX] != null && pcs[i][pX].getName().startsWith(side)) break;
                        }
                        pX = initialX;

                        //Alavasen
                        for (int i = initialY + 1; i < 8; i++) {
                            mX = mX - 1;
                            if (mX > -1 && pcs[i][mX] == null) available.add(rcts[i][mX]);
                            if (mX > -1 && pcs[i][mX] != null && !pcs[i][mX].getName().startsWith(side)) {
                                available.add(rcts[i][mX]);
                                break;
                            }
                            if (mX > -1 && pcs[i][mX] != null && pcs[i][mX].getName().startsWith(side)) break;
                        }
                        mX = initialX;
                        //Alaoikea
                        for (int i = initialY + 1; i < 8; i++) {
                            pX = pX + 1;
                            if (pX < 8 && pcs[i][pX] == null) available.add(rcts[i][pX]);
                            if (pX < 8 && pcs[i][pX] != null && !pcs[i][pX].getName().startsWith(side)) {
                                available.add(rcts[i][pX]);
                                break;
                            }
                            if (pX < 8 && pcs[i][pX] != null && pcs[i][pX].getName().startsWith(side)) break;
                        }
                        pX = initialX;
                    }

                }
                break;

                case "KNIGHT": {
                    int initialX = pc.getX();
                    int initialY = pc.getY();
                    //Yläpuolen liikkeen validointi
                    if (initialX > 1 && initialY > 0){
                        if(pcs[initialY - 1][initialX - 2] != null && !pcs[initialY-1][initialX-2].getName().startsWith(side)) available.add(rcts[initialY-1][initialX-2]);
                        if(pcs[initialY -1][initialX -2 ] == null) available.add(rcts[initialY-1][initialX-2]);
                    }

                    if (initialX > 0 && initialY > 1){
                        if(pcs[initialY - 2][initialX - 1] != null && !pcs[initialY-2][initialX-1].getName().startsWith(side)) available.add(rcts[initialY-2][initialX-1]);
                        if(pcs[initialY - 2][initialX - 1] == null) available.add(rcts[initialY-2][initialX-1]);
                    }

                    if (initialX <7 && initialY >1){
                        if(pcs[initialY -2][initialX +1] != null && !pcs[initialY-2][initialX+1].getName().startsWith(side)) available.add(rcts[initialY-2][initialX+1]);
                        if(pcs[initialY -2][initialX +1] == null) available.add(rcts[initialY-2][initialX+1]);
                    }
                    if (initialX <6 && initialY >0){
                        if(pcs[initialY -1][initialX +2] != null && !pcs[initialY-1][initialX+2].getName().startsWith(side)) available.add(rcts[initialY-1][initialX+2]);
                        if(pcs[initialY -1][initialX +2] == null) available.add(rcts[initialY-1][initialX+2]);
                    }

                    //Alapuolen liikkeen validointi
                    if (initialX > 1 && initialY < 7){
                        if(pcs[initialY + 1][initialX - 2] != null && !pcs[initialY+1][initialX-2].getName().startsWith(side)) available.add(rcts[initialY+1][initialX-2]);
                        if(pcs[initialY +1][initialX -2 ] == null) available.add(rcts[initialY+1][initialX-2]);
                    }
                    if (initialX > 0 && initialY < 6){
                        if(pcs[initialY + 2][initialX - 1] != null && !pcs[initialY+2][initialX-1].getName().startsWith(side)) available.add(rcts[initialY+2][initialX-1]);
                        if(pcs[initialY +2][initialX -1 ] == null) available.add(rcts[initialY+2][initialX-1]);
                    }

                    if (initialX <7 && initialY < 6){
                        if(pcs[initialY + 2][initialX +1] != null && !pcs[initialY+2][initialX+1].getName().startsWith(side)) available.add(rcts[initialY+2][initialX+1]);
                        if(pcs[initialY +2][initialX +1 ] == null) available.add(rcts[initialY+2][initialX+1]);
                    }
                    if (initialX <6 && initialY < 7){
                        if(pcs[initialY + 1][initialX +2] != null && !pcs[initialY+1][initialX+2].getName().startsWith(side)) available.add(rcts[initialY+1][initialX+2]);
                        if(pcs[initialY +1][initialX +2 ] == null) available.add(rcts[initialY+1][initialX+2]);
                    }
                }
                break;

                case "SIDEWAYS AND BACKWARDS": {
                    int initialX = pc.getX();
                    int initialY = pc.getY();

                    //Alemmat tornille ja kuningattarelle
                    //oikealle

                    for(int i = initialX+1;i<8;i++){
                        if(initialX >6) break;
                        Piece toRight = pcs[initialY][i];
                        if(toRight != null && !toRight.getName().startsWith(side)){
                            available.add(rcts[initialY][i]);
                            break;
                        }
                        if(toRight != null && toRight.getName().startsWith(side)) break;
                        if(toRight == null) available.add(rcts[initialY][i]);

                    }

                    //vasemmalle
                    for(int i = initialX-1; i>-1; i--){
                        if(initialX<1) break;
                        Piece toLeft = pcs[initialY][i];
                        if(toLeft != null && !toLeft.getName().startsWith(side)){
                            available.add(rcts[initialY][i]);
                            break;
                        }
                        if(toLeft != null && toLeft.getName().startsWith(side)) break;
                        if(toLeft == null) available.add(rcts[initialY][i]);
                    }

                    //alas
                    for(int i = initialY+1;i<8;i++){
                        if(initialY >6) break;
                        Piece down = pcs[i][initialX];
                        if(down != null && !down.getName().startsWith(side)){
                            available.add(rcts[i][initialX]);
                            break;
                        }
                        if(down != null && down.getName().startsWith(side)) break;
                        if(down == null) available.add(rcts[i][initialX]);
                    }

                }
                break;

                case "KING":{

                    int initialX = pc.getX();
                    int initialY = pc.getY();
                    //alas ylös ja sivuille
                    if(initialX >0 && pcs[initialY][initialX-1] != null && !pcs[initialY][initialX-1].getName().startsWith(side)) available.add(rcts[initialY][initialX-1]);
                    if(initialY <7 && pcs[initialY+1][initialX] != null && !pcs[initialY+1][initialX].getName().startsWith(side)) available.add(rcts[initialY+1][initialX]);
                    if(initialX <7 && pcs[initialY][initialX+1] != null && !pcs[initialY][initialX+1].getName().startsWith(side)) available.add(rcts[initialY][initialX+1]);
                    if(initialY >0 && pcs[initialY-1][initialX] != null && !pcs[initialY-1][initialX].getName().startsWith(side)) available.add(rcts[initialY-1][initialX]);

                    if(initialX > 0 &&pcs[initialY][initialX-1] == null) available.add(rcts[initialY][initialX-1]);
                    if(initialY <7 &&pcs[initialY+1][initialX] == null) available.add(rcts[initialY+1][initialX]);
                    if(initialX <7 &&pcs[initialY][initialX+1] == null) available.add(rcts[initialY][initialX+1]);
                    if(initialY > 0 &&pcs[initialY-1][initialX] == null) available.add(rcts[initialY-1][initialX]);

                    //nurkat
                    if(initialY >0 && initialX >0 && pcs[initialY-1][initialX-1] != null && !pcs[initialY-1][initialX-1].getName().startsWith(side)) available.add(rcts[initialY-1][initialX-1]);
                    if(initialY >0 && initialX <7 && pcs[initialY-1][initialX+1] != null && !pcs[initialY-1][initialX+1].getName().startsWith(side)) available.add(rcts[initialY-1][initialX+1]);
                    if(initialY <7 && initialX <7 && pcs[initialY+1][initialX+1] != null && !pcs[initialY+1][initialX+1].getName().startsWith(side)) available.add(rcts[initialY+1][initialX+1]);
                    if(initialY <7 && initialX >0 && pcs[initialY+1][initialX-1] != null && !pcs[initialY+1][initialX-1].getName().startsWith(side)) available.add(rcts[initialY+1][initialX-1]);

                    if(initialY >0 && initialX >0 && pcs[initialY-1][initialX-1] == null) available.add(rcts[initialY-1][initialX-1]);
                    if(initialY >0 && initialX <7 && pcs[initialY-1][initialX+1] == null) available.add(rcts[initialY-1][initialX+1]);
                    if(initialY <7 && initialX <7 && pcs[initialY+1][initialX+1] == null) available.add(rcts[initialY+1][initialX+1]);
                    if(initialY <7 && initialX >0 && pcs[initialY+1][initialX-1] == null) available.add(rcts[initialY+1][initialX-1]);

                    //tornitus
                    Piece rightRook = pcs[7][7];
                    Piece leftRook = pcs[7][0];
                    boolean rightClear = true;
                    boolean leftClear = true;

                    if(!pc.gethasMoved() && rightRook != null && !rightRook.gethasMoved()){
                        for(int i = pc.getX()+1; i<7; i++){
                            if(pcs[pc.getY()][i] != null) rightClear = false;
                        }
                        if(rightClear) available.add(rcts[7][pc.getX() +2]);
                    }

                    if(!pc.gethasMoved() && leftRook != null && !leftRook.gethasMoved()){
                        for(int i = pc.getX()-1; i>0; i--){
                            if(pcs[pc.getY()][i] != null) leftClear = false;
                        }
                        if(leftClear) available.add(rcts[7][pc.getX() -2]);
                    }

                }
                break;
            }

        }

        return available;
    }

}


