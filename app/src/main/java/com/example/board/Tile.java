package com.example.board;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Tile {
    float lastx,lasty;
    float x,y,w,h;

    int number, color;
    BoardGame boardGame;
    Paint p,paintTitle;
    public Tile(BoardGame boardGame,float x,float y,float w,float h,int num,int color)
    {
        this.boardGame = boardGame;
        this.lastx=x;
        this.lasty=y;
        this.x=x;this.y=y;this.w=w;this.h=h;

        this.number = num;
        this.color = color;
        p = new Paint();
        p.setColor(color);

        paintTitle = new Paint();

        paintTitle.setARGB(255, 255, 255, 255);
        paintTitle.setTextAlign(Paint.Align.CENTER);
        paintTitle.setTextSize(50);
        //textp.setColor(Color.GREEN);

    }



    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
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

        canvas.drawText(String.valueOf(number),x+(w/2),y+(h/2),paintTitle);
    }

    public boolean didUserTouchMe(float xu,float yu)
    {
        if(xu > x && xu < x + w && yu > y && yu < yu + h )
            return true;
        return false;
    }
    public void slide(Square to)
    {

        this.setX(to.getX());
        this.setY(to.getY());
    }

}
