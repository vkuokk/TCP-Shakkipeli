package Shakkipeli;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server extends Thread{
    public int portti;
    public trafficIn t_in;
    public trafficOut t_out;
    private ObjectOutputStream out;

    public Server(int port){
        this.portti = port;
    }


    public void send(String message){
        t_out.out(message);
    }

    @Override
    public void run() {
        try {

            System.out.println("odotetaan pelaajaa");
            while(true){
                ServerSocket ssock = new ServerSocket(portti);
                System.out.println("soketti auki "+ portti);
                Socket client = ssock.accept();
                t_in = new trafficIn(client);
                t_out = new trafficOut(client);
                t_in.start();
                t_out.start();
                System.out.println("tässä ollaan");
                System.out.println(t_in.in());
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
        }

        @Override
        public void run() {
            //String vastaanotettu;
            try {
                in = new ObjectInputStream(ssock.getInputStream());
                while(true) {
                    vastaanotettu = in.readUTF();

                    System.out.println(in);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {

    }

    private class trafficOut extends Thread {
        private Socket csock;
        private ObjectOutputStream out;

        public trafficOut(Socket client) {
            this.csock = client;
        }

        public void out(Serializable msg){
            try {
                out.writeObject(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            System.out.println("valmiina lähettämään viestejä");
            //PrintWriter out;
            try {
                 out = new ObjectOutputStream(csock.getOutputStream());
                 while(true){

                 }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
