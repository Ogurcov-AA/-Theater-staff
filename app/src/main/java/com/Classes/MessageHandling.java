package com.Classes;

import android.os.Build;

import com.example.lib.Connection;
import com.example.lib.ConnectionLis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;

public class MessageHandling implements ConnectionLis {

    protected static final String IP_ADDR = "192.168.246.2"; //"172.20.10.12";
    protected static final int PORT = 8095;
    public ArrayList<String> logs = new ArrayList<>();

    public Connection connection;

    public boolean CreateConnection(String login){
        try {
            connection = new Connection(this, IP_ADDR,PORT);
            Thread.sleep(1000);
        }
        catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
        return connection.CheckAliveConnect();
    }

    public void SendMes(String msg){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            msg = Base64.getEncoder().encodeToString(msg.getBytes());
        }
        connection.sendString(msg);
    }



    @Override
    public void ConnectionReady(Connection connection) {

    }

    @Override
    public void ReceiveString(Connection connection, String value) {
        byte[] actualByte = new byte[0];
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            actualByte = Base64.getMimeDecoder().decode(value);
        }
        logs.add(new String(actualByte));

    }

    @Override
    public void Disconnection(Connection connection) {
        connection.disconnected();
        logs.add("Вы отключились");
    }

    @Override
    public void Exception(Connection connection, Exception e) {
        logs.add("Connection exception : " + e);
    }


}
