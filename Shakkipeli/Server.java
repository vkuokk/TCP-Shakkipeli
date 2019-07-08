package Shakkipeli;


import javafx.application.Platform;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

// Ville Kuokkanen 2.7.2019
// Server -luokka on palvelinta, eli pelin toista osapuolta varten oleva osa. Ohjelmaa avatessa käyttäjältä kysytään portti, johon
// avataan palvelinsoketti, joka mahdollistaa pelaajan liittymisen peliin
// Kuten Client, myös Server avaa lisäksi uudet säikeet sisäänpäin ja ulospäin menevälle liikenteelle.

public class Server extends Thread {
    private int portti;
    private trafficIn t_in;
    private trafficOut t_out;
    private Shakkicontroller shc;
    private boolean running = true;

    public Server(int port, Shakkicontroller shc){
        this.portti = port;
        this.shc = shc;
    }
    public
    void stopRunning(){
        t_in.stopRunning();
        t_out.stopRunning();

    }

    // Viestin lähettäminen
    public void send(String message){
        t_out.out(message);
    }

    // Siirron lähettäminen
    public void sendMove(int x, int y, Piece pc){
        String pieceName = pc.getName();
        t_out.out("@" + " " +x +" "+ y+" "+pieceName);
    }

    @Override
    public void run() {
        try {
            ServerSocket s = new ServerSocket(portti);
            String[] side ={"musta", "valkoinen"};
            final String puoli;
            Random random = new Random();
            puoli = side[random.nextInt(side.length)];

            while(running) {
                Socket sock = s.accept();
                shc.getFxChatfield().appendText("Pelaaja liittyi osoittesta "+ sock.getInetAddress().toString() + "\n");

                t_in = new trafficIn(sock);
                t_in.start();
                t_out = new trafficOut(sock);
                t_out.start();

                Platform.runLater(()-> {
                    shc.aloitaPeli(puoli);
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Säie sisään tulevaa liikennettä varten
    private class trafficIn extends Thread{
        private Socket ssock;
        private boolean running = true;



        public void stopRunning(){
            t_out.out("Lopetti pelin");
            try {
                ssock.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        public trafficIn(Socket client) {
            this.ssock = client;
        }

        @Override
        public void run() {
            String line;
            try{
                BufferedReader inp = new BufferedReader(new InputStreamReader(ssock.getInputStream(), "UTF-8"));

            while(running){
                try{
                    System.out.println("yritetään lukea viestiä");
                    line = inp.readLine();
                    if(line == null) break;
                    if(line.contains("@")){
                        shc.interpretMove(line);
                    }
                    if(!line.contains("@") && line.length() > 2) shc.appendText(line);
                    System.out.println(line);

                }catch (IOException e){
                    System.out.println("luku epäonnistui");
                    ssock.close();
                    break;
                }
            }
            }catch (IOException e){
                System.out.println("pieleen meni " +e);
            }
        }
    }

    public static void main(String[] args) {

    }

    // Säie ulospäin menevälle liikenteelle
    private class trafficOut extends Thread {
        private Socket csock;
        private DataOutputStream out;

        public trafficOut(Socket client) {
            this.csock = client;
        }

        public void out(String msg){
            try {
                out.writeUTF(msg + "\n");
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void stopRunning(){
            try {
                csock.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                 out = new DataOutputStream(csock.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
