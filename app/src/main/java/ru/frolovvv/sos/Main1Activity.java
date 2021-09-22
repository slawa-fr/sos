package ru.frolovvv.sos;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.Date;

public class Main1Activity extends AppCompatActivity {

    SharedPreferences mySP11;   //для сохранения значения в Preferences переменной
    SharedPreferences mySP12;   //для сохранения значения в Preferences переменной
    SharedPreferences mySP13;   //для сохранения значения в Preferences переменной
    SharedPreferences mySP14;   //для сохранения значения в Preferences переменной
    SharedPreferences mySP15;   //для сохранения значения в Preferences переменной
    SharedPreferences mySP16;   //для сохранения значения в Preferences переменной
    String SAVE_TEXT11 = "save text11";   //для сохранения значения в Preferences
    String SAVE_TEXT12 = "save text12";   //для сохранения значения в Preferences
    String SAVE_TEXT13 = "save text13";   //для сохранения значения в Preferences
    String SAVE_TEXT14 = "save text14";   //для сохранения значения в Preferences
    String SAVE_TEXT15 = "save text15";   //для сохранения значения в Preferences
    String SAVE_TEXT16 = "save text16";   //для сохранения значения в Preferences

    public static  String numberPhone1;
    public static  String numberPhone2;
    public static  String email1;
    public static  String email2;
    public static  String name;
    public static  String text;

    private AdView adView;                 // рекламный баннер
    private AdView adView2;                 // рекламный баннер

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);


// Рекламный баннер - начало
        //MobileAds.initialize(this, "Идентификатор приложения");
        MobileAds.initialize(this, "ca-app-pub-6255050935881113~8895524538");
        adView = findViewById(R.id.adView);
        AdRequest adRequest1 = new AdRequest.Builder().build();
        adView.loadAd(adRequest1);
// Рекламный баннер - конец

// Рекламный баннер - начало
        //MobileAds.initialize(this, "Идентификатор приложения");
        MobileAds.initialize(this, "ca-app-pub-6255050935881113~8895524538");
        adView2 = findViewById(R.id.adView2);
        AdRequest adRequest2 = new AdRequest.Builder().build();
        adView2.loadAd(adRequest2);
// Рекламный баннер - конец


//Заблокировать ориентацию экрана
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
// Убрать ActionBar
        getSupportActionBar().hide();
// убрать фокус - спрятать экранную клавиатуру
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
// что-бы не включался экран блокировки
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        EditText editText11 = (EditText) findViewById(R.id.editText11);
        EditText editText11a = (EditText) findViewById(R.id.editText11a);
        EditText editText12 = (EditText) findViewById(R.id.editText12);
        EditText editText12a = (EditText) findViewById(R.id.editText12a);
        EditText editText13 = (EditText) findViewById(R.id.editText13);
        EditText editText14 = (EditText) findViewById(R.id.editText14);

        Button sms1 = (Button) findViewById(R.id.sms1);
        Button sms2 = (Button) findViewById(R.id.sms2);
        Button email1 = (Button) findViewById(R.id.email1);
        Button email2 = (Button) findViewById(R.id.email2);
        Button buttonBack = (Button) findViewById(R.id.buttonBack);

        loadPref();

// Убрать фокус
        editText11.clearFocus();
        editText11a.clearFocus();
        editText12.clearFocus();
        editText12a.clearFocus();
        editText13.clearFocus();
        editText14.clearFocus();
    }

// Кнопка проверить отправку смс 1
    public void onClickSms1(View view) {
// проверка на введен ли правильно телефон
// https://fooobar.com/questions/293373/android-email-validation-on-edittext
        EditText editText11 = (EditText) findViewById(R.id.editText11);
        CharSequence temp_emilID=editText11.getText().toString();
        if(!isValidPhone(temp_emilID))
        {
            editText11.requestFocus();
            editText11.setError("введите номер телефона");
            Toast.makeText(this, " Укажите номер телефона ", Toast.LENGTH_LONG).show();
        }
        else
        {
// после ввода номера телефона и нажатии на кнопку проверить - клавиатура убирается с экрана
// https://qna.habr.com/q/50240
            //EditText editText12 = (EditText) findViewById(R.id.editText12);
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText11.getWindowToken(), 0);
            savePref();
            upSms1();
            //Toast.makeText(this, " если нужно отправлять СМС, скачайте версию программы с яндекс-диска, ссылка в описании ", Toast.LENGTH_LONG).show();
        }
    }

// Кнопка проверить отправку смс 2
    public void onClickSms2(View view) {
// проверка на введен ли правильно телефон
        EditText editText11a = (EditText) findViewById(R.id.editText11a);
        CharSequence temp_emilID=editText11a.getText().toString();
        if(!isValidPhone(temp_emilID))
        {
            editText11a.requestFocus();
            editText11a.setError("введите номер телефона");
            Toast.makeText(this, " Укажите номер телефона ", Toast.LENGTH_LONG).show();
        }
        else
        {
// после ввода номера телефона и нажатии на кнопку проверить - клавиатура убирается с экрана
// https://qna.habr.com/q/50240
            //EditText editText12 = (EditText) findViewById(R.id.editText12);
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText11a.getWindowToken(), 0);
            savePref();
            upSms2();
            //Toast.makeText(this, " если нужно отправлять СМС, скачайте версию программы с яндекс-диска, ссылка в описании ", Toast.LENGTH_LONG).show();
        }
    }

// проверка на введен ли правильно телефон
    public final static boolean isValidPhone(CharSequence target) {
        return !TextUtils.isEmpty(target) && Patterns.PHONE.matcher(target).matches();
    }



// метод для отправки смс, срабатывает когда выбран радиобуттон sms и есть движение вкадре
    void upSms1() {
        Date da = new Date();
        final CharSequence vrem = DateFormat.format("hh:mm:ss_dd.MM.yyyy", da.getTime());
        String message = " нажата кнопка SOS " + vrem + " " + text;
        String phoneNo1 = numberPhone1;
        if (!TextUtils.isEmpty(phoneNo1)) {
            System.out.println("1phoneNo= " + phoneNo1);
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo1, null, message, null, null);
        }else{
            System.out.println("2phoneNo= " + phoneNo1);
//            Toast toast = Toast.makeText(getApplicationContext(), " Укажите номер телефона на который будут приходить СМС-сообщения ", Toast.LENGTH_LONG);
//            toast.show();
        }

//        Toast toast = Toast.makeText(getApplicationContext(), "Отправка SMS доступна в другой версии программы, ссылку на скачивание ищите в описании к программе", Toast.LENGTH_LONG);
//        toast.show();

// В манифесте удалена строчка:
//            <uses-permission android:name="android.permission.SEND_SMS" />
    }


    // метод для отправки смс, срабатывает когда выбран радиобуттон sms и есть движение вкадре
    void upSms2() {
        Date da = new Date();
        final CharSequence vrem = DateFormat.format("hh:mm:ss_dd.MM.yyyy", da.getTime());
        String message = " нажата кнопка SOS " + vrem + " " + text;
        String phoneNo2 = numberPhone2;

        if (!TextUtils.isEmpty(phoneNo2)) {
            System.out.println("1phoneNo= " + phoneNo2);
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo2, null, message, null, null);
        }else{
            System.out.println("2phoneNo= " + phoneNo2);
//            Toast toast = Toast.makeText(getApplicationContext(), " Укажите номер телефона на который будут приходить СМС-сообщения ", Toast.LENGTH_LONG);
//            toast.show();
        }

    }

    public void onClickEmail1(View view) {
        savePref();
        EditText editText12 = (EditText) findViewById(R.id.editText12);
        email1 = editText12.getText().toString();
        new AsyncRequest1().execute("123", "/ajax", "foo=bar");
    }

    public void onClickEmail2(View view) {
        savePref();
        EditText editText12a = (EditText) findViewById(R.id.editText12a);
        email2 = editText12a.getText().toString();
        new AsyncRequest2().execute("123", "/ajax", "foo=bar");
    }

// Нужно запускать в другом потоке
// https://ru.stackoverflow.com/questions/506131/%D0%9A%D0%B0%D0%BA-%D0%BF%D0%BE%D1%87%D0%B8%D0%BD%D0%B8%D1%82%D1%8C-android-os-networkonmainthreadexception

    class AsyncRequest1 extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... arg) {
            //System.out.println("Запущен другой поток - AsyncRequest");
            Mail m = new Mail("sos.mainfrend@mail.ru", "Yflt;ysqGfhjkm12");
            System.out.println("email1=" + email1);
            String[] toArr = {email1};
            m.setTo(toArr);
            m.setFrom("sos.mainfrend@mail.ru");
            m.setSubject("SOS");
            m.setBody("Это Я " + name + " - " + text);
// Вложение файла заремлено
            try {
                //m.addAttachment(strFilePathA);
                if(m.send()) {
                    //Toast.makeText(this, "Email was sent successfully.", Toast.LENGTH_LONG).show();
                    System.out.println("Email was sent successfully");
                } else {
                    //Toast.makeText(this, "Email was not sent.", Toast.LENGTH_LONG).show();
                    System.out.println("Email was not sent.");
                }
            } catch(Exception e) {
                //Toast.makeText(this, "There was a problem sending the email.", Toast.LENGTH_LONG).show();
                Log.e("MailApp", "Could not send email", e);
                System.out.println("Could not send email");
            }
            return arg[0];
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            TextView WebTest = (TextView) findViewById(R.id.webTest);
//            WebTest.setText(s);
            //System.out.println("AsyncRequest-2");
        }
    }

