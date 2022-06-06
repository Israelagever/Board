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
import android.graphics.Color;


import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.MotionEvent;
import android.view.View;

import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;


import java.util.ArrayList;
import java.util.Calendar;

import java.util.List;



import static com.example.board.BoardGame.time;



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

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//הופף את הactivity למסך מלא

        setContentView(R.layout.activity_main);

        init();

    }
    private void init(){//פעולה שמאתחלת את האובייקטים ומעדכנת את הSharedPreferences

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
        game.update();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {//פעולה שיוצרת את התפריט

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_setting,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {//כשלוחצים על אפשרות בתפריט
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



    public void startNotification(){//הגדרת ההתראות

        Intent intent = new Intent(this, receiverNotification.class);
        pendingIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), 234324243, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);


        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 20);//הגדרת השעה של ההתראה ל20:30
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








    public List<Record> createRecordListForShow()//צמצום המערךנ שיאים ל10 שיאים הראשונים
    {
        game.recordHelper.open();
        ArrayList<Record> oldList = game.recordHelper.getAllRecord();
        ArrayList<Record> newList = new ArrayList<>();
        if (oldList.size()>10) {
            for (int i = 0; i < 10; i++) {

                newList.add(oldList.get(i));
            }
            game.recordHelper.close();
            return newList;
        }
        else return oldList;

    }
    public void createRecordsDialog()//יצירת הדיאלוג של השיאים
    {

        recordsD=new Dialog(this);
        recordsD.setContentView(R.layout.custom_dialog_records);
        recordsD.setCancelable(true);
        recyclerView = (RecyclerView) recordsD.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
                game.update();
                game.recordHelper.open();
                RecordAdapter recordAdapter = new RecordAdapter(MainActivity.this,
                        createRecordListForShow());
                recyclerView.setAdapter(recordAdapter);
                recordsD.show();
                game.recordHelper.close();
            }
        });

    }

}