package com.example.board;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.example.board.BoardGame.blank;
import static com.example.board.BoardGame.time;

public class Game implements View.OnTouchListener, View.OnClickListener {
    private BoardGame boardGame;
    private Button btnStart, btnSolved, btnPause, btnOrder;
    Dialog solvedD;
    TextView tvTime, tvMoves;
    private Context context;
    SharedPreferences getSetting;
    LinearLayout l;

    boolean ifOne = true, ifStart, ifPause = true;
    int moves;
    Handler handler;
    RecordHelper recordHelper;


    public Game(Context context) {
        this.context = context;

        btnStart = ((Activity)context).findViewById(R.id.btnStart);
        btnPause = ((Activity)context).findViewById(R.id.btnPause);
        btnStart.setOnClickListener(this);
        btnPause.setOnClickListener(this);

        tvTime = ((Activity)context).findViewById(R.id.tvTime);
        tvMoves = ((Activity)context).findViewById(R.id.tvMoves);

        l = ((Activity)context).findViewById(R.id.lGame);
        doHandler();
        moves = 0;
        createBoardGame();

    }
    public void createBoardGame(){
        getSetting = context.getSharedPreferences("settings",0);
        int size = getSetting.getInt("size", 0);
        int color = getSetting.getInt("color", 0);
        if (size != 0 && color != 0)
            boardGame = new BoardGame(context,size,color);
        else
            boardGame = new BoardGame(context,4, Color.MAGENTA);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        boardGame = new BoardGame(context, size,color);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,width);
        boardGame.setLayoutParams(params);
        boardGame.setOnTouchListener(this);
        l.addView(boardGame);
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
                    time.isRun = false;
                    createSolvedDialog();
                    //Toast.makeText(context, "ניצחת אלוף!!", Toast.LENGTH_SHORT).show();
                    recordHelper.open();
                    String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                    Record r = new Record(moves,tvTime.getText().toString(),currentDate);
                    System.out.println(recordHelper.createRecord(r).getRecordId());



                    recordHelper.close();


                }
            }
        }
        return true;
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
                //ifPause = false;

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
    public void stopGame(){
        if (time!=null) time.isRun = false;
        ifPause = true;

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
    public void createSolvedDialog()
    {
        solvedD=new Dialog(context);
        solvedD.setContentView(R.layout.custom_solved);
        solvedD.setCancelable(false);
        btnSolved = solvedD.findViewById(R.id.btnSolved);
        btnSolved.setOnClickListener((View.OnClickListener) this);
        solvedD.show();
    }
}
