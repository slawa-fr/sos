package ru.frolovvv.sos;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
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


// Прижать последний LinearLayout к низу экрана:
//                android:layout_weight="1"
//                android:gravity="center|bottom"

public class MainActivity extends AppCompatActivity {

// Включить GPS если он не включен-1
// https://ru.stackoverflow.com/questions/499751/%D0%94%D0%B8%D0%B0%D0%BB%D0%BE%D0%B3-%D1%81-%D0%BF%D1%80%D0%B5%D0%B4%D0%BB%D0%BE%D0%B6%D0%B5%D0%BD%D0%B8%D0%B5%D0%BC-%D0%B2%D0%BA%D0%BB%D1%8E%D1%87%D0%B8%D1%82%D1%8C-gps
    private LocationManager locationManager;
    public static boolean geolocationEnabled = false;

    private long startTime = 0L;
    private Handler customHandler = new Handler();
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;

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
    public static  String message;
    public double latGps;
    public double lonGps;
    public boolean ttt;  // Переменная для смены картики при нажатии кнопки ImageView

// MULTIPLE_PERMISSIONS - 1-я часть
// https://legkovopros.ru/questions/25905/kak-zaprosit-neskol-ko-razreshenij-srazu-v-android-dublikat
    private static final int RC_PERMISSION_WRITE_EXTERNAL_STORAGE = 11;
    private static final int RC_PERMISSION_READ_EXTERNAL_STORAGE = 22;
    private static final int SEND_SMS_PERMISSION_REQUEST_CODE = 111;  // переменная для отправки смс
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    private static final int MY_ACCESS_FINE_LOCATION = 33;
    private static final int MY_ACCESS_COARSE_LOCATION = 44;
    private static final int MULTIPLE_PERMISSIONS = 77;

    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.SEND_SMS,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION}; // Here i used multiple permission check}; // Here i used multiple permission check
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

// Включить GPS если он не включен-2
        checkLocationServiceEnabled();

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

// Широта и долшота
        TextView textView2a = (TextView) findViewById(R.id.textView2a);
        TextView textView3a = (TextView) findViewById(R.id.textView3a);

// gps
// http://streletzcoder.ru/rabotaem-s-gps-v-android-na-java/

        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // округление до 3-х знаков используя метод roundAvoid
                double lat = roundAvoid(location.getLatitude(), 3);
                double lon = roundAvoid(location.getLongitude(), 3);
                TextView textView2a = (TextView) findViewById(R.id.textView2a);
                textView2a.setText(String.valueOf(lat));
                TextView textView3a = (TextView) findViewById(R.id.textView3a);
                textView3a.setText(String.valueOf(lon));
                latGps = lat;
                lonGps = lon;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };

        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Проверка наличия разрешений
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        }
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
        manager.requestSingleUpdate(LocationManager.GPS_PROVIDER, listener, null);

        //polzSogl();
    }

