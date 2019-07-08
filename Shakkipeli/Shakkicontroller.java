package Shakkipeli;

import javafx.application.Platform;
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

// Ville Kuokkanen 2.7.2019
// Shakkicontroller on luokka käyttöliittymän tapahtumia varten. Kaikki käyttöliittymän nappien painallukset ja tekstikentät
// käsitellään tässä.

public class Shakkicontroller implements Initializable{
    private Game game;
    private boolean isServer = true;
    private Server palvelin;
    private Client asiakas;
    private String puoli;
    private String IPString;
    private String PortString;
    private String localPort;
    @FXML private TextField fxIP;
    @FXML private TextField fxPortti;
    @FXML private TextField fxChatbox;
    @FXML private TextArea fxChatfield;
    @FXML private GridPane fxChessgrid;

    //Liittymisnapin tapahtuma
    @FXML void handleLiity(){
        isServer = false;
        try {
            asiakas = new Client(InetAddress.getByName(IPString), Integer.parseInt(PortString), this);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            fxChatfield.appendText("IP-osoitteeseen ei voida liittyä" +"\n");
        }
        asiakas.start();
    }

    //tekstikenttään kirjoittaminen
    @FXML void handleChat(){
        String msg = fxChatbox.getText();
        fxChatfield.appendText(msg + "\n");
        if(isServer)palvelin.send(msg);
        if(!isServer)asiakas.send(msg);
        fxChatbox.clear();
    }

    //Pelin lopettamisen käsittely
    @FXML void handleLopeta(){
            if(!isServer)asiakas.stopRunning();
            if(isServer)palvelin.stopRunning();
            fxChessgrid.getChildren().clear();
            fxChatfield.appendText("Lopetetaan peli"+ "\n");

    }

    //Pelin alustaminen
    public void alusta(){

        fxIP.textProperty().addListener((observable, f, newIP) -> {
            IPString = newIP;
        });

        fxPortti.textProperty().addListener((observable, f, newPort) -> {
            if(newPort.matches("[0-9]+"))PortString = newPort;

        });

        TextInputDialog f = new TextInputDialog("");
        f.setResizable(true);
        Platform.runLater(() -> {
            f.setResizable(false);
        });
        f.setTitle("Shakkipeli");
        f.setHeaderText("Portti, jonka avulla toinen pelaaja voi liittyä");
        Optional<String> result = f.showAndWait();


        result.ifPresent(name -> {
            if(name.matches("[0-9]+")){
                localPort = name;
                startServer();
            }
            else fxChatfield.appendText("Syötetty portti on virheellinen, käynnistä peli uudelleen jos haluat, että peliisi voidaan liittyä" + "\n");
        });

    }

    // Käynnistetään palvelin
    public void startServer(){
        palvelin = new Server(Integer.parseInt(localPort), this);
        palvelin.start();
    }

    // Pelin aloittaminen
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
        if(isServer) {
            if (puoli == "musta") palvelin.send("//1");
            else palvelin.send("//0");
        }
    }

    // Tektsikenttään viestin lisääminen
    public void appendText(String text){
        Platform.runLater(() -> {
            if (puoli == "musta") fxChatfield.appendText("valkoinen: " + text + "\n");
            else fxChatfield.appendText("musta: " + text + "\n");
        });
    }

    // Informaation lisääminen tekstikenttään
    public void appendInfo(String text){
        Platform.runLater(()-> {
            fxChatfield.appendText(text + "\n");
        });
    }

    // Shakkilaudan luominen
    public void createBoard(){

        for (int i = 0; i<8; i++) {
            for (int j = 0; j<8; j++) {
                Rectangle square = new Rectangle();
                Color color;
                if ((i+j) %2 == 0) {
                    color = Color.valueOf("#eeeeee");
                } else {
                    color = Color.valueOf("#82747e");
                }

                square.setFill(color);

                GridPane.setConstraints(square, i,j);
                fxChessgrid.add(square, i, j);
                square.widthProperty().bind(fxChessgrid.widthProperty().divide(8));
                square.heightProperty().bind(fxChessgrid.heightProperty().divide(8));

            }
        }
    }


    // Viestin lähettäminen vastustajalle, koordinaatit muunnetaan tässä
    public void sendMove(int xcoord, int ycoord, Piece ps){
        int mX = 7-xcoord;
        int mY = 7-ycoord;


        if(isServer)palvelin.sendMove(mX, mY, ps);
        if(!isServer)asiakas.sendMove(mX, mY, ps);

    }

    // Vastustajan siirron tulkinta
    public void interpretMove(String move){
        String[] split = move.split(" ");
        String xCoord = split[1];
        String yCoord = split[2];
        String pcname = split[3];;
        game.moveOpponent(game.getByName(pcname), Integer.parseInt(xCoord), Integer.parseInt(yCoord));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        alusta();

    }

}
