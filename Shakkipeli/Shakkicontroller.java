package Shakkipeli;

import javafx.geometry.Point2D;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.net.*;
import java.util.ResourceBundle;
import java.util.Optional;

import javafx.scene.control.TextInputDialog;


public class Shakkicontroller implements Initializable{
    private Game game;
    private boolean isServer = true;
    private Server palvelin;
    private Client asiakas;
    private boolean listenToClient = true;


    private String puoli;
    private String IPString;
    private String PortString;

    private String localPort;

    //@FXML private Button fxLiity;
    @FXML private TextField fxIP;
    @FXML private TextField fxPortti;
    @FXML private TextField fxChatbox;
    @FXML private TextArea fxChatfield;
    @FXML private GridPane fxChessgrid;

    //Liittymisnapin tapahtuma
    @FXML void handleLiity(){
        listenToClient = false;
        isServer = false;
        System.out.println(IPString);
        System.out.println(PortString);
        //createBoard();

        // Luo shakkinappulat



        try {
            asiakas = new Client(InetAddress.getByName(IPString), Integer.parseInt(PortString), this);
            System.out.println(InetAddress.getByName(IPString));
            //client.start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        asiakas.start();
    }

    //tekstikenttään kirjoittaminen
    @FXML void handleChat(){
        String msg = fxChatbox.getText();
        System.out.println(msg);
        fxChatfield.appendText(msg + "\n");
        if(isServer)palvelin.send(msg);
        if(!isServer)asiakas.send(msg);
        fxChatbox.clear();
    }


    public void alusta(){

        //TESTAUSTA VARTEN
        IPString = "127.0.0.1";
        PortString = "57";
        localPort = "57";


        fxIP.textProperty().addListener((observable, f, newIP) -> {
            IPString = newIP;
        });

        fxPortti.textProperty().addListener((observable, f, newPort) -> {
            PortString = newPort;
        });

        //TESTAUSTA VARTEN POISTETTU
/*
        TextInputDialog f = new TextInputDialog("");
        f.setTitle("Shakkipeli");
        f.setHeaderText("Portti, jonka avulla toinen pelaaja voi liittyä");
        Optional<String> result = f.showAndWait();


        result.ifPresent(name -> {
            localPort = name;
        });

 */



        palvelin = new Server(Integer.parseInt(localPort), this);
        //Thread p = new Thread(palvelin);
        palvelin.start();
        //kuuntele();
    }

    public void aloitaPeli(String puoli){
        createBoard();
        setPuoli(puoli);
        fxChatfield.appendText("Olet pelaaja: " + puoli + "\n");
        int ipuol = 0;
        if(puoli.contains("musta")) ipuol = 0;
        if(puoli.contains("valkoinen")) ipuol = 1;

        game = new Game(fxChessgrid,ipuol,this);
        game.spawn();

    }

    public TextArea getFxChatfield(){
        return fxChatfield;
    }

    public void setPuoli(String puoli){
        this.puoli = puoli;
        String testiteksti = "//0";
        byte[] b = testiteksti.getBytes();

        for(byte bi : b){
            System.out.println(bi);
        }

        if(isServer) {
            if (puoli == "musta") palvelin.send("//1");
            else palvelin.send("//0");
        }
    }

    public void appendText(String text){
        if(puoli == "musta") fxChatfield.appendText("valkoinen: " + text + "\n");
        else fxChatfield.appendText("musta: "+ text + "\n");
    }









    public void createBoard(){

        //final int size = 8;
        for (int i = 0; i<8; i++) {
            for (int j = 0; j<8; j++) {
                Rectangle square = new Rectangle();
                Color color;
                if ((i+j) %2 == 0) {
                    color = Color.rgb(242, 237, 225);
                } else {
                    color = Color.rgb(114, 175, 161);
                }

                square.setFill(color);
                //square.setStyle("-fx-background-color: "+BLACK+";");

                GridPane.setConstraints(square, i,j);

                //TOIMIVA
                fxChessgrid.add(square, i, j);

                square.widthProperty().bind(fxChessgrid.widthProperty().divide(8));
                square.heightProperty().bind(fxChessgrid.heightProperty().divide(8));
            }
        }
    }

    public void sendMove(Point2D tomove, Piece ps){
        double yCoord = tomove.getY();
        double xCoord = tomove.getX();
        //Point2D fmove = new Point2D(yCoord,xCoord);
        Point2D chessCenter = new Point2D(fxChessgrid.heightProperty().divide(2).get(), fxChessgrid.widthProperty().divide(2).get());
        System.out.println(chessCenter);
        xCoord = chessCenter.getX()-xCoord;
        yCoord = chessCenter.getY()-yCoord;

        Point2D newCoord = new Point2D(xCoord,yCoord);
        palvelin.sendMove(newCoord, ps);

           //palvelin.sendMove(fmove, ps);

    }

    public void interpretMove(String move){
        String[] split = move.split(" ");
        String xCoord = split[1];
        String yCoord = split[2];
        String pcname = split[4];
        System.out.println(xCoord + " " + yCoord + " " +pcname);
        game.moveOpponent(game.getByName(pcname), Double.parseDouble(xCoord), Double.parseDouble(yCoord));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        alusta();
        //kuuntele();
    }
}
