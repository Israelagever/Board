package com.example.board;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;


import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.View;

import android.view.WindowManager;
import android.widget.Button;


import java.util.ArrayList;
import java.util.Calendar;

import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    static Button btnStart, btnPause;
    Button btnOrder;

    SharedPreferences getSetting;
    Dialog recordsD;

    boolean passToIntent = false;//בודק אם עשינו onStop דרך intent


    RecyclerView recyclerView;

    AlarmManager alarmManager;
    PendingIntent pendingIntent;

    Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Board);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//הופף את הactivity למסך מלא

        setContentView(R.layout.activity_main);

        init();

    }
    //פעולה שמאתחלת את האובייקטים ומעדכנת את הSharedPreferences
    private void init(){

        getSetting = getSharedPreferences("data",0);
        SharedPreferences.Editor editor = getSetting.edit();

        if (getSetting.getString("orderBy",null) == null) {
            editor.putString("orderBy", "move");
            editor.commit();
        }

        if(getSetting.getBoolean("firstLaunch",true)) {
            startNotification();
            editor.putBoolean("firstLaunch", false);
            editor.commit();
        }

        btnStart = findViewById(R.id.btnStart);
        btnStart.setOnClickListener(this);
        btnPause = findViewById(R.id.btnPause);
        btnPause.setOnClickListener(this);

        game = new Game(this);


    }

    //פעולה שיוצרת את התפריט
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return true;
    }


    //כשלוחצים על אפשרות בתפריט
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.setting) {//כניסה להגדרות
            Intent intent = new Intent(MainActivity.this,SettingActivity.class);
            passToIntent = true;
            StartForResult.launch(intent);
        }

        else if (item.getItemId() == R.id.records) {//פתיחת הטבלת שיאים
            if(game.ifStart)
                game.stopMode();
            createRecordsDialog();
        }

        else if (item.getItemId() == R.id.contact) {//פתיחת המסך של ה"contact us"
            Intent intent = new Intent(MainActivity.this,SmsActivity.class);
            passToIntent = true;
            StartForResult.launch(intent);
        }
        return true;

    }

    //פעולה שמופעלת כשלוחצים על כפתור
    @SuppressLint({"SetTextI18n", "Range"})
    @Override
    public void onClick(View v) {

        if (v == btnStart) {//אם לחצו על start
            game = new Game(this);
            game.tvTime.setText("00:00.0");

        }
        else if (v == btnPause)//אם לחצו על pause
        {
            if (!game.ifPause)
                game.stopMode();
            else
                game.runMode();

        }
        else {//טיפול בכפתור הsolved
            game.solvedD.dismiss();
            game = new Game(this);

        }

    }
    ActivityResultLauncher<Intent> StartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {//חזרה מintent
                    if (result.getResultCode() == Activity.RESULT_OK) {


                        passToIntent = false;

                        game = new Game(MainActivity.this);//אתחול המשחק

                    }
                }
            });


    //הגדרת ההתראות
    public void startNotification(){

        Intent intent = new Intent(this, ReceiverNotification.class);

        //שליחה לברודקסט receiverNotification.class
        pendingIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), 234324243, intent, PendingIntent.FLAG_IMMUTABLE);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);//התחברות לservice של המערכת

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 8);//הגדרת השעה של ההתראה ל20:30
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);


        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                alarmManager.INTERVAL_DAY, pendingIntent);//הפעלת החזרה היומית
    }


    @Override
    protected void onPause() {
        super.onPause();

        if (!game.ifPause) {
            game.stopMode();

            if (passToIntent) btnPause.setText("pause");
        }
    }

    //צמצום המערך שיאים ל10 שיאים הראשונים
    public List<Record> createRecordListForShow()
    {
        game.recordHelper.open();
        ArrayList<Record> list = game.recordHelper.getAllRecord();


        //אם המערך של השיאים גדול מ10
        if (list.size()>10) {
            return list.subList(0, 10);
        }

        game.recordHelper.close();
        return list;

    }

    //יצירת הדיאלוג של השיאים
    public void createRecordsDialog()
    {

        //אתחול והגדרת הדיאלוג
        recordsD=new Dialog(this);
        recordsD.setContentView(R.layout.custom_dialog_records);
        recordsD.setCancelable(true);

        //אתחול הrecyclerView
        recyclerView = (RecyclerView) recordsD.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //יצירת adapter עם list של השיאים
        RecordAdapter recordAdapter = new RecordAdapter(this, createRecordListForShow());

        recyclerView.setAdapter(recordAdapter);
        recordsD.show();


        btnOrder = recordsD.findViewById(R.id.btnOrder);
        if(game.recordHelper.getOrderBy().equals("time")) btnOrder.setText("order by time");
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = getSetting.edit();

                //שינוי מסידור לפי מהלכים לסידור לפי זמן
                if (getSetting.getString("orderBy",null).equals("move")) {
                    editor.putString("orderBy", "time");
                    btnOrder.setText("order by time");
                }
                else {//שינוי מסידור לפי זמן לסידור לפי מהלכים
                    editor.putString("orderBy", "move");
                    btnOrder.setText("order by move");
                }
                //אתחול הטבלה לפי ההגדרה החדשה
                editor.commit();
                //עדכון הסידור החדש והצגתו בדיאלוג
                game.update();
                RecordAdapter recordAdapter = new RecordAdapter(MainActivity.this,
                        createRecordListForShow());
                recyclerView.setAdapter(recordAdapter);
                recordsD.show();

            }
        });

    }

}