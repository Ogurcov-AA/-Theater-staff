package com.example.mobilemes;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Classes.CheckValidDate;
import com.Classes.InputOutputData;
import com.example.mobilemes.ui.login.LoginActivity;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.time.LocalDate.now;

public class RegistrationActivity extends AppCompatActivity {
    CheckValidDate validDate = new CheckValidDate();

    InputOutputData inputOutputData = new InputOutputData();
    ArrayList<String> ErrorList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(this, R.array.diaps_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        initialize();
        spinner.setAdapter(adapter);

        //   spinner.setPrompt(R.string.CountryTitle);
        spinner.setSelection(0);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    TextView lognTextFieldR;
    TextView nameTextBoxR;
    TextView surnameTextBoxR;
    TextView emailTextFieldR;
    TextView bDayR;
    TextView passFieldR;
    TextView passFieldTwoR;
    Spinner spinner;
    Button RegButton;
    ProgressBar progressBar;
    RadioButton genderButtonMaleR;
    RadioButton genderButtonWomanR;

    public void initialize() {
        lognTextFieldR = this.findViewById(R.id.RegLoginEditText);
        nameTextBoxR = this.findViewById(R.id.RegNameEditText);
        surnameTextBoxR = this.findViewById(R.id.RegSurnameEditText);
        emailTextFieldR = this.findViewById(R.id.RegEmailEditText);
        bDayR = this.findViewById(R.id.RegBdayEditTextDate);
        passFieldR = this.findViewById(R.id.RegPasswordEditText);
        passFieldTwoR = this.findViewById(R.id.RegPasswordEditText2);
        spinner = this.findViewById(R.id.spiner);
        RegButton = this.findViewById(R.id.acceptRegButton);
        progressBar = this.findViewById(R.id.progressBar2);
        genderButtonMaleR = this.findViewById(R.id.radioButtonMale);
        genderButtonWomanR = this.findViewById(R.id.radioButtonWomen);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)

    public void regButtonClck(View view) {

            progressBar.setVisibility(View.VISIBLE);
            RegButton.setEnabled(false);
         new ProgressTask().execute();
    }

    class ProgressTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... unu) {
            if (!checkFieldIsEmty()) {
                publishProgress();
                return null;
            }
            if (!passFieldR.getText().toString().trim().equals(passFieldTwoR.getText().toString().trim())) {
                ErrorList.add("Пароли не совпадают");
                publishProgress();
                return null;
            }
            LocalDate currentDate = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                currentDate = now();
            }
            String pass;
            String msg = "";
            String salt;
            SystemClock.sleep(500);
            try {
                salt = validDate.generateSalt();
                pass = validDate.bytetoString(validDate.getHashWithSalt(passFieldR.getText().toString().trim(), validDate.stringToByte(salt)));
                msg = lognTextFieldR.getText().toString().trim() + " " + pass + " " + salt + " " +
                        nameTextBoxR.getText().toString().trim() + " " + surnameTextBoxR.getText().toString().trim() + " " +
                        spinner.getSelectedItem().toString().trim() + " " + emailTextFieldR.getText().toString().trim() + " " +
                        bDayR.getText() + " " + currentDate + " ";

                if (genderButtonMaleR.isSelected()) {
                    msg += genderButtonMaleR.getText();
                } else msg += genderButtonWomanR.getText();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            if (!inputOutputData.OutputDate(msg)) {
                publishProgress();
            }
            if (!CheckField()) {
                publishProgress();
                return null;
            }
        return null;
        }
        @Override
        protected void onProgressUpdate(Void... items) {
            showErrorMessage("Error", "Ошибка регистрации");

        }
        @Override
        protected void onPostExecute(Void unused) {
            progressBar.setVisibility(View.INVISIBLE);
            RegButton.setEnabled(true);

        }
    }

    boolean checkFieldIsEmty() {
        TextView lognTextFieldR = this.findViewById(R.id.RegLoginEditText);
        TextView nameTextBoxR = this.findViewById(R.id.RegNameEditText);
        TextView surnameTextBoxR =this.findViewById(R.id.RegSurnameEditText);
        // TextView selectedCountryR = this.findViewById(R.id.Re);
        TextView emailTextFieldR = this.findViewById(R.id.RegEmailEditText);
        TextView bDayR = this.findViewById(R.id.RegBdayEditTextDate);
        TextView passFieldR = this.findViewById(R.id.RegPasswordEditText);
        TextView passFieldTwoR = this.findViewById(R.id.RegPasswordEditText2);
        Spinner spinner = this.findViewById(R.id.spiner);



        if (CheckTextField(lognTextFieldR)) {
            if (CheckTextField(nameTextBoxR)) {
                if (CheckTextField(surnameTextBoxR)) {
                    if (CheckTextField(emailTextFieldR)) {
                        if (CheckTextField(passFieldR)) {
                            if (CheckTextField(passFieldTwoR)) {
                                if (spinner.getSelectedItem() != null) {
                                    if (validDate.CheckCorrectEmail(emailTextFieldR.getText().toString().trim())) {
                                          if (bDayR.getText() != null) {
                                        return true;
                                    } else {
                                       bDayR.setTextColor(Color.RED);
                                       showErrorMessage("Ошибка ввода", "Не все поля заполнены");
                                        return false;
                                    }
                                }else {
                                        lognTextFieldR.setTextColor(Color.RED);
                                        showErrorMessage("Ошибка ввода", "Формат ввода ivanov@gmail.com");
                                    return false;
                                    }
                                }
                                    else {
                                    showErrorMessage("Ошибка ввода", "Не все поля заполнены");
                                    return false;
                                }
                            } else {
                                showErrorMessage("Ошибка ввода", "Не все поля заполнены");
                                return false;
                            }
                        } else {
                            showErrorMessage("Ошибка ввода", "Не все поля заполнены");
                            return false;
                        }
                    } else {
                        showErrorMessage("Ошибка ввода", "Не все поля заполнены");
                        return false;
                    }
                } else {
                    showErrorMessage("Ошибка ввода", "Не все поля заполнены");
                    return false;
                }
            } else {
                showErrorMessage("Ошибка ввода", "Не все поля заполнены");
                return false;
            }
        } else {
            showErrorMessage("Ошибка ввода", "Не все поля заполнены");
            return false;
        }
    }

    public boolean CheckField() {
        TextView lognTextFieldR = this.findViewById(R.id.RegLoginEditText);
        TextView nameTextBoxR = this.findViewById(R.id.RegNameEditText);
        TextView surnameTextBoxR = this.findViewById(R.id.RegSurnameEditText);
        // TextView selectedCountryR = this.findViewById(R.id.Re);
        TextView emailTextFieldR = this.findViewById(R.id.RegEmailEditText);
        TextView bDayR = this.findViewById(R.id.RegBdayEditTextDate);
        TextView passFieldR = this.findViewById(R.id.RegPasswordEditText);
        TextView passFieldTwoR = this.findViewById(R.id.RegPasswordEditText2);
        Thread thread = new Thread((Runnable) () -> {
            validDate.CheckRepeatLogin(lognTextFieldR.getText().toString().trim());
            inputOutputData.TimerLogin("validLogin");
            // inputOutputData.TimerLogin("validLogin");
            System.out.println(InputOutputData.flagL);
            if (!InputOutputData.flagL)
                ErrorList.add("Пользователь с таким логином существует");
            validDate.CheckRepeatEmail(emailTextFieldR.getText().toString());
            inputOutputData.TimerLogin("validEmail");
            System.out.println(InputOutputData.flagE);
            if (!InputOutputData.flagE)
                ErrorList.add("Пользователь с таким email существует");
        });
        thread.start();

        if (thread.isAlive()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (InputOutputData.flagL && InputOutputData.flagE)
            return true;
        else return false;
    }

    private void ShowErrorList(){
        for (String item:ErrorList  ) {
            showErrorMessage("Error", item);
        }
        ErrorList.clear();

    }


    public void showErrorMessage(String Teg, String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    boolean CheckTextField(Object object) {
        if (object instanceof TextView) {
            if ((((TextView) object).getText().toString().trim().isEmpty())) {
                showErrorMessage("Error", "Не все поля заполнены");
                ((TextView) object).setTextColor(Color.RED);
                return false;
            } else {

            return true;
            }
        }
        return false;
    }

}