// Сообщение о том что Пользовательское соглашение нарушено
    void polzSogl(){

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Внимание!")
                .setMessage("Приложение использует определение координат в фоновом режиме. (The app uses coordinate detection in the background)" )
                .setIcon(R.drawable.sos1)
                .setCancelable(false)
                .setNegativeButton("ОК",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

// Округление чисел: Метод Math.round()
// https://www.internet-technologies.ru/articles/kak-v-java-okruglit-chislo-do-n-znakov-posle-zapyatoy.html

    public static double roundAvoid(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
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

// Старт таймера
        ButtonStart();

        if(!numberPhone1.equals(""))
            upSms1();
        if(!numberPhone2.equals(""))
            upSms2();
        if(!email1.equals(""))
            new AsyncRequest1().execute("123", "/ajax", "foo=bar");
        if(!email2.equals(""))
            new AsyncRequest2().execute("123", "/ajax", "foo=bar");
    }


// Кнопка Старт
    public void onClickStart(View view) {
        ButtonStart();
    }

// метод   старт
    void ButtonStart() {
// таймер
// https://www.cyberforum.ru/android-dev/thread1919287.html
        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);
    }

// Кнопка стоп
    public void onClickStop(View view) {
        ButtonStop();
    }

// метод  стоп
    void ButtonStop() {
        startTime = 0L;
        customHandler.removeCallbacks(updateTimerThread);
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
            m.setSubject(name + " - " + text);

            Date da = new Date();
            final CharSequence vrem = DateFormat.format("hh:mm:ss_dd.MM.yyyy", da.getTime());
            TextView textView2a = (TextView) findViewById(R.id.textView2a);
            String tempA = textView2a.getText().toString();
            //System.out.println("tempA= " + tempA);
            if(tempA.equals("широта")){
                // координат еще нет
                m.setBody(name + " - " + text);
            }else {
                // координаты есть
                m.setBody("https://www.google.ru/maps/place/" + latGps + "n," + lonGps + "e");
            }

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
            m.setSubject(name + " - " + text);

            Date da = new Date();
            final CharSequence vrem = DateFormat.format("hh:mm:ss_dd.MM.yyyy", da.getTime());
            TextView textView2a = (TextView) findViewById(R.id.textView2a);
            String tempA = textView2a.getText().toString();
            //System.out.println("tempA= " + tempA);
            if(tempA.equals("широта")){
                // координат еще нет
                m.setBody(name + " - " + text);
            }else {
                // координаты есть
                m.setBody("https://www.google.ru/maps/place/" + latGps + "n," + lonGps + "e");
            }

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
                    ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, MY_ACCESS_FINE_LOCATION);
                    ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, MY_ACCESS_COARSE_LOCATION);
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
        TextView textView2a = (TextView) findViewById(R.id.textView2a);
        String tempA = textView2a.getText().toString();
        //System.out.println("tempA= " + tempA);
        if(tempA.equals("широта")){
            // координат еще нет
            message = " нажата кнопка SOS " + vrem + " " + name + " " + text;
        }else {
            // координаты есть
            message = "https://www.google.ru/maps/place/" + latGps + "n," + lonGps + "e";
        }
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
        TextView textView2a = (TextView) findViewById(R.id.textView2a);
        String tempA = textView2a.getText().toString();
        //System.out.println("tempA= " + tempA);
        if(tempA.equals("широта")){
            // координат еще нет
            message = " нажата кнопка SOS " + vrem + " " + name + " " + text;
        }else {
            // координаты есть
            message = "https://www.google.ru/maps/place/" + latGps + "n," + lonGps + "e";
        }
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


// Таймер

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000);
            TextView textView2 = (TextView) findViewById(R.id.textView2);
            //textView2.setText("" + mins + ":" + String.format("%02d", secs) + ":" + String.format("%03d", milliseconds));
            textView2.setText("" + mins + ":" + String.format("%02d", secs));

            customHandler.postDelayed(this, 0);

            if (secs >= 10) {   // step
// Сканируем GPS Каждые 3 секунды
                startTime = 0L;
                customHandler.removeCallbacks(updateTimerThread);
                //scanGps();
                CheckKoordinat();
//                startTime = SystemClock.uptimeMillis();
//                customHandler.postDelayed(updateTimerThread, 0);
            }
        }
    };

// Проверка, что координаты определились и выводятся в текстовые поля
    void CheckKoordinat(){
        TextView textView2a = (TextView) findViewById(R.id.textView2a);
        String tempA = textView2a.getText().toString();
        System.out.println("tempA= " + tempA);

        if(tempA.equals("широта")){
            // координат еще нет
            startTime = SystemClock.uptimeMillis();
            customHandler.postDelayed(updateTimerThread, 0);
            System.out.println("координат еще нет_tempA= " + tempA);

        }else {
            // координаты есть
            System.out.println("координаты есть_tempA= " + tempA);
            ButtonStop();
            if(!numberPhone1.equals(""))
                upSms1();
            if(!numberPhone2.equals(""))
                upSms2();
            if(!email1.equals(""))
                new AsyncRequest1().execute("123", "/ajax", "foo=bar");
            if(!email2.equals(""))
                new AsyncRequest2().execute("123", "/ajax", "foo=bar");
        }

    }

// Включить GPS если он не включен-3
    private boolean checkLocationServiceEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            geolocationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        return buildAlertMessageNoLocationService(geolocationEnabled);
    }

// Включить GPS если он не включен-4
    private boolean buildAlertMessageNoLocationService(boolean network_enabled) {
        String msg = !network_enabled ? getResources().getString(R.string.msg_switch_network) : null;

        if (msg != null) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false)
                    .setMessage(msg)
                    .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
            return true;
        }
        return false;
    }
}