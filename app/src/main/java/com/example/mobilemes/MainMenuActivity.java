package com.example.mobilemes;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Classes.MessageHandling;
import com.Classes.PrivateMessage;
import com.Classes.UserInfo;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainMenuActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        getSupportActionBar().hide();
        InitialazeView();
        Bundle arguments = getIntent().getExtras();
        if( arguments != null){
            setDataAboutMePanel();
            log.setText(arguments.getString("publicChat"));
            SetButtonToSettingChat();
            publicButtonChat.performClick();
        }else {
        CheckConnection();
        setDataAboutMePanel();
        homeButtonClick(homeButton);
        SetButtonToSettingChat();
        }
    }

    public void CheckConnection(){
        if(tt.CreateConnection(loginLabel.getText().toString().trim())){
         }
        if(privateDialogs.CreateConnection()){
        }
        CheckInputPrivateMsg(true);
        CheckInputMsg(true);
    }

    public Button homeButton;
    public Button publicButtonChat;
    public TextView loginLabel;
    public TextView NameLabel;
    public TextView countryLabel;
    public TextView EmailLabel;
    public TextView bDayLabel;
    public View aboutMePanel;
    public View publicChatPanel;
    public TextView log;
    public TextView sayTextField;
    public TextView loginInPublicChat;
    public ListView PeopleListView;
    public TextView searchTextField;
    public View searchPeoplePanel;
    public View  privateChatPanel;
    public Button searchPeopleButton;
    public Button privateChatButton;
    public TextView privateMSGLoginLabel;
    public TextView privateMsgTextArea;
    public View UserInfoPanel;
    public TextView sendPrivateMsg;
    public TextView UserBDayLabel;
    public TextView UserEmailLabel;
    public TextView UserCountryLabel;
    public TextView UserNameLabel;
    public TextView UserLoginLabel;

    public void InitialazeView(){
        homeButton = this.findViewById(R.id.homeButton);
        publicButtonChat = this.findViewById(R.id.publicChatButton);
        loginLabel = this.findViewById(R.id.loginLabel);
        NameLabel = this.findViewById(R.id.NameLabel);
        countryLabel = this.findViewById(R.id.countryLabel);
        EmailLabel = this.findViewById(R.id.EmailLabel);
        bDayLabel = this.findViewById(R.id.bDayLabel);
        aboutMePanel = this.findViewById(R.id.aboutMe);
        publicChatPanel = this.findViewById(R.id.publicChatPanel);
        log = this.findViewById(R.id.log);
        log.setMovementMethod(new ScrollingMovementMethod());
        sayTextField = this.findViewById(R.id.sayTextField);
        loginInPublicChat = this.findViewById(R.id.loginInPublicChat);
        PeopleListView = this.findViewById(R.id.PeopleListView);
        searchTextField = this.findViewById(R.id.searchTextField);
        searchPeoplePanel = this.findViewById(R.id.searchPeoplePanel);
        searchPeopleButton = this.findViewById(R.id.searchPeopleButton);
        privateChatPanel = this.findViewById(R.id.privateChatPanel);
        privateChatButton = this.findViewById(R.id.privateChatButton);
        privateMSGLoginLabel = this.findViewById(R.id.privateMSGLoginLabel);
        privateMsgTextArea = this.findViewById(R.id.privateMsgTextArea);
        privateMsgTextArea.setMovementMethod(new ScrollingMovementMethod());
        UserInfoPanel = this.findViewById(R.id.UserInfoPanel);
        sendPrivateMsg = this.findViewById(R.id.sendPrivateMsg);
        UserBDayLabel = this.findViewById(R.id.UserBDayLabel);
        UserEmailLabel = this.findViewById(R.id.UserEmailLabel);
        UserCountryLabel = this.findViewById(R.id.UserCountryLabel);
        UserNameLabel = this.findViewById(R.id.UserNameLabel);
        UserLoginLabel = this.findViewById(R.id.UserLoginLabel);
        SetListView();
    }

    PrivateMessage privateDialogs = new PrivateMessage();
    ArrayList<String> currentDialog = new ArrayList<>();
    MessageHandling tt = new MessageHandling();
    Timer timer = new java.util.Timer();
    Timer timerToPrivateMSG;
    String TwoLogins;


    private void setDataAboutMePanel(){
        loginLabel.setText(UserInfo.currentUser.getLogin());
        NameLabel.setText(UserInfo.currentUser.getSurname() + " " + UserInfo.currentUser.getName());
        countryLabel.setText(UserInfo.currentUser.getCountry());
        EmailLabel.setText(UserInfo.currentUser.getMail());
        bDayLabel.setText(UserInfo.currentUser.getbDay());
        loginInPublicChat.setText(UserInfo.currentUser.getLogin());
    }

    public static void buttonEffect(View button){
        button.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
    }

    public void homeButtonClick(View view) {
        buttonEffect(view);
        publicChatPanel.setVisibility(View.INVISIBLE);

        searchPeoplePanel.setVisibility(View.INVISIBLE);
        UserInfoPanel.setVisibility(View.INVISIBLE);
        aboutMePanel.setVisibility(View.VISIBLE);
        homeButton.setEnabled(false);
        publicButtonChat.setEnabled(true);
       }

    public void publicButtonChatClick(View view) {
        buttonEffect(view);
        homeButton.setEnabled(true);
        publicChatPanel.setVisibility(View.VISIBLE);
        searchPeoplePanel.setVisibility(View.INVISIBLE);
        UserInfoPanel.setVisibility(View.INVISIBLE);
        aboutMePanel.setVisibility(View.INVISIBLE);
        searchPeopleButton.setEnabled(true);
        publicButtonChat.setEnabled(false);
        UserInfoPanel.setVisibility(View.INVISIBLE);
        privateChatPanel.setVisibility(View.INVISIBLE);
        privateChatButton.setEnabled(true);
    }

    public void SearchButtonClick(View view ) {
        buttonEffect(view);
        searchPeoplePanel.setVisibility(View.VISIBLE);
        searchPeopleButton.setEnabled(false);
        homeButton.setEnabled(true);
        aboutMePanel.setVisibility(View.VISIBLE);
        UserInfoPanel.setVisibility(View.INVISIBLE);
        publicChatPanel.setVisibility(View.INVISIBLE);
        aboutMePanel.setVisibility(View.INVISIBLE);
        SetListView();
        publicButtonChat.setEnabled(true);
        privateChatPanel.setVisibility(View.INVISIBLE);
        privateChatButton.setEnabled(true);
    }

    public void privateChatButtonClick(View view){
        buttonEffect(view);
        if(privateMSGLoginLabel.getText().equals("") || !searchPeopleButton.isEnabled()){
            return;
        }
        privateChatPanel.setVisibility(View.VISIBLE);
        privateChatButton.setEnabled(false);
        homeButton.setEnabled(true);
        searchPeopleButton.setEnabled(true);
        publicButtonChat.setEnabled(true);
        searchPeopleButton.setEnabled(true);
        aboutMePanel.setVisibility(View.INVISIBLE);
        publicChatPanel.setVisibility(View.INVISIBLE);
        searchPeoplePanel.setVisibility(View.INVISIBLE);
        UserInfoPanel.setVisibility(View.INVISIBLE);
    }

    public void privateDialogsButtonClick(View view){
        privateDialogs.CreateConnection();
        TwoLogins = UserLoginLabel.getText().toString().trim();
        privateDialogs.SendLoginToGetKey(UserInfo.currentUser.getLogin().trim(),TwoLogins);
        privateMSGLoginLabel.setText(UserLoginLabel.getText().toString().trim());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        privateDialogs.InputArrayWithFile(currentDialog);
        for(String temp : currentDialog)
            privateMsgTextArea.append(temp + System.getProperty("line.separator"));
        privateChatButtonClick(view);
    }

    public void SetButtonToSettingChat() {
        Spinner chatSettings = this.findViewById(R.id.chatSettings);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.ChatSettings));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chatSettings.setAdapter(adapter);
        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 1: {
                        log.setText("");
                        chatSettings.setSelection(0);
                        break;
                    }
                    case 2: {
                        tt.Disconnection(tt.connection);
                        chatSettings.setSelection(0);
                        break;
                    }

                    case 3: {
                        tt.Disconnection(tt.connection);
                        tt.CreateConnection(loginLabel.getText().toString().trim());
                        chatSettings.setSelection(0);
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        chatSettings.setOnItemSelectedListener(itemSelectedListener);
    }

    public void sendMsgButtonClick(View view) {
        String msg = sayTextField.getText().toString().trim();
        if(msg.equals("")) return;
        sayTextField.setText("");
        tt.SendMes(UserInfo.currentUser.getLogin().trim() + " : " + msg);
    }


    public void EnterPrivateTextField(View view){
        String msg = sendPrivateMsg.getText().toString().trim();
        if(msg.equals("")) return;
        sendPrivateMsg.setText("");
        privateDialogs.SendMes(UserInfo.currentUser.getLogin().trim() + " : " + msg);
        privateMsgTextArea.append(UserInfo.currentUser.getLogin().trim() + " : " + msg + System.getProperty("line.separator"));
    }

    public void CheckInputMsg(boolean flag) {
        Runnable task = () -> {
            timer.schedule(new TimerTask() {
                public void run() {
                    if(flag == false)
                        return;
                    if(tt.logs.size()!=0){
                        for(int i= 0;i<tt.logs.size();i++){
                            printMsg(tt.logs.get(i));
                            tt.logs.remove(tt.logs.get(i));
                        }
                    }
                }
            }, 0, 200);
        };
        Thread myThread = new Thread(task, "timer");
        myThread.start();
    }

    /////////////////////////////

    private static final int PERMISSION_REQUEST_CODE = 200;
    private boolean checkPermission() {

        return ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                ;
    }

    private void requestPermissionAndContinue() {
        if (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, WRITE_EXTERNAL_STORAGE)
                    && ActivityCompat.shouldShowRequestPermissionRationale(this, READ_EXTERNAL_STORAGE)) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                alertBuilder.setCancelable(true);
                alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(MainMenuActivity.this, new String[]{WRITE_EXTERNAL_STORAGE
                                , READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                    }
                });
                AlertDialog alert = alertBuilder.create();
                alert.show();
                Log.e("", "permission denied, show dialog");
            } else {
                ActivityCompat.requestPermissions(MainMenuActivity.this, new String[]{WRITE_EXTERNAL_STORAGE,
                        READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
        } else {
            openActivity();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (permissions.length > 0 && grantResults.length > 0) {

                boolean flag = true;
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        flag = false;
                    }
                }
                if (flag) {
                    openActivity();
                } else {
                    finish();
                }
            } else {
                finish();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
//cddc
    private void openActivity() {
    }
    /////////////////////////////

    public void CheckInputPrivateMsg(boolean flag) {
        timerToPrivateMSG  = new Timer();
        if (!checkPermission()) {
            openActivity();
        } else {
            if (checkPermission()) {
                requestPermissionAndContinue();
            } else {
                openActivity();
            }
        }
        int index = 0;
        Runnable task = () -> {
            timerToPrivateMSG.schedule(new TimerTask() {
                public void run() {
                    if(!flag)
                        return;
                    if(privateDialogs.logs.size()!=0){

                        for(int i= 0;i<privateDialogs.logs.size();i++){
                            System.out.println(i + " ) " + privateDialogs.logs.get(i));
                                if(privateDialogs.logs.get(i).trim().equals("/yOuRloGin")){
                                    privateDialogs.sendMyLogin("/mYlOGin " + UserInfo.currentUser.getLogin());
                                    privateDialogs.logs.remove(privateDialogs.logs.get(i));
                                    break;
                                }
                            else if(privateDialogs.logs.get(i).substring(0,privateDialogs.logs.get(i).indexOf(' ')).equals("/getPrivateMSGkey")){
                                privateDialogs.CheckDialogKey(privateDialogs.logs.get(i).substring(privateDialogs.logs.get(i).indexOf(' ')+1));
                                privateDialogs.logs.remove(privateDialogs.logs.get(i));
                               System.out.println("timerDelete");
                                return;
                            }
                            else if(privateDialogs.logs.get(i).substring(0,privateDialogs.logs.get(i).indexOf(' ')).equals("/pmLogToKey")){
                                String msg = privateDialogs.logs.get(i).substring(privateDialogs.logs.get(i).indexOf(' ')+1);
                                System.out.println(msg);
                                printPrivateMsg(msg.substring(msg.indexOf(' ')+1));
                                privateDialogs.InputLogFile(msg);
                                privateDialogs.logs.remove(privateDialogs.logs.get(i));
                              break;
                            }
                            else if(privateDialogs.logs.get(i).substring(0,privateDialogs.logs.get(i).indexOf(' ')).equals("/pmLogToKeySuccess")){
                                privateDialogs.logs.remove(privateDialogs.logs.get(i));
                                System.out.println(privateDialogs.logs.size());

                                break;
                            }
                                else if(privateDialogs.logs.get(i).substring(0,privateDialogs.logs.get(i).indexOf(' ')).equals("/pm")){
                                    String tempMsg = privateDialogs.logs.get(i).substring(privateDialogs.logs.get(i).indexOf(' ')+1);

                                    if(privateChatPanel.getVisibility() == View.VISIBLE
                                            && privateDialogs.GetCurrentkey().equals(tempMsg.substring(0,tempMsg.indexOf(' ')))) {
                                        printPrivateMsg(tempMsg.substring(tempMsg.indexOf(' ') + 1));
                                        privateDialogs.InputLogFile(privateDialogs.logs.get(i));
                                    }
                                    else{
                                        if(privateChatPanel.getVisibility() == View.INVISIBLE || !MainMenuActivity.this.hasWindowFocus()) {
                                            tempMsg = tempMsg.substring(tempMsg.indexOf(' ')+1);
                                            if (!(tempMsg.substring(0, tempMsg.indexOf(' ')).equals(loginInPublicChat.getText().toString().trim()))) {
                                                showPrivateNotification(getString(R.string.logo), tempMsg.substring(0, tempMsg.indexOf(' ')) + getString(R.string.privateNotification));
                                            }
                                        }
                                        privateDialogs.InputLogFile(privateDialogs.logs.get(i));
                                    }
                                    privateDialogs.logs.remove(privateDialogs.logs.get(i));
                                    break;
                                }
                            else if(privateDialogs.logs.get(i).equals("ok") || privateDialogs.logs.get(i).equals("error")){
                                privateDialogs.logs.remove(privateDialogs.logs.get(i));
                               break;
                            }
                        }
                    }
                }
            }, 0, 100);
        };
        Thread myThread = new Thread(task, "timer");
        myThread.start();
    }

    public synchronized void printMsg(String msg) {
    new Thread((Runnable)()->{

    //    SpannableStringBuilder builder = new SpannableStringBuilder();
   //     SpannableString str1 = new SpannableString(msg + System.getProperty("line.separator"));
        if(publicChatPanel.getVisibility() == View.INVISIBLE || !MainMenuActivity.this.hasWindowFocus()) {
            if (!(msg.substring(0, msg.indexOf(" ")).equals(loginInPublicChat.getText().toString().trim()))) {
                showNotification(getString(R.string.logo) + " " +getString(R.string.publicChat), msg);
            }
        }
     /*    if (msg.substring(0, msg.indexOf(" ")).equals(loginInPublicChat.getText().toString().trim())) {
            str1.setSpan(new ForegroundColorSpan(Color.RED), 0, str1.length(), 0);
        }
        builder.append(log.getText());
        builder.append(str1);
        log.setText(builder, TextView.BufferType.SPANNABLE);
       */
     log.append(msg + System.getProperty("line.separator"));

        }).start();
        }

    public synchronized void printPrivateMsg(String msg) {
        new Thread((Runnable)()-> {
       //     System.out.println(msg.substring(0, msg.indexOf(':')));
       //     SpannableStringBuilder builder = new SpannableStringBuilder();

        //    SpannableString str1 = new SpannableString(msg + System.getProperty("line.separator"));
        //    str1.setSpan(new ForegroundColorSpan(Color.RED), 0, str1.length(), 0);
        //    builder.append(privateMsgTextArea.getText());
         //   builder.append(str1);
          //  privateMsgTextArea.setText(builder, TextView.BufferType.SPANNABLE);
      //  } else{
       //    */
                privateMsgTextArea.append(msg + System.getProperty("line.separator"));
           // }
            }).start();
        }

    public void SetListView(){
        ArrayList<String> userList = new ArrayList<String>();

        ArrayAdapter<?> adapter1 = new ArrayAdapter<>(this,R.layout.list_view_cutstom,R.id.label,userList);
        if(searchTextField.getText().toString().trim().equals("") ) {
            for (int i = 0; i < UserInfo.UserList.size(); i++) {
                userList.add(UserInfo.UserList.get(i).getLogin());
            }
        }
        PeopleListView.setAdapter(adapter1);
        PeopleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < UserInfo.UserList.size(); i++) {
                    if (UserInfo.UserList.get(i).getLogin().equals(PeopleListView.getItemAtPosition(position))) {
                        UserLoginLabel.setText(UserInfo.UserList.get(i).getLogin());
                        UserNameLabel.setText(UserInfo.UserList.get(i).getName() + " " + UserInfo.UserList.get(i).getSurname());
                        UserEmailLabel.setText(UserInfo.UserList.get(i).getMail());
                        UserCountryLabel.setText(UserInfo.UserList.get(i).getCountry());
                        UserBDayLabel.setText(UserInfo.UserList.get(i).getbDay());
                        searchPeoplePanel.setVisibility(View.INVISIBLE);
                        searchPeopleButton.setEnabled(true);
                        UserInfoPanel.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    private static String CHANNEL_ID = "MobileMess";
    private int INDEX = 1;
    public void showNotification(String title, String msg) {

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "public chat";
            String Description = "public chat message";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = null;
            mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);

        notificationManager.createNotificationChannel(mChannel);
    }
                NotificationCompat.Builder builder =
                new NotificationCompat.Builder(MainMenuActivity.this, CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentTitle(title)
                        .setContentText(msg)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true);

        Intent resultIntent = new Intent(this, MainMenuActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                resultIntent.putExtra("publicChat", log.getText().toString());
                stackBuilder.addParentStack(MainMenuActivity.class);
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(resultPendingIntent);


        notificationManager.notify(INDEX, builder.build());
        INDEX++;
    }

    public void showPrivateNotification(String title, String msg) {


        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "private chat";
            String Description = "private chat message";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = null;
            mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);

            notificationManager.createNotificationChannel(mChannel);
        }
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(MainMenuActivity.this, CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentTitle(title)
                        .setContentText(msg)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true);

        notificationManager.notify(INDEX, builder.build());
        INDEX++;
    }

}
