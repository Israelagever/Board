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


    public final String COLUMN_MOVE = "move";
    public final String COLUMN_TIME = "time";
    public final String COLUMN_DATE = "date";

    SQLiteDatabase database;

    private static String CREATE_TABLE_RECORD;

    private String orderBy;

    String[] allColumns = { COLUMN_MOVE, COLUMN_TIME, COLUMN_DATE};//כל העמודות


    public RecordHelper(Context context, final String table_records,String orderBy) {
        super(context, DATABASENAME, null, DATABASEVERSION);
        this.table_records = table_records;//הגדרת איזה טבלה
        this.orderBy = orderBy;//הגדרת איזה סידור
        CREATE_TABLE_RECORD = "CREATE TABLE IF NOT EXISTS " +
                this.table_records + "(" + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_MOVE + " INTEGER," + COLUMN_TIME + " VARCHAR,"
                + COLUMN_DATE + " INTEGER " + ");";//פקודה בsql ליצירת טבלה
    }

    public String getOrderBy() {
        return orderBy;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_RECORD);//מריץ את הפקודה ליצירת טבלה
        Log.d("data1", "Table customer created");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {//עדכון הבסיס נתונים
        db.execSQL("DROP TABLE IF EXISTS " + table_records);
        onCreate(db);
    }

    public void open() {//נותן הרשאת כתיבה בבסיס נתונים
        database = this.getWritableDatabase();
        Log.d("data1", "Database connection open");
    }

    public Record createRecord(Record r) {//פעולה שמוסיפה עוד שיא לבסיס נתונים
        ContentValues values = new ContentValues();//אובייקט שמיועד להכניס נתונים לבסיס נתונים
        values.put(this.COLUMN_MOVE, r.getMove());
        values.put(this.COLUMN_TIME, r.getTime());
        values.put(this.COLUMN_DATE, r.getDate());

        long insertId = database.insert(RecordHelper.table_records, null, values);//הפקודה שמוסיפה את השיא החדש
        Log.d("data1", "Record " + insertId + " insert to database");

        return r;
    }
    @SuppressLint("Range")
    public ArrayList<Record> getAllRecord() {//פעולה שמחזירה את כל השיאים

        onCreate(database);
        ArrayList<Record> l = new ArrayList<Record>();
        Cursor cursor=database.query(this.table_records, allColumns, null, null, null, null, orderBy+ " ASC");//פקודה שמוציאה את כל הנתונים מהטבלה

        if(cursor.getCount()>0)
        {
            while(cursor.moveToNext())//כל עוד יש עוד שיא
            {

                int move=cursor.getInt(cursor.getColumnIndex(this.COLUMN_MOVE));
                String time=cursor.getString(cursor.getColumnIndex(this.COLUMN_TIME));
                String date=cursor.getString(cursor.getColumnIndex(this.COLUMN_DATE));
                Record r=new Record(move,time,date);
                l.add(r);
            }

        }
        return l;
    }
}
