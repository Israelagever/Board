package com.example.board;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;



public class Square  {

    BoardGame boardGame;
    Tile tile;
    float x,y,w,h;

    Paint p;
    int number;


    public Square(BoardGame boardGame,float x,float y,float w,float h,Tile tile,int number)
    {


        this.number = number;//המספר של הריבוע
        this.x = x;//מיקום לפי x
        this.y = y;//מיקום לפי y

        this.boardGame = boardGame;
        p = new Paint();

        this.w = w;//גודל של הריבוע
        this.h = h;
        this.tile = tile;
    }
    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }
    public Tile getTile() {
        return tile;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public boolean didXAndYInSquare(float xo, float yo)//פעולה שמקבלת x וy ובודקת אם הם נמצאים על הריבוע
    {
        if(xo > x && xo < x + w && yo > y && yo < y + h )
            return true;
        return false;
    }


}
