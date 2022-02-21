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
    public static  String table_records;
    public static final int DATABASEVERSION = 1;

    public static final String COLUMN_ID = "recordId";
    public static final String COLUMN_MOVE = "move";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_DATE = "date";

    SQLiteDatabase database;

    private static String CREATE_TABLE_RECORD;


    String[] allColumns = {RecordHelper.COLUMN_ID, RecordHelper.COLUMN_MOVE, RecordHelper.COLUMN_TIME,
            RecordHelper.COLUMN_DATE};


    public RecordHelper(Context context, final String table_records) {
        super(context, DATABASENAME, null, DATABASEVERSION);
        this.table_records = table_records;
        CREATE_TABLE_RECORD = "CREATE TABLE IF NOT EXISTS " +
                table_records + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_MOVE + " INTEGER," + COLUMN_TIME + " VARCHAR,"
                + COLUMN_DATE + " INTEGER " + ");";
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_RECORD);
        Log.d("data1", "Table customer created");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + table_records);
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

        long insertId = database.insert(RecordHelper.table_records, null, values);
        Log.d("data1", "Record " + insertId + " insert to database");
        r.setRecordId(insertId);
        return r;
    }
    @SuppressLint("Range")
    public ArrayList<Record> getAllRecord() {

        onCreate(database);
        ArrayList<Record> l = new ArrayList<Record>();
        Cursor cursor=database.query(this.table_records, allColumns, null, null, null, null, COLUMN_MOVE+ " ASC");

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
