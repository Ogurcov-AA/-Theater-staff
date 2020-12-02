package com.example.mobilemes.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.Classes.CheckValidDate;
import com.Classes.UserInfo;
import com.example.mobilemes.MainMenuActivity;
import com.example.mobilemes.R;
import com.Classes.InputOutputData;


import com.example.mobilemes.RegistrationActivity;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;



public class LoginActivity extends AppCompatActivity {
    CheckValidDate validDate = new CheckValidDate();
    protected UserInfo userUser = new UserInfo();
    InputOutputData inputOutputData = new InputOutputData();
    private LoginViewModel loginViewModel;
    Button loginButton;
    Button regButton;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);
        loginButton = this.findViewById(R.id.signIn);
        regButton = this.findViewById(R.id.signUp);
        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.signIn);
        final Button regButton = findViewById(R.id.signUp);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.super.getApplication(), RegistrationActivity.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              errorList = new ArrayList<>();
                InputOutputData.log.clear();
                    loading = findViewById(R.id.loading);
                    loading.setVisibility(View.VISIBLE);
                loginButton.setEnabled(false);
                regButton.setEnabled(false);
                 new ProgressTask().execute(usernameEditText.getText().toString().trim());
            }
        });
    }
    ProgressBar loading;
    ArrayList<String> errorList;

   class ProgressTask extends AsyncTask<String, Void, Void> {
    @Override
    protected Void doInBackground(String... unused) {
     errorList.clear();
        String login = unused[0];
        String tempMessage = inputOutputData.GetKeySendConnection(login);
        if(!inputOutputData.CheckAliveConnection()){
            errorList.add(getString(R.string.ErrorMsg1));
            publishProgress();
        return null;
        }
        if (tempMessage == null) {
            System.out.println(tempMessage);
            errorList.add(getString(R.string.ErrorMsg2));
            publishProgress();
            return null;
        } else {
            inputOutputData.TimerLogin("getkey");
            SystemClock.sleep(1000);
            if (!InputOutputData.flag) {
                errorList.add(getString(R.string.ErrorMsg3));
                publishProgress();
                return null;
            }
            if (SetPass()) {
                if (inputOutputData.GetUserInfo(login)) {
                } else {
                    errorList.add(getString(R.string.ErrorMsg4));
                    publishProgress();
                    return null;
                }
            } else {
                errorList.add(getString(R.string.ErrorMsg4));
                publishProgress();
                return null;
            }
        }
        Intent intent = new Intent(LoginActivity.super.getApplication(), MainMenuActivity.class);
        startActivity(intent);
        inputOutputData.NextThreadToGetUserList();
        finish();
        return null;
    }
    @Override
    protected void onProgressUpdate(Void... items) {
        if (errorList.size() > 0) {
            for (String errorMsg : errorList) {
                                 showErrorMessage(errorMsg);
            }
        }
    }
    @Override
    protected void onPostExecute(Void unused) {
            loading.setVisibility(View.INVISIBLE);
            loginButton.setEnabled(true);
            regButton.setEnabled(true);
    }
}


    protected boolean SetPass() {
        final EditText passwordEditText = findViewById(R.id.password);
        final EditText usernameEditText = findViewById(R.id.username);

        try {
            String pas = validDate.bytetoString(validDate.getHashWithSalt(passwordEditText.getText().toString().trim(), validDate.stringToByte(inputOutputData.currentKey)));
            return inputOutputData.LoginUserSendConnection(usernameEditText.getText().toString().trim(), pas);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();

        }
        return false;
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    public void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void regButtonClck(View view) {
        Intent intent = new Intent(this ,RegistrationActivity.class);
        startActivity(intent);
    }}