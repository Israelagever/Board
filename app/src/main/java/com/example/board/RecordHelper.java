package com.example.board;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class RecordHelper extends SQLiteOpenHelper {

    public static final String DATABASENAME = "records.db";
    public static final String TABLE_RECORDS = "tblrecords";
    public static final int DATABASEVERSION = 1;

    public static final String COLUMN_ID = "recordId";
    public static final String COLUMN_MOVE = "move";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_DATE = "date";

    SQLiteDatabase database;

    private static final String CREATE_TABLE_RECORD = "CREATE TABLE IF NOT EXISTS " +
            TABLE_RECORDS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_MOVE + " INTEGER," + COLUMN_TIME + " VARCHAR,"
            + COLUMN_DATE + " INTEGER " + ");";


    String[] allColumns = {RecordHelper.COLUMN_ID, RecordHelper.COLUMN_MOVE, RecordHelper.COLUMN_TIME,
            RecordHelper.COLUMN_DATE};


    public RecordHelper(Context context) {
        super(context, DATABASENAME, null, DATABASEVERSION);
        // TODO Auto-generated constructor stub
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_RECORD);
        Log.d("data1", "Table customer created");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECORDS);
        onCreate(db);
    }

    public void open() {
        database = this.getWritableDatabase();
        Log.d("data1", "Database connection open");
    }

    public Record createRecord(Record r) {
        ContentValues values = new ContentValues();
        values.put(RecordHelper.COLUMN_MOVE, r.getMove());
        values.put(RecordHelper.COLUMN_TIME, r.getTime());
        values.put(RecordHelper.COLUMN_DATE, r.getDate());

        long insertId = database.insert(RecordHelper.TABLE_RECORDS, null, values);
        Log.d("data1", "Record " + insertId + " insert to database");
        r.setRecordId(insertId);
        return r;
    }
    @SuppressLint("Range")
    public ArrayList<Record> getAllRecord() {

        ArrayList<Record> l = new ArrayList<Record>();
        Cursor cursor=database.query(RecordHelper.TABLE_RECORDS, allColumns, null, null, null, null, COLUMN_MOVE+ " ASC");

        if(cursor.getCount()>0)
        {
            while(cursor.moveToNext())
            {
                 long id=cursor.getLong(cursor.getColumnIndex(RecordHelper.COLUMN_ID));
                int move=cursor.getInt(cursor.getColumnIndex(RecordHelper.COLUMN_MOVE));
                String time=cursor.getString(cursor.getColumnIndex(RecordHelper.COLUMN_TIME));
                String date=cursor.getString(cursor.getColumnIndex(RecordHelper.COLUMN_DATE));
                Record r=new Record(id,move,time,date);
                l.add(r);
            }
        }
        return l;
    }
}
