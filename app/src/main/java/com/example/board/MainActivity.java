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

import android.view.View;

import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;


import java.util.ArrayList;
import java.util.Calendar;

import java.util.List;



import static com.example.board.BoardGame.time;



public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    static Button btnStart, btnPause, btnOrder;



     int sizeOfBoard = 4;
     int colorOfTile = Color.MAGENTA;

    SharedPreferences getSetting;
    Dialog recordsD;

    boolean passToIntent = false;


    RecordHelper recordHelper;

    RecyclerView recyclerView;

    AlarmManager alarmManager;
    PendingIntent pendingIntent;

    Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        getSetting = getSharedPreferences("settings",0);
        if (getSetting.getString("orderBy",null) == null) {
            SharedPreferences.Editor editor = getSetting.edit();
            editor.putString("orderBy", "move");
            editor.commit();
        }

        startNotification();

        update();

        init();
        //doHandler();
    }
    private void init(){
        btnStart = findViewById(R.id.btnStart);
        btnStart.setOnClickListener(this);
        btnPause = findViewById(R.id.btnPause);
        btnPause.setOnClickListener(this);
        game = new Game(this);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_setting,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.setting) {
            Intent intent = new Intent(MainActivity.this,SettingActivity.class);
            passToIntent = true;
            mStartForResult.launch(intent);


            return true;
        }
        else if (item.getItemId() == R.id.records)
            createRecordsDialog();
        return true;

    }
    @SuppressLint({"SetTextI18n", "Range"})
    @Override
    public void onClick(View v) {




        if (v == btnStart) {


            game.tvTime.setText("00:00.0");
            game = new Game(this);

        }
        else if (v == btnPause)
        {


            if (!game.ifPause)
                game.stopMode();
            else
                game.runMode();

        }
        else {
            game.solvedD.dismiss();
            game = new Game(this);

        }

    }
    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {


                        passToIntent = false;
                        game = new Game(MainActivity.this);
                    }
                }
            });



    public void startNotification(){

        Intent intent = new Intent(this, receiverNotification.class);
        pendingIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), 234324243, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);


        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 20);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);


        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                alarmManager.INTERVAL_DAY, pendingIntent);

    }


    public void update()
    {
        int size = getSetting.getInt("size", 0);
        if (size != 0) sizeOfBoard =size;
        int color = getSetting.getInt("color", 0);
        if (color != 0) colorOfTile = color;
        recordHelper = new RecordHelper(this,"tblrecords"+sizeOfBoard, getSetting.getString("orderBy",null));

    }



    @Override
    protected void onPause() {
        super.onPause();

        if (!game.ifPause) {
            game.stopMode();
            /*
            if (passToIntent) btnPause.setText("pause");
            else btnPause.setText("continue");

             */
        }
    }








    public List<Record> createRecordListForShow()
    {
        recordHelper.open();
        ArrayList<Record> oldList = recordHelper.getAllRecord();
        ArrayList<Record> newList = new ArrayList<>();
        if (oldList.size()>10) {
            for (int i = 0; i < 10; i++) {

                newList.add(oldList.get(i));
            }
            recordHelper.close();
            return newList;
        }
        else return oldList;

    }
    public void createRecordsDialog()
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
        if(recordHelper.getOrderBy().equals("time")) btnOrder.setText("order by time");
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = getSetting.edit();
                if (getSetting.getString("orderBy",null).equals("move")) {
                    editor.putString("orderBy", "time");
                    btnOrder.setText("order by time");
                }
                else {
                    editor.putString("orderBy", "move");
                    btnOrder.setText("order by move");
                }
                editor.commit();
                update();
                recordHelper.open();
                RecordAdapter recordAdapter = new RecordAdapter(MainActivity.this, createRecordListForShow());
                recyclerView.setAdapter(recordAdapter);
                recordsD.show();
                recordHelper.close();
            }
        });

    }

}