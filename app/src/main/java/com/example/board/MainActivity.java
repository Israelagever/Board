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
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.example.board.BoardGame.blank;
import static com.example.board.BoardGame.time;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {
    DisplayMetrics displayMetrics;
    Button btnStart, btnSolved, btnPause, btnOrder;

    BoardGame boardGame;
    TextView tvTime, tvMoves;
     int sizeOfBoard = 4;
     int colorOfTile = Color.MAGENTA;
    LinearLayout l;
    SharedPreferences getSetting;
    Dialog solvedD,recordsD;
    int moves;
    boolean ifStart, ifPause = true, passToIntent = false, ifOne = true;
    Handler handler;

    RecordHelper RecordHelper;
    ArrayList<Record> records;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSetting = getSharedPreferences("settings",0);
        if (getSetting.getString("orderBy",null) == null) {
            SharedPreferences.Editor editor = getSetting.edit();
            editor.putString("orderBy", "move");
            editor.commit();
        }

        update();
        init();
        doHandler();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void createBoardGame(){

        int width = displayMetrics.widthPixels;
        boardGame = new BoardGame(this, sizeOfBoard,colorOfTile);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,width);
        boardGame.setLayoutParams(params);
        boardGame.setOnTouchListener(this);
        l.addView(boardGame);
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
//          if (!ifPause && !boardGame.getIfPause()) stopGame();
            passToIntent = true;
            mStartForResult.launch(intent);


            return true;
        }
        else if (item.getItemId() == R.id.records)
            createRecordsDialog();
        return true;

    }

    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        update();
                        btnPause.setText("pause");
                        ifPause = true;
                        ifStart = true;
                        passToIntent = false;
                        resetGame();
                    }
                }
            });



    private void init(){

        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);


        btnStart = findViewById(R.id.btnStart);
        btnStart.setOnClickListener(this);
        tvTime = findViewById(R.id.tvTime);
        tvMoves = findViewById(R.id.tvMoves);

        btnPause = findViewById(R.id.btnPause);
        btnPause.setOnClickListener(this);



        l = findViewById(R.id.lGame);
        createBoardGame();




        moves = 0;


    }

    @SuppressLint({"SetTextI18n", "Range"})
    @Override
    public void onClick(View v) {




        if (v == btnStart) {
            resetGame();
            ifStart = true;
            if(ifPause)
            {
                btnPause.setText("pause");
                ifPause = false;

            }

        }
        else if (v == btnPause)
        {


            if (!ifPause) {
                stopGame();
                btnPause.setText("continue");
            }

            else {
                time.isRun = true;
                btnPause.setText("pause");
                ifPause = false;


            }
        }
        else {
            resetGame();
            ifStart = true;
            solvedD.dismiss();
        }

    }

    public void doHandler(){
        handler=new Handler(new Handler.Callback() {
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            @Override
            public boolean handleMessage(Message msg)
            {

                tvTime.setText(String.format("%02d",time.getMinute())+":"+String.format("%02d",msg.arg2) +"."+ msg.arg1);
                if (ifStart) {
                    tvTime.setText("00:00.0");
                    ifStart = false;
                }
                return true;
            }

        });
    }

    public void resetGame()
    {
        l.removeView(boardGame);
        createBoardGame();
        if (time != null)
            time.isRun = false;
        tvMoves.setText("num of moves: 0");
        tvTime.setText("00:00.0");
        moves = 0;
        btnPause.setEnabled(false);
        ifOne = true;
    }

    public void stopGame(){
        if (time!=null) time.isRun = false;
        ifPause = true;

    }

    public void update()
    {
        int size = getSetting.getInt("size", 0);
        if (size != 0) sizeOfBoard =size;
        int color = getSetting.getInt("color", 0);
        if (color != 0) colorOfTile = color;
        RecordHelper = new RecordHelper(this,"tblrecords"+sizeOfBoard, getSetting.getString("orderBy",null));

    }



    @Override
    protected void onPause() {
        super.onPause();
        if (!ifPause) {
            stopGame();
            if (passToIntent) btnPause.setText("pause");
            else btnPause.setText("continue");
        }
    }

    public void createRecordsDialog()
    {
        RecordHelper.open();
        recordsD=new Dialog(this);
        recordsD.setContentView(R.layout.custom_dialog_records);
        recordsD.setCancelable(true);
        recyclerView = (RecyclerView) recordsD.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        RecordAdapter recordAdapter = new RecordAdapter(this, RecordHelper.getAllRecord());
        recyclerView.setAdapter(recordAdapter);
        recordsD.show();
        RecordHelper.close();

        btnOrder = recordsD.findViewById(R.id.btnOrder);
        if(RecordHelper.getOrderBy().equals("time")) btnOrder.setText("order by time");
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
                RecordHelper.open();
                RecordAdapter recordAdapter = new RecordAdapter(MainActivity.this, RecordHelper.getAllRecord());
                recyclerView.setAdapter(recordAdapter);
                recordsD.show();
                RecordHelper.close();
            }
        });

    }
    public void createSolvedDialog()
    {
        solvedD=new Dialog(this);
        solvedD.setContentView(R.layout.custom_solved);
        solvedD.setCancelable(false);
        btnSolved = solvedD.findViewById(R.id.btnSolved);
        btnSolved.setOnClickListener((View.OnClickListener) this);
        solvedD.show();
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (ifOne){
            time = new Time(handler);
            time.start();
            btnPause.setEnabled(true);
            ifPause = false;
        }

        ifOne = false;

        if (!ifPause) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                Square mySquare = boardGame.findSquare(event.getX(), event.getY());
                //Square target = blank();
                if (mySquare != null && boardGame.checkBlank(event.getX(), event.getY())) {
                    boardGame.slide(mySquare, blank);

                    moves++;
                    tvMoves.setText("num of moves: " + moves);
                }
                boardGame.invalidate();

                if (boardGame.isWin()) {
                    //Toast.makeText(context, "ניצחת אלוף!!", Toast.LENGTH_SHORT).show();
                    RecordHelper.open();
                    String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                    Record r = new Record(moves,tvTime.getText().toString(),currentDate);
                    System.out.println(RecordHelper.createRecord(r).getRecordId());


                    records = RecordHelper.getAllRecord();
                    if (records.size() > 0) {
                        Log.d("data1", records.toString());
                    }
                    RecordHelper.close();
                    time.isRun = false;
                    createSolvedDialog();

                }
            }
        }
        return true;
    }
}