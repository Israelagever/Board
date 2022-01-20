package com.example.board;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;

import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Random;


import static com.example.board.MainActivity.btnPause;
import static com.example.board.MainActivity.btnSolved;
import static com.example.board.MainActivity.handler;
import static com.example.board.MainActivity.solvedD;


public class BoardGame extends View {

    boolean ifOne1 = true, ifOne2 = true, ifPause;
    int size,color;

    int[] theSort;
    Random r;
    int all;

    Tile[][]tiles;
    Square[][]squares;

    Context context;

    Square blank;
    int moves;

    static Time time;
    public BoardGame(Context context,int size,int color) {
        super(context);
        this.context = context;
        this.size = size;
        this.color = color;
        squares = new Square[size][size];
        tiles = new Tile[size][size];
        r = new Random();
        all = size*size;
        theSort = new int[all];
        ifPause = true;


        //this.setBackgroundColor(Color.parseColor("#393939"));

    }

    public void setIfPause(boolean ifPause) {
        this.ifPause = ifPause;
    }
    public boolean getIfPause() {return ifPause;}

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBoard(canvas);


    }

    public void resetArr(int[] arr){
        Arrays.fill(arr, -1);
    }

    public void swap(){
        int temp;
        for (int i = 0;i<all;i++) {

            do {
                temp = r.nextInt(all);
            }while (findInArray(temp,theSort));

            theSort[i] = temp;
        }
    }

    public boolean findInArray(int n,int[] arr)
    {
        for (int j : arr) {
            if (j == n)
                return true;

        }
        return false;
    }

    public boolean isSolvable(int[] puzzle)
    {
        int parity = 0;
        int gridWidth = (int) Math.sqrt(puzzle.length);
        int row = 0; // the current row we are on
        int blankRow = 0; // the row with the blank tile

        for (int i = 0; i < puzzle.length; i++)
        {
            if (i % gridWidth == 0) { // advance to next row
                row++;
            }
            if (puzzle[i] == 0) { // the blank tile
                blankRow = row; // save the row on which encountered
                continue;
            }
            for (int j = i + 1; j < puzzle.length; j++)
            {
                if (puzzle[i] > puzzle[j] && puzzle[j] != 0)
                {
                    parity++;
                }
            }
        }

        if (gridWidth % 2 == 0) { // even grid
            if (blankRow % 2 == 0) { // blank on odd row; counting from bottom
                return parity % 2 == 0;
            } else { // blank on even row; counting from bottom
                return parity % 2 != 0;
            }
        } else { // odd grid
            return parity % 2 == 0;
        }
    }


    public void drawBoard(Canvas canvas){
        if(ifOne1) {

            //int count =1;
            do {
                resetArr(theSort);
                swap();
            }while (!isSolvable(theSort));

            int count = 1;
            int x = 10;
            int y = 10;
            int e = canvas.getWidth(),f = canvas.getHeight();
            int h = ((canvas.getWidth()-10)/size)-10;
            int w = ((canvas.getWidth()-10)/size)-10;

            Paint dark = new Paint();
            dark.setStrokeWidth(15);
            dark.setColor(Color.MAGENTA);
/*
            for (int i = 0; i < size; i++) {
                canvas.drawLine(0, i * (x+h), getWidth(), i * (y+w), dark);
                canvas.drawLine(i * (x+h), 0, i * (x+h), getHeight(), dark);
            }
            moves=0;

 */

            for (int i = 0; i < squares.length; i++) {
                for (int j = 0; j < squares.length; j++) {


                    /*
                    if(i==size-1&&j==size-1){
                        squares[i][j] = new Square(this, x, y, w, h, color,tiles[i][j],count);
                        break;
                    }

                     */

                    if (theSort[count - 1] != 0) {

                        tiles[i][j] = new Tile(this, x, y, w, h, theSort[count - 1], color);
                        tiles[i][j].draw(canvas);

                    }
                    squares[i][j] = new Square(this, x, y, w, h,tiles[i][j],count);
                    x = x + w + 10;
                    count++;

                }
                y = y + h + 10;
                x = 10;
            }
        }
        ifOne1 = false;
        blank = blank();
        if (!ifOne1) {
            for (int i = 0; i < squares.length; i++) {
                for (int j = 0; j < squares.length; j++) {
                    if (squares[i][j].number != blank.number)
                        squares[i][j].getTile().draw(canvas);

                }
            }
        }


    }

    public Square blank(){
        for (int i = 0; i < squares.length; i++) {
            for (int j = 0; j < squares.length; j++) {
                if (squares[i][j].getTile() == null)
                    return squares[i][j];

            }
        }
        return null;

    }
    public void slide(Square from,Square to){
        if (from.getTile()!=null)
        {
            to.setTile(from.getTile());
            from.setTile(null);
            to.getTile().slide(to);

        }
    }

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    @Override
    public boolean onTouchEvent(MotionEvent event) {


        if (ifOne2){
            time = new Time(handler);
            time.start();
            btnPause.setEnabled(true);
            ifPause = false;
        }

        ifOne2 = false;

        if (!ifPause) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                Square mySquare = findSquare(event.getX(), event.getY());
                //Square target = blank();
                if (mySquare != null && checkBlank(event.getX(), event.getY())) {
                    slide(mySquare, blank);

                    moves++;
                    MainActivity.tvMoves.setText("num of moves: " + moves);
                }
                invalidate();

                if (isWin()) {
                    //Toast.makeText(context, "ניצחת אלוף!!", Toast.LENGTH_SHORT).show();

                    time.isRun = false;
                    createSolvedDialog();

                }
            }
        }
        return true;

    }

    private boolean checkBlank(float x, float y) {
        float distant = squares[0][0].h;
        if (blank.didXAndYInSquare(x,y+distant) || blank.didXAndYInSquare(x,y-distant) || blank.didXAndYInSquare(x+distant,y) || blank.didXAndYInSquare(x-distant,y))
            return true;
        return false;
    }

    public boolean isWin() {
        for(int i = 0;i<squares.length;i++) {
            for (int j = 0; j < squares.length; j++) {
                if (squares[i][j].getTile()==null)
                {
                    if (i!=size-1||j!=size-1)
                        return false;
                    continue;
                }
                if(squares[i][j].number != squares[i][j].getTile().number)
                    return false;
            }
        }
        return true;
    }

    private Square findSquare(float x, float y) {
        for(int i = 0;i<squares.length;i++) {
            for (int j = 0; j < squares.length; j++) {
                if(squares[i][j].didXAndYInSquare(x,y))

                    return squares[i][j];
            }
        }
        return null;
    }


    public void createSolvedDialog()
    {
        solvedD=new Dialog(context);
        solvedD.setContentView(R.layout.custom_solved);
        solvedD.setCancelable(false);
        btnSolved = solvedD.findViewById(R.id.btnSolved);
        btnSolved.setOnClickListener((OnClickListener) context);
        solvedD.show();


    }

}
