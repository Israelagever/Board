package com.example.board;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowInsets;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import static com.example.board.BoardGame.time;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnApplyWindowInsetsListener {

    static Button btnReset, btnStart, btnSolved;

    BoardGame boardGame;
    static TextView tvTime, tvMoves;
     int sizeOfBoard = 4;
     int colorOfTile = Color.MAGENTA;
    LinearLayout l;

    Dialog solvedD;

    boolean ifStart;
    static Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        boardGame = new BoardGame(this, sizeOfBoard,colorOfTile);

        l.addView(boardGame);

        doHandler();


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
                intent.putExtra("size",sizeOfBoard);
                //intent.putExtra("color",colorOfTile);
//                startActivity(intent);
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
                        Intent intent = result.getData();
                        int size = intent.getExtras().getInt("size");
                        sizeOfBoard =size;
                        int color = intent.getExtras().getInt("color");
                        int b = Color.GREEN;
                        colorOfTile = color;
                        ifStart = true;
                        resetGame();
                    }
                }
            });



    private void init(){

        btnStart = findViewById(R.id.btnStart);
        btnStart.setOnClickListener(this);
        tvTime = findViewById(R.id.tvTime);
        tvMoves = findViewById(R.id.tvMoves);
        btnReset = findViewById(R.id.btnReset);
        btnReset.setOnClickListener(this);

        btnReset.setOnApplyWindowInsetsListener(this);


        l = findViewById(R.id.lGame);

    }

    @Override
    public void onClick(View v) {


        if (v == btnReset) {
            resetGame();

        }
        else if (v==boardGame){
            Time time = new Time(handler);
            time.start();
        }
        else if (v == btnStart) {
            resetGame();
            ifStart = true;
        }
        else {
            resetGame();
            ifStart = true;
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
        boardGame = new BoardGame(this, sizeOfBoard,colorOfTile);
        if (time != null)
            time.isRun = false;
        tvMoves.setText("num of moves: 0");
        tvTime.setText("0.0");
        l.addView(boardGame);
        btnReset.setVisibility(View.INVISIBLE);

    }

    public void createSolvedDialog()
    {
        solvedD=new Dialog(this);
        solvedD.setContentView(R.layout.custom_solved);
        solvedD.setCancelable(false);
        btnSolved = solvedD.findViewById(R.id.btnSolved);
        btnSolved.setOnClickListener(this);
        solvedD.show();


    }


    @Override
    public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
        if(view.getVisibility() == View.VISIBLE){
            createSolvedDialog();
        }
        return null;
    }
}