package com.example.board;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;



public class Square  {

    BoardGame boardGame;
    Tile tile;
    float x,y,w,h;//top left

    Paint p;
    int number;


    public Square(BoardGame boardGame,float x,float y,float w,float h,Tile tile,int number)
    {


        this.number = number;
        this.x = x;
        this.y = y;

        this.boardGame = boardGame;
        p = new Paint();

        this.w = w;
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

    public void draw(Canvas canvas)
    {
        canvas.drawRect(x,y,x+w,y+h,p);

    }

    public boolean didXAndYInSquare(float xo, float yo)
    {
        if(xo > x && xo < x + w && yo > y && yo < y + h )
            return true;
        return false;
    }


}
