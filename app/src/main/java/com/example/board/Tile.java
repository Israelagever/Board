package com.example.board;


import android.graphics.Canvas;
import android.graphics.Paint;


public class Tile {


    BoardGame boardGame;
    float x, y, w, h;
    int number, color;
    Paint p, paintTitle;

    public Tile(BoardGame boardGame, float x, float y, float w, float h, int num, int color) {
        this.boardGame = boardGame;

        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

        this.number = num;
        this.color = color;

        init();

    }

    private void init() {

        //אתחול הpaint של הרקע של האריח
        p = new Paint();
        p.setColor(color);

        //אתחול הpaint של המספרים
        paintTitle = new Paint();
        paintTitle.setARGB(255, 255, 255, 255);
        paintTitle.setTextAlign(Paint.Align.CENTER);
        paintTitle.setTextSize(70);


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

    public void draw(Canvas canvas) {//פעולה שמציירת את האריח
        canvas.drawRect(x,y,x+w,y+h,p);//מצייר את הרקע של האריח

        //drawRoundRect(x, y, x + w, y + h, p2, canvas);

        //מצייר את המספר שעל האריח
        canvas.drawText(String.valueOf(number), x + (w / 2), (y + (h / 2))-(paintTitle.descent()+paintTitle.ascent())/2, paintTitle);
    }



    public void slide(Square to) {//פעולה שמקבלת ריבוע ומעבירה את האריח הנוכחי לריבוע הזה

        this.setX(to.getX());
        this.setY(to.getY());
    }

    /*
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void drawRoundRect(float left, float top, float right, float bottom, Paint paint, Canvas canvas) {


        Path path = new Path();
        float rx = 25;
        float ry = 25;
        float width = right - left;
        float height = bottom - top;
        if (rx > width / 2) rx = width / 2;
        if (ry > height / 2) ry = height / 2;
        float widthMinusCorners = (width - (2 * rx));
        float heightMinusCorners = (height - (2 * ry));

        path.moveTo(right, top + ry);
        path.arcTo(right - 2 * rx, top, right, top + 2 * ry, 0, -90, false); //top-right-corner
        path.rLineTo(-widthMinusCorners, 0);
        path.arcTo(left, top, left + 2 * rx, top + 2 * ry, 270, -90, false);//top-left corner.
        path.rLineTo(0, heightMinusCorners);
        if (false) {
            path.rLineTo(0, ry);
            path.rLineTo(width, 0);
            path.rLineTo(0, -ry);
        } else {
            path.arcTo(left, bottom - 2 * ry, left + 2 * rx, bottom, 180, -90, false); //bottom-left corner
            path.rLineTo(widthMinusCorners, 0);
            path.arcTo(right - 2 * rx, bottom - 2 * ry, right, bottom, 90, -90, false); //bottom-right corner

        }
        path.rLineTo(0, -heightMinusCorners);

        path.close();//Given close, last lineto can be removed.


        canvas.drawPath(path, paint);
    }

     */
}

