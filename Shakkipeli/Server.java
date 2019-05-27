package Shakkipeli;


import javafx.application.Platform;
import javafx.beans.Observable;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
    public int portti;
    public trafficIn t_in;
    public trafficOut t_out;
    private ObjectOutputStream out;
    private Shakkicontroller shc;

    public Server(int port, Shakkicontroller shc){
        this.portti = port;
        this.shc = shc;
    }

    public String getMessage(){
        return t_in.in();
    }


    public void send(String message){
        t_out.out(message);
    }

    @Override
    public void run() {
        try {
            ServerSocket s = new ServerSocket(portti);
            while(true) {

                Socket sock = s.accept();
                System.out.println("soketti hyväksytty");
                t_in = new trafficIn(sock);
                t_in.start();
                //uutta
                t_out = new trafficOut(sock);
                t_out.start();
                Platform.runLater(()-> {
                    shc.createBoard();
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

        public String in(){
            try {
                return in.readUTF();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "ei viestiä";
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


            while(true){
                //System.out.println("trafficIn kuuntelusilmukka");
                try{
                    System.out.println("yritetään lukea viestiä");
                    line = inp.readLine();
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
                out.writeChars(msg +"\n");
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
