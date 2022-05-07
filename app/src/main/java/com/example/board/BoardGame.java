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





public class BoardGame extends View {

    boolean ifOne1 = true;
    int size,color;

    int[] theSort;
    Random r;
    int all;

    Tile[][]tiles;
    Square[][]squares;

    Context context;

    static Square blank;
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



        this.setBackgroundColor(Color.parseColor("#393939"));

    }



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
            int x = 15;
            int y = 15;
            int e = canvas.getWidth(),f = canvas.getHeight();
            int h = ((canvas.getWidth()-15)/size)-15;
            int w = ((canvas.getWidth()-15)/size)-15;


            moves=0;



            for (int i = 0; i < squares.length; i++) {
                for (int j = 0; j < squares.length; j++) {


                    /*
                    if(i==size-1&&j==size-1){
                        squares[i][j] = new Square(this, x, y, w, h, colorOfTile,tiles[i][j],count);
                        break;
                    }

                     */

                    if (theSort[count - 1] != 0) {

                        tiles[i][j] = new Tile(this, x, y, w, h, theSort[count - 1], color);
                        tiles[i][j].draw(canvas);

                    }
                    squares[i][j] = new Square(this, x, y, w, h,tiles[i][j],count);
                    x = x + w + 15;
                    count++;

                }
                y = y + h + 15;
                x = 15;
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



    public boolean checkBlank(float x, float y) {
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

    public Square findSquare(float x, float y) {
        for(int i = 0;i<squares.length;i++) {
            for (int j = 0; j < squares.length; j++) {
                if(squares[i][j].didXAndYInSquare(x,y))

                    return squares[i][j];
            }
        }
        return null;
    }




}
