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

            t_out.out(message);

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
                out.writeChars(msg + "\n");
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
