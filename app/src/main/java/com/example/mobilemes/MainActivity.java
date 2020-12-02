package com.example.mobilemes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.Classes.InputOutputData;
import com.example.mobilemes.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity {


    InputOutputData inputOutputData = new InputOutputData();
    ProgressBar progressBar;
    Button reConnectionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        reConnectionButton = this.findViewById(R.id.reConnectionButton);
        progressBar = this.findViewById(R.id.progressBar);
        isNetworkAvailable();
        new ProgressTask().execute();
     }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void reConButtonClick(View view) {
        progressBar.setVisibility(View.VISIBLE);
        reConnectionButton.setVisibility(View.INVISIBLE);
        new ProgressTask().execute();
    }


    class ProgressTask extends AsyncTask<Void, Integer, Void> {
        @Override
        protected Void doInBackground(Void... unused) {
                SystemClock.sleep(500);
            if(inputOutputData.CreateConnection()) {
                if (!inputOutputData.CheckAliveConnection()) {
                    publishProgress();
                    return null;
                }
                Intent intent = new Intent(MainActivity.super.getApplication(), LoginActivity.class);
                startActivity(intent);
                finish();
            }else
                publishProgress();
        return null;
        }
        @Override
        protected void onProgressUpdate(Integer... items) {
            Toast.makeText(MainActivity.super.getApplication(), R.string.ErrorMsg1, Toast.LENGTH_LONG).show();
            reConnectionButton.setVisibility(View.VISIBLE);
        }
        @Override
        protected void onPostExecute(Void unused) {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }


}