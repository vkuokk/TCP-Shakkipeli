package Shakkipeli;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client extends Thread {
    public int port;
    public InetAddress ia;
    public trafficIn t_in;
    public trafficOut t_out;

    public Client(InetAddress ina, int portti){
        this.ia = ina;
        this.port = portti;
    }

    public void send(String message) {
        try {
            t_out.out.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(true){
            try {
                Socket soc = new Socket(ia,port);
                System.out.println("soketti luotu " + ia.toString() + port);
                t_in = new trafficIn(soc);
                t_out = new trafficOut(soc);
                t_in.start();
                t_out.start();
                System.out.println(t_in.in());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private class trafficIn extends Thread{
        private Socket ssock;
        private ObjectInputStream in;
        private String vastaanotettu;

        public trafficIn(Socket client) {
            this.ssock = client;
        }

        public String in(){
            try {
                return in.readUTF();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "ei viestiä";
        }

        @Override
        public void run() {
            try {
                while(true) {
                    in = new ObjectInputStream(ssock.getInputStream());
                    vastaanotettu = in.readUTF();
                    System.out.println(in);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
            try {
                out = new ObjectOutputStream(csock.getOutputStream());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
