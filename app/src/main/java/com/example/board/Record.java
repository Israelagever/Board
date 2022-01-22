package com.example.board;

public class Record {
    private int move;
    private String time;
    private String date;
    private long recordId;

    public Record(long recordId,int move, String time, String date) {
        this.move = move;
        this.time = time;
        this.date = date;
        this.recordId = recordId;

    }
    public Record(int move, String time, String date) {
        this.move = move;
        this.time = time;
        this.date = date;
        this.recordId = 0;

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

    public void setTime(String description) {
        this.time = description;
    }

    public String getDate() {
        return date;
    }

    public void setPrice(String date) {
        this.date = date;
    }
    public long getRecordId() {
        return recordId;
    }
    public void setRecordId(long recordId) {
        this.recordId = recordId;
    }
    @Override
    public String toString() {
        return "Product{" +
                "name='" + move + '\'' +
                ", description='" + time + '\'' +
                ", price=" + date +
                ", productId=" + recordId +
                '}';
    }
}