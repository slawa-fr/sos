package ru.frolovvv.sos;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// Проба почты-4 - OK
// https://qastack.ru/programming/2020088/sending-email-in-android-using-javamail-api-without-using-the-default-built-in-a
// Вот альтернативная версия, которая также работает для меня и имеет вложения

public class MainActivity extends AppCompatActivity {

    SharedPreferences mySP11;
    SharedPreferences mySP12;
    SharedPreferences mySP13;
    SharedPreferences mySP14;
    SharedPreferences mySP15;
    SharedPreferences mySP16;
    String SAVE_TEXT11 = "save text11";
    String SAVE_TEXT12 = "save text12";
    String SAVE_TEXT13 = "save text13";
    String SAVE_TEXT14 = "save text14";
    String SAVE_TEXT15 = "save text15";
    String SAVE_TEXT16 = "save text16";

    public static  String numberPhone1;
    public static  String numberPhone2;
    public static  String email1;
    public static  String email2;
    public static  String name;
    public static  String text;

    public boolean ttt;  // Переменная для смены картики при нажатии кнопки ImageView

    // MULTIPLE_PERMISSIONS - 1-я часть
// https://legkovopros.ru/questions/25905/kak-zaprosit-neskol-ko-razreshenij-srazu-v-android-dublikat
    private static final int RC_PERMISSION_WRITE_EXTERNAL_STORAGE = 11;
    private static final int RC_PERMISSION_READ_EXTERNAL_STORAGE = 22;
    private static final int SEND_SMS_PERMISSION_REQUEST_CODE = 111;  // переменная для отправки смс
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    private static final int MULTIPLE_PERMISSIONS = 77;

    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.SEND_SMS,
            Manifest.permission.CAMERA}; // Here i used multiple permission check
// MULTIPLE_PERMISSIONS - 1-я часть

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//Заблокировать ориентацию экрана
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
// Убрать ActionBar
        getSupportActionBar().hide();
// что-бы не включался экран блокировки
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

// MULTIPLE_PERMISSIONS - 2-я часть
// https://legkovopros.ru/questions/25905/kak-zaprosit-neskol-ko-razreshenij-srazu-v-android-dublikat
        if (checkPermissions()) {
            getCallDetails();
        }
// MULTIPLE_PERMISSIONS - 2-я часть

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.sos1);
        TextView textView21 = (TextView) findViewById(R.id.textView21);
        TextView textView22 = (TextView) findViewById(R.id.textView22);
        TextView textView23 = (TextView) findViewById(R.id.textView23);
        TextView textView24 = (TextView) findViewById(R.id.textView24);

        loadPref();
        //System.out.println(numberPhone1 + numberPhone2 + email1 + email2 + name + text);
        textView21.setText("тел. 1: " + numberPhone1);
        textView22.setText("тел. 2: " + numberPhone2);
        textView23.setText("E-mail 1: " + email1);
        textView24.setText("E-mail 2: " + email2);
    }

    // Кнопка SOS
    public void onClick1(View view) {

// Смена картинки кнопки
        if(ttt == false){
            ttt = !ttt;
            ImageView imageView = (ImageView) findViewById(R.id.imageView);
            imageView.setImageResource(R.drawable.sos2);
        }else {
            ttt = !ttt;
            ImageView imageView = (ImageView) findViewById(R.id.imageView);
            imageView.setImageResource(R.drawable.sos1);
        }

        //System.out.println(numberPhone1 + numberPhone2 + email1 + email2 + name + text);
        if(numberPhone1.equals("") && numberPhone2.equals("") && email1.equals("") && email2.equals("")){
            // не введено ни одно значение - выведем тост
            Toast.makeText(this, " нужно заполнить хотябы одно поле в настройках ", Toast.LENGTH_LONG).show();
        }   else {
            // все что нужно введено - ничего делать не нужно
            //Toast.makeText(this, " 2 нужно заполнить хотябы одно поле в настройках ", Toast.LENGTH_LONG).show();
        }

        if(!numberPhone1.equals(""))
            upSms1();
        if(!numberPhone2.equals(""))
            upSms2();
        if(!email1.equals(""))
            new AsyncRequest1().execute("123", "/ajax", "foo=bar");
        if(!email2.equals(""))
            new AsyncRequest2().execute("123", "/ajax", "foo=bar");
    }


// отправка на почту - первый адрес
// Нужно запускать в другом потоке
// https://ru.stackoverflow.com/questions/506131/%D0%9A%D0%B0%D0%BA-%D0%BF%D0%BE%D1%87%D0%B8%D0%BD%D0%B8%D1%82%D1%8C-android-os-networkonmainthreadexception

    class AsyncRequest1 extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... arg) {
            //System.out.println("Запущен другой поток - AsyncRequest");
            Mail m = new Mail("sos.mainfrend@gmail.com", "[********]");
            //System.out.println("email1=" + email1);
            String[] toArr = {email1};
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

    class AsyncRequest2 extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... arg) {
            //System.out.println("Запущен другой поток - AsyncRequest");
            Mail m = new Mail("sos.mainfrend@gmail.com", "[********]");
            //System.out.println("email2=" + email2);
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
            //System.out.println("AsyncRequest-2");
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    // MULTIPLE_PERMISSIONS - 3-я часть
// https://legkovopros.ru/questions/25905/kak-zaprosit-neskol-ko-razreshenij-srazu-v-android-dublikat
    private void getCallDetails() {
        //Log.d(LOG_TAG, " Все разрешения имеются ");
    }
// MULTIPLE_PERMISSIONS - 3-я часть


// MULTIPLE_PERMISSIONS - 4-я часть
// https://legkovopros.ru/questions/25905/kak-zaprosit-neskol-ko-razreshenij-srazu-v-android-dublikat

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(getApplicationContext(), p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permissions granted.
                    getCallDetails(); // Now you call here what ever you want :)

                } else {
                    String perStr = "";
                    for (String per : permissions) {
                        perStr += "\n" + per;
                    }
                    // permissions list of don't granted permission
                    ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, RC_PERMISSION_WRITE_EXTERNAL_STORAGE);
                    ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, RC_PERMISSION_READ_EXTERNAL_STORAGE);
                    ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
                    ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                }
                return;
            }
        }
    }
// MULTIPLE_PERMISSIONS - 4-я часть

    // метод для отправки смс - 1
    void upSms1() {
        Date da = new Date();
        final CharSequence vrem = DateFormat.format("hh:mm:ss_dd.MM.yyyy", da.getTime());
        String message = " нажата кнопка SOS " + vrem + " " + text;
        String phoneNo1 = numberPhone1;
        if (!TextUtils.isEmpty(phoneNo1)) {
            //System.out.println("1phoneNo= " + phoneNo1);
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo1, null, message, null, null);
        }else{
            //System.out.println("2phoneNo= " + phoneNo1);
//            Toast toast = Toast.makeText(getApplicationContext(), " Укажите номер телефона на который будут приходить СМС-сообщения ", Toast.LENGTH_LONG);
//            toast.show();
        }

//        Toast toast = Toast.makeText(getApplicationContext(), "Отправка SMS доступна в другой версии программы, ссылку на скачивание ищите в описании к программе", Toast.LENGTH_LONG);
//        toast.show();

// В манифесте удалена строчка:
//            <uses-permission android:name="android.permission.SEND_SMS" />
    }


    // метод для отправки смс - 2
    void upSms2() {
        Date da = new Date();
        final CharSequence vrem = DateFormat.format("hh:mm:ss_dd.MM.yyyy", da.getTime());
        String message = " нажата кнопка SOS " + vrem + " " + text;
        String phoneNo2 = numberPhone2;

        if (!TextUtils.isEmpty(phoneNo2)) {
            //System.out.println("1phoneNo= " + phoneNo2);
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo2, null, message, null, null);
        }else{
            //System.out.println("2phoneNo= " + phoneNo2);
//            Toast toast = Toast.makeText(getApplicationContext(), " Укажите номер телефона на который будут приходить СМС-сообщения ", Toast.LENGTH_LONG);
//            toast.show();
        }

    }


    void loadPref(){
        mySP11 = getSharedPreferences("MyPref", MODE_PRIVATE);
        String saveText11 = mySP11.getString(SAVE_TEXT11, "" );
        numberPhone1 = saveText11;

        mySP12 = getSharedPreferences("MyPref", MODE_PRIVATE);
        String saveText12 = mySP12.getString(SAVE_TEXT12, "" );
        numberPhone2 = saveText12;

        mySP13 = getSharedPreferences("MyPref", MODE_PRIVATE);
        String saveText13 = mySP13.getString(SAVE_TEXT13, "" );
        email1 = saveText13;

        mySP14 = getSharedPreferences("MyPref", MODE_PRIVATE);
        String saveText14 = mySP14.getString(SAVE_TEXT14, "" );
        email2 = saveText14;

        mySP15 = getSharedPreferences("MyPref", MODE_PRIVATE);
        String saveText15 = mySP15.getString(SAVE_TEXT15, "" );
        name = saveText15;

        mySP16 = getSharedPreferences("MyPref", MODE_PRIVATE);
        String saveText16 = mySP16.getString(SAVE_TEXT16, "" );
        text = saveText16;
    }

    // Кнопка - НАСТРОЙКИ
    public void onClick2(View view) {
        Intent intent = new Intent(this, Main1Activity.class);
        startActivity(intent);
        finish();
    }

}