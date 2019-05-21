package Shakkipeli;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import Shakkipeli.Server;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.io.*;
import java.util.ResourceBundle;
import java.util.Optional;
import java.util.Scanner;

import javafx.scene.control.TextInputDialog;


public class Shakkicontroller implements Initializable{
    private Game game;

    private String IPString;
    private String PortString;

    private String localPort;

    //@FXML private Button fxLiity;
    @FXML private TextField fxIP;
    @FXML private TextField fxPortti;
    @FXML private TextField fxChatbox;
    @FXML private TextArea fxChatfield;
    @FXML private GridPane fxChessgrid;

    @FXML void handleLiity(){
        System.out.println(IPString);
        System.out.println(PortString);
        createBoard();

        // Luo shakkinappulat
        Game game = new Game(fxChessgrid);

        try {
            joinServer(IPString, PortString);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }


    public void alusta(){
        fxIP.textProperty().addListener((observable, f, newIP) -> {
            IPString = newIP;
        });

        fxPortti.textProperty().addListener((observable, f, newPort) -> {
            PortString = newPort;
        });

        TextInputDialog f = new TextInputDialog("");
        f.setTitle("Shakkipeli");
        f.setHeaderText("Kuunneltava portti, jonka avulla toinen pelaaja voi liittyä");
        Optional<String> result = f.showAndWait();

        result.ifPresent(name -> {
            localPort = name;
        });


    }





    /*
    public void createServer(String portti){
        try (ServerSocket listener = new ServerSocket(Integer.parseInt(localPort))){
            /*while(true){
                try(Socket socket = listener.accept()) {
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    out.println("yhteys");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
            } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    */

    //TCP-asiakas
    public void joinServer(String IP, String portti) throws UnknownHostException {
        try {
            Socket socket = new Socket(InetAddress.getByName(IPString), Integer.parseInt(PortString));
            Scanner in = new Scanner(socket.getInputStream());
            while(in.hasNextLine()){
                System.out.println(in.nextLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                fxChessgrid.add(square, i, j);
                square.widthProperty().bind(fxChessgrid.widthProperty().divide(8));
                square.heightProperty().bind(fxChessgrid.heightProperty().divide(8));
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        alusta();
    }
}
