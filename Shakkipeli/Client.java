package Shakkipeli;

import javafx.application.Platform;
import javafx.geometry.Point2D;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client extends Thread {
    public int port;
    public InetAddress ia;
    public trafficIn t_in;
    public trafficOut t_out;
    public Shakkicontroller shc;

    public Client(InetAddress ina, int portti, Shakkicontroller shc){
        this.ia = ina;
        this.port = portti;
        this.shc = shc;
    }

    public void send(String message) {

            t_out.out(message);

    }
    public void sendMove(int x, int y, Piece pc){
        String pieceName = pc.getName();
        t_out.out("@" + " " +x +" "+ y+" "+pieceName);
    }
    /*
    public void sendMove(Point2D point, Piece pc){
        double toX = point.getX();
        double toY = point.getY();
        String pieceName = pc.getName();

        t_out.out("@" +" "+toX +" " + toY + " " + " " + pieceName);
    }

     */

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
    private class trafficIn extends Thread{
        private Socket ssock;
        private String vastaanotettu;

        public trafficIn(Socket client) {
            this.ssock = client;
            this.start();
        }

        public String in(){

            return "ei viestiä";
        }

        @Override
        public void run() {
            try {
                BufferedReader inp = new BufferedReader(new InputStreamReader(ssock.getInputStream()));
                while(true) {
                    vastaanotettu = inp.readLine();
                    System.out.println(vastaanotettu);


                    if(vastaanotettu.contains("//0")){
                        Platform.runLater(()-> {
                            shc.appendText("Liityit onnistuneesti peliin " + ssock.getInetAddress().toString() );
                            shc.aloitaPeli("musta");
                        });
                    }
                    if(vastaanotettu.contains("//1")){
                        Platform.runLater(()-> {
                            shc.appendText("Liityit onnistuneesti peliin " + ssock.getInetAddress().toString() );
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
                e.printStackTrace();
            }
        }
    }
    private class trafficOut extends Thread {
        private Socket csock;
        private DataOutputStream out;

        public trafficOut(Socket client) {
            this.csock = client;
            this.start();
        }

        public void out(String msg){
            try {
                out.writeUTF(msg + "\n");

                out.flush();

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
