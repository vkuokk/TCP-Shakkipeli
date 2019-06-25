package Shakkipeli;


import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.geometry.Point2D;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class Server extends Thread {
    public int portti;
    public trafficIn t_in;
    public trafficOut t_out;
    private ObjectOutputStream out;
    private Shakkicontroller shc;
    private boolean running = true;

    public Server(int port, Shakkicontroller shc){
        this.portti = port;
        this.shc = shc;
    }
    public void stopRunning(){
        this.running = false;
        t_in.stopRunning();
        t_out.stopRunning();

    }

    public String getMessage(){
        return t_in.in();
    }


    public void send(String message){
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
            ServerSocket s = new ServerSocket(portti);
            String[] side ={"musta", "valkoinen"};
            final String puoli;
            Random random = new Random();
            puoli = side[random.nextInt(side.length)];

            while(running) {

                Socket sock = s.accept();
                //System.out.println("soketti hyväksytty");
                //shc.setPuoli(puoli);
                shc.getFxChatfield().appendText("Pelaaja liittyi osoittesta "+ sock.getInetAddress().toString() + "\n");
                t_in = new trafficIn(sock);
                t_in.start();
                //uutta
                t_out = new trafficOut(sock);
                t_out.start();
                //luodaan peli

                Platform.runLater(()-> {
                    shc.aloitaPeli(puoli);
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }




    }

    private class trafficIn extends Thread{
        private Socket ssock;
        private ObjectInputStream in;
        public String vastaanotettu;
        private boolean running = true;

        public String in(){
            try {
                return in.readUTF();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "ei viestiä";
        }

        public void stopRunning(){
            t_out.out("Lopetti pelin");
            try {
                ssock.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            this.running = false;

        }

        public trafficIn(Socket client) {
            this.ssock = client;
            //this.run();
        }

        @Override
        public void run() {
            String line;


            try{

                BufferedReader inp = new BufferedReader(new InputStreamReader(ssock.getInputStream()));


            while(running){
                //System.out.println("trafficIn kuuntelusilmukka");
                try{
                    System.out.println("yritetään lukea viestiä");
                    line = inp.readLine();
                    if(line == null) break;
                    if(line.contains("@")){
                        shc.interpretMove(line);
                    }
                    if(!line.contains("@") && line.length() > 2) shc.appendText(line);
                    //shc.getFxChatfield().appendText(line + "\n");
                    //if(line!=null)System.out.println(line);
                    //line = inp.readLine();
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

    private class trafficOut extends Thread {
        private Socket csock;
        private DataOutputStream out;

        public trafficOut(Socket client) {
            this.csock = client;
            //this.start();
        }

        public void out(String msg){


            try {
                //out.writeChars(msg +"\n");
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
