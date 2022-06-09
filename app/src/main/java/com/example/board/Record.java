package com.example.board;

public class Record {
    private int move;//מהלכים
    private String time;//זמן
    private String date;//תאריך



    public Record(int move, String time, String date) {
        this.move = move;
        this.time = time;
        this.date = date;


    }
    public int getMove() {
        return move;
    }
    public void setMove(int move) {
        this.move = move;
    }

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Record{" +
                "move='" + move + '\'' +
                ", time='" + time + '\'' +
                ", date=" + date +
                '}';
    }
}