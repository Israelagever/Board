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
import static com.example.board.MainActivity.btnPause;

public class Game implements View.OnTouchListener {
    private BoardGame boardGame;
    Button btnSolved;
    Dialog solvedD;
    TextView tvTime, tvMoves;
    private Context context;
    SharedPreferences getSetting;
    LinearLayout l;

    boolean ifOne = true, ifStart=false, ifPause = true;
    int moves, sizeOfBoard, colorOfTile;
    Handler handler;
    RecordHelper recordHelper;


    public Game(Context context) {
        this.context = context;

        tvTime = ((Activity)context).findViewById(R.id.tvTime);
        tvMoves = ((Activity)context).findViewById(R.id.tvMoves);
        startMode();

        l = ((Activity)context).findViewById(R.id.lGame);
        l.removeView(l.getChildAt(0));

        getSetting = context.getSharedPreferences("data",0);
        update();
        moves = 0;
        createBoardGame();


    }

    public void createBoardGame(){

        if (sizeOfBoard != 0 && colorOfTile != 0)
            boardGame = new BoardGame(context,sizeOfBoard, colorOfTile);
        else
            boardGame = new BoardGame(context,4, Color.MAGENTA);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

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

                if (time.isRun)
                    tvTime.setText(String.format("%02d",time.getMinute())+":"+String.format("%02d",msg.arg2) +"."+ msg.arg1);
                if (ifStart = false)
                    tvTime.setText("00:00.0");
                return true;
            }

        });
    }
    public void startMode(){
        if (time != null) {

            time.isRun = false;
            time.handler = null;
        }

        doHandler();
        time = new Time(handler);
        tvTime.setText("00:00.0");
        tvMoves.setText("num of moves: 0");
        btnPause.setText("pause");
        btnPause.setEnabled(false);

    }
    public void runMode(){
        if (time!=null) time.isRun = true;
        btnPause.setEnabled(true);
        btnPause.setText("pause");
        ifStart = true;
        ifPause = false;
    }

    public void stopMode(){
        if (time!=null) time.isRun = false;
        btnPause.setText("continue");
        ifStart = true;
        ifPause = true;

    }


    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (ifOne){
            time.start();
            runMode();
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
                boardGame.invalidate();
            }
        }
        return true;
    }

    public void update()
    {
        int size = getSetting.getInt("size", 0);
        if (size != 0) sizeOfBoard =size;
        int color = getSetting.getInt("color", 0);
        if (color != 0) colorOfTile = color;
        recordHelper = new RecordHelper(context,"tblrecords"+sizeOfBoard, getSetting.getString("orderBy",null));

    }
    public void createSolvedDialog()
    {
        solvedD=new Dialog(context);
        solvedD.setContentView(R.layout.custom_solved);
        solvedD.setCancelable(false);
        btnSolved = solvedD.findViewById(R.id.btnSolved);
        btnSolved.setOnClickListener((View.OnClickListener) context);
        solvedD.show();
    }
}
