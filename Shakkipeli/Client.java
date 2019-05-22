package Shakkipeli;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client extends Thread {
    public int port;
    public InetAddress ia;

    public Client(InetAddress ina, int portti){
        this.ia = ina;
        this.port = portti;
    }
    @Override
    public void run() {
        while(true){
            try {
                Socket soc = new Socket(ia,port);
                System.out.println("soketti luotu " + ia.toString() + port);
                Thread t_in = new trafficIn(soc);
                Thread t_out = new trafficOut(soc);
                t_in.start();
                t_out.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private class trafficIn extends Thread{
        private Socket ssock;
        public trafficIn(Socket client) {
            this.ssock = client;
        }
        @Override
        public void run() {
            try {
                while(true) {
                    InputStream in = new BufferedInputStream(ssock.getInputStream());
                    System.out.println(in);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private class trafficOut extends Thread {
        private Socket csock;
        public trafficOut(Socket client) {
            this.csock = client;
        }

        @Override
        public void run() {
            try {
                PrintStream out = new PrintStream(csock.getOutputStream());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
