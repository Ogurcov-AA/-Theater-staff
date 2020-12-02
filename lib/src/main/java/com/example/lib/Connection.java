package com.example.lib;

import org.omg.PortableServer.THREAD_POLICY_ID;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import sun.rmi.runtime.Log;


public class Connection implements Runnable{
        private Socket socket;
        private Thread nextThread;
        private Thread outThread;
        private BufferedReader in;

        private BufferedWriter out;
        private String ipAddr;
        private int port;
        private String Login = "User";

           private ConnectionLis eventList;

        public Connection(final ConnectionLis eventList, final String ipAddr, final int port) throws IOException {
            //    this.socket = socket;

            nextThread =
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                socket = new Socket(ipAddr, port);
                                in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                                out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8")));
                                eventList.ConnectionReady(Connection.this);
                                while (!nextThread.isInterrupted()) {
                                    eventList.ReceiveString(Connection.this, in.readLine());
                                }
                            } catch (IOException e) {
                                Thread.currentThread().interrupt();
                                e.printStackTrace();
                            } finally {
                                eventList.Disconnection(Connection.this);
                                return;
                            }

                        }

                    });
            nextThread.start();
        }

    public synchronized void sendString (final String msg) {
        System.out.println(msg);
        new Thread(new Runnable() {
            @Override
            public void run() {
        try {
      //      Thread.sleep(200);
             //   if(!nextThread.isAlive()) {
                    out.write(msg + System.getProperty("line.separator"));
                    System.out.println("SystemLog (send): " + msg);
                    out.flush();
                    Thread.interrupted();
               // }
            } catch (IOException e) {
                eventList.Exception(Connection.this, e);
                disconnected();
                Thread.interrupted();
            }
            }
        }).start();

    }

        public synchronized void disconnected(){
            nextThread.interrupt();
            try{
                socket.close();
            }
            catch (IOException e){
                eventList.Exception(Connection.this, e);
            }
        }

        public String OutputAdr(){
            return String.valueOf(socket.getInetAddress());
        }

        public boolean CheckAliveConnect(){
            if(socket == null)
            return false;
           return true;
        }


        @Override
        public String toString(){
        return "������������ " + Login + socket.getInetAddress() ;
    }

    @Override
    public void run() {

    }
}
