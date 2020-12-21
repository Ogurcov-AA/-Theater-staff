package com.Classes;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.example.lib.Connection;
import com.example.lib.ConnectionLis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class PrivateMessage implements ConnectionLis {

    protected static final String IP_ADDR = "192.168.246.2"; //"172.20.10.12";
    protected static final int PORT = 8015;
    public Connection connection;
    private String Currentkey;
    public String GetCurrentkey(){
        return Currentkey;
    }
    public ArrayList<String> logs  = new ArrayList<>();
    private final String pathToLog = Environment.getExternalStorageDirectory() + "/Android/data/MobileMes/";


    public PrivateMessage(){
    }

   public boolean CreateConnection(){
       try {
           connection = new Connection(this, IP_ADDR,PORT);
           Thread.sleep(1000);
       }
       catch (IOException | InterruptedException ex) {
           ex.printStackTrace();
       }
       return connection.CheckAliveConnect();
    }

    public void CheckDialogKey(String msg){
        Currentkey = msg;
        //String path = Environment.getDataDirectory().toString()+"/cache/";

        File externalAppDir = new File(Environment.getExternalStorageDirectory() + "/Android/data/MobileMes/");
        if (!externalAppDir.exists()) {
            externalAppDir.mkdir();
        }

        File[] listDir = externalAppDir.listFiles();
      int strCount = -1;
        try {
             Log.d("Files", "Size: "+ listDir.length);
        for (int i = 0; i < listDir.length; i++)
        {
            Log.d("Files", "FileName:" + listDir[i].getName());
        }

            for (File s : listDir) {
                if (s.isFile() && s.getName().equals(msg + ".txt")) {
                    strCount = CountLogStr();
                }
            }

        }catch (Exception e){
            System.out.println("Array = null");
        }
        if(strCount==-1){
            File file = new File(externalAppDir , msg + ".txt");
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        SendCountStr(strCount);
    }

    private int CountLogStr(){
        int  strCount = 0;
        try {
            BufferedReader buff = new BufferedReader(new InputStreamReader(new FileInputStream(pathToLog+Currentkey+".txt")));
            while(buff.readLine()!=null){
                strCount++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) { }
        return strCount;
    }

    public void SendLoginToGetKey(String CurrentUserLogin, String Login){
        String msg = "getkeyDialog " + CurrentUserLogin + " " + Login;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            msg = Base64.getEncoder().encodeToString(msg.getBytes());
        }
        connection.sendString(msg);
    }

    private void SendCountStr(int counter){
        String msg = "counterInFile " + counter + " " + Currentkey;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            msg = Base64.getEncoder().encodeToString(msg.getBytes());
        }
        connection.sendString(msg);
    }

    public Timer timerS;

    public void InputLogFile(String msg){
        String key = msg.substring(0,msg.indexOf(' '));
        msg = msg.substring(msg.indexOf(' ')+1);
        try {
            BufferedWriter buff = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pathToLog + key + ".txt",true)));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                msg = Base64.getEncoder().encodeToString(msg.getBytes());
            }
            buff.write(msg + System.getProperty("line.separator"));
            buff.flush();
            buff.close();
            System.out.println("+++");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void InputArrayWithFile(ArrayList<String> temp) {
     temp.clear();
        try {
            BufferedReader buff = new BufferedReader(new InputStreamReader(new FileInputStream(pathToLog + Currentkey + ".txt")));
            String line;
            while ((line = buff.readLine()) != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    line = new String(Base64.getMimeDecoder().decode(line.getBytes()));
                }
                temp.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            }
    }

    public void SendMes(String msg){
        msg = "/pm " + Currentkey + " " + msg;
        System.out.println(msg);
        Date date = new Date();
        DateFormat tt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        InputLogFile(msg.substring(msg.indexOf(' ')+1) + " (" + tt.format(date) + ")");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            msg = Base64.getEncoder().encodeToString(msg.getBytes());
        }
        connection.sendString(msg);
    }

    public void sendMyLogin(String msg){
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
        System.out.println("In program :  " + new String(actualByte));
    }

    @Override
    public void Disconnection(Connection connection) {
        System.out.println("Disconnected");
    }

    @Override
    public void Exception(Connection connection, Exception e) {

    }
}
