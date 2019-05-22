package Shakkipeli;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server extends Thread{
    public int portti;
    public Server(int port){
        this.portti = port;
    }

    @Override
    public void run() {
        try {

            System.out.println("odotetaan pelaajaa");
            while(true){
                ServerSocket ssock = new ServerSocket(portti);
                System.out.println("soketti auki "+ portti);
                Socket client = ssock.accept();
                Thread t_in = new trafficIn(client);
                Thread t_out = new trafficOut(client);
                t_in.start();
                t_out.start();
                System.out.println("tässä ollaan");
            }
        } catch (IOException e) {
            e.printStackTrace();
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

    public static void main(String[] args) {

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