// Нужно запускать в другом потоке
// https://ru.stackoverflow.com/questions/506131/%D0%9A%D0%B0%D0%BA-%D0%BF%D0%BE%D1%87%D0%B8%D0%BD%D0%B8%D1%82%D1%8C-android-os-networkonmainthreadexception

    class AsyncRequest2 extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... arg) {
            //System.out.println("Запущен другой поток - AsyncRequest");
            Mail m = new Mail("sos.mainfrend@gmail.com", "[********]");
            System.out.println("email2=" + email2);
            String[] toArr = {email2};
            m.setTo(toArr);
            m.setFrom("sos.mainfrend@gmail.com");
            m.setSubject("SOS");
            m.setBody("Это Я " + name + " - " + text);
// Вложение файла заремлено
            try {
                //m.addAttachment(strFilePathA);
                if(m.send()) {
                    //Toast.makeText(this, "Email was sent successfully.", Toast.LENGTH_LONG).show();
                    System.out.println("Email was sent successfully");
                } else {
                    //Toast.makeText(this, "Email was not sent.", Toast.LENGTH_LONG).show();
                    System.out.println("Email was not sent.");
                }
            } catch(Exception e) {
                //Toast.makeText(this, "There was a problem sending the email.", Toast.LENGTH_LONG).show();
                Log.e("MailApp", "Could not send email", e);
                System.out.println("Could not send email");
            }
            return arg[0];
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            TextView WebTest = (TextView) findViewById(R.id.webTest);
//            WebTest.setText(s);
            //System.out.println("AsyncRequest-2");
        }
    }


//для сохранения значения в Preferences
    void savePref(){
        mySP11 = getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor ed1 = mySP11.edit();
        EditText editText11 = (EditText) findViewById(R.id.editText11);
        ed1.putString(SAVE_TEXT11,editText11.getText().toString());

        mySP12 = getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor ed2 = mySP12.edit();
        EditText editText11a = (EditText) findViewById(R.id.editText11a);
        ed2.putString(SAVE_TEXT12,editText11a.getText().toString());

        mySP13 = getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor ed3 = mySP13.edit();
        EditText editText12 = (EditText) findViewById(R.id.editText12);
        ed3.putString(SAVE_TEXT13,editText12.getText().toString());

        mySP14 = getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor ed4 = mySP14.edit();
        EditText editText12a = (EditText) findViewById(R.id.editText12a);
        ed4.putString(SAVE_TEXT14,editText12a.getText().toString());

        mySP15 = getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor ed5 = mySP15.edit();
        EditText editText13 = (EditText) findViewById(R.id.editText13);
        ed5.putString(SAVE_TEXT15,editText13.getText().toString());

        mySP16 = getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor ed6 = mySP16.edit();
        EditText editText14 = (EditText) findViewById(R.id.editText14);
        ed6.putString(SAVE_TEXT16,editText14.getText().toString());

        ed1.apply();
        ed2.apply();
        ed3.apply();
        ed4.apply();
        ed5.apply();
        ed6.apply();
    }

    void loadPref(){
        mySP11 = getSharedPreferences("MyPref", MODE_PRIVATE);
        String saveText11 = mySP11.getString(SAVE_TEXT11, "" );
        EditText editText11 = (EditText) findViewById(R.id.editText11);
        editText11.setText(saveText11);
        numberPhone1 = editText11.getText().toString();

        mySP12 = getSharedPreferences("MyPref", MODE_PRIVATE);
        String saveText12 = mySP12.getString(SAVE_TEXT12, "" );
        EditText editText11a = (EditText) findViewById(R.id.editText11a);
        editText11a.setText(saveText12);
        numberPhone2 = editText11a.getText().toString();

        mySP13 = getSharedPreferences("MyPref", MODE_PRIVATE);
        String saveText13 = mySP13.getString(SAVE_TEXT13, "" );
        EditText editText12 = (EditText) findViewById(R.id.editText12);
        editText12.setText(saveText13);
        email1 = editText12.getText().toString();

        mySP14 = getSharedPreferences("MyPref", MODE_PRIVATE);
        String saveText14 = mySP14.getString(SAVE_TEXT14, "" );
        EditText editText12a = (EditText) findViewById(R.id.editText12a);
        editText12a.setText(saveText14);
        email2 = editText12a.getText().toString();

        mySP15 = getSharedPreferences("MyPref", MODE_PRIVATE);
        String saveText15 = mySP15.getString(SAVE_TEXT15, "" );
        EditText editText13 = (EditText) findViewById(R.id.editText13);
        editText13.setText(saveText15);
        name = editText13.getText().toString();

        mySP16 = getSharedPreferences("MyPref", MODE_PRIVATE);
        String saveText16 = mySP16.getString(SAVE_TEXT16, "" );
        EditText editText14 = (EditText) findViewById(R.id.editText14);
        editText14.setText(saveText16);
        text = editText14.getText().toString();
    }

// Кнопка - Сохранить
    public void onClick4(View view) {
        savePref();
    }

// кнопка назад (к активити №1)3
    public void onClick3(View view) {
        savePref();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed(){
        savePref();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}