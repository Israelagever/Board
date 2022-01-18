package com.example.board;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import static com.example.board.BoardGame.time;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    DisplayMetrics displayMetrics;
    static Button btnStart, btnSolved, btnPause;

    BoardGame boardGame;
    static TextView tvTime, tvMoves;
     int sizeOfBoard = 4;
     int colorOfTile = Color.MAGENTA;
    LinearLayout l;
    SharedPreferences getSetting;
    static Dialog solvedD;

    boolean ifStart, ifPause = false, passToIntent = false;
    static Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSetting = getSharedPreferences("settings",0);

        update();
        init();




        doHandler();


    }

    public void createBoardGame(){

        int width = displayMetrics.widthPixels;
        boardGame = new BoardGame(this, sizeOfBoard,colorOfTile);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,width);
        boardGame.setLayoutParams(params);
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
        return true;

    }

    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        update();
                        btnPause.setText("pause");
                        ifPause = false;
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

        getSetting = getSharedPreferences("settings",0);

        l = findViewById(R.id.lGame);
        createBoardGame();

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
                boardGame.setIfPause(false);

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
            @Override
            public boolean handleMessage(Message msg)
            {

                tvTime.setText(msg.arg2 +"."+ msg.arg1);
                if (ifStart) {
                    tvTime.setText("0.0");
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
        tvTime.setText("0.0");

        btnPause.setEnabled(false);
    }

    public void stopGame(){
        time.isRun = false;
        ifPause = true;
        boardGame.setIfPause(true);
    }

    public void update()
    {
        int size = getSetting.getInt("size", 0);
        if (size != 0) sizeOfBoard =size;
        int color = getSetting.getInt("color", 0);
        if (color != 0) colorOfTile = color;

    }



    @Override
    protected void onPause() {
        super.onPause();
        if (!ifPause&& !boardGame.getIfPause()) {
            stopGame();
            if (passToIntent) btnPause.setText("pause");
            else btnPause.setText("continue");
        }
    }
}