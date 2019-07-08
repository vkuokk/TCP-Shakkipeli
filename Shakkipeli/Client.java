package Shakkipeli;

import javafx.application.Platform;
import javafx.geometry.Point2D;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

// Ville Kuokkanen 2.7.2019
// Client-luokkassa muodostetaan yhteys palvelimeen
// Client käynnistetään Shakkicontrollerista käsin omana säikeenään, jonka jälkeen Client jakaa käyttäjän
// liikenteen vielä edelleen kahteen säikeeseen: in ja out.
// Tässä myös vastaanotetaan ja lähetetään viestit sekä siirrot.

public class Client extends Thread {
    public int port;
    public InetAddress ia;
    public trafficIn t_in;
    public trafficOut t_out;
    public Shakkicontroller shc;
    private boolean running = true;

    public Client(InetAddress ina, int portti, Shakkicontroller shc){
        this.ia = ina;
        this.port = portti;
        this.shc = shc;
    }
    public void stopRunning(){
        this.running = false;
        t_in.stopRunning();
        t_out.stopRunning();

    }

    public void send(String message) {

            if(running)t_out.out(message);

    }
    public void sendMove(int x, int y, Piece pc){
        String pieceName = pc.getName();
        t_out.out("@" + " " +x +" "+ y+" "+pieceName);
    }

    @Override
    public void run() {
            try {
                Socket soc = new Socket(ia,port);
                System.out.println("soketti luotu " + ia.toString() + port);
                t_in = new trafficIn(soc);
                t_out = new trafficOut(soc);

            } catch (IOException e) {
                e.printStackTrace();
            }

    }

    // Säie sisään tulevalle liikenteelle
    private class trafficIn extends Thread{
        private Socket ssock;
        private String vastaanotettu;
        private boolean running = true;

        private trafficIn(Socket client) {
            this.ssock = client;
            this.start();
        }
        private void stopRunning(){
            try {
                ssock.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.running = false;

        }

        @Override
        public void run() {
            try {
                BufferedReader inp = new BufferedReader(new InputStreamReader(ssock.getInputStream(),"UTF-8"));
                while(running) {
                    vastaanotettu = inp.readLine();
                    if(vastaanotettu == null) {
                        Platform.runLater(()->{
                            shc.appendInfo("Vastustaja sulki pelin");
                        });
                        this.stopRunning();
                        break;
                    }

                    if(vastaanotettu.contains("//0")){
                        Platform.runLater(()-> {
                            shc.appendInfo("Liityit onnistuneesti peliin " + ssock.getInetAddress().toString() );
                            shc.aloitaPeli("musta");
                        });
                    }
                    if(vastaanotettu.contains("//1")){
                        Platform.runLater(()-> {
                            shc.appendInfo("Liityit onnistuneesti peliin " + ssock.getInetAddress().toString() );
                            shc.aloitaPeli("valkoinen");
                        });
                    }
                    if(vastaanotettu.contains("@")){
                        shc.interpretMove(vastaanotettu);
                    }

                    if(!vastaanotettu.contains("//0") && !vastaanotettu.contains("//1") && !vastaanotettu.contains("@") && vastaanotettu.length() >2) {
                        shc.appendText(vastaanotettu);
                    }
                }
            } catch (IOException e) {
                //e.printStackTrace();
                this.stopRunning();
            }
        }
    }

    // Ulospäin menevän liikenteen säie
    private class trafficOut extends Thread {
        private Socket csock;
        private DataOutputStream out;

        private trafficOut(Socket client) {
            this.csock = client;
            this.start();
        }
        private void stopRunning(){
            try {
                csock.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        private void out(String msg){
            try {
                out.writeUTF(msg + "\n");

                out.flush();

            } catch (IOException e) {
                this.stopRunning();
            }
        }
        @Override
        public void run() {
            try {
                out = new DataOutputStream(csock.getOutputStream());

            } catch (IOException e) {
                this.stopRunning();
            }
        }
    }
}
