package com.example.board;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class RecordHelper extends SQLiteOpenHelper {

    public static final String DATABASENAME = "records.db";
    public static final String TABLE_RECORDS = "tblrecords";
    public static final int DATABASEVERSION = 1;

    public static final String COLUMN_ID = "recordId";
    public static final String COLUMN_MOVE = "move";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_DATE = "date";

    SQLiteDatabase database;

    private static final String CREATE_TABLE_PPRODUCT = "CREATE TABLE IF NOT EXISTS " +
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
        db.execSQL(CREATE_TABLE_PPRODUCT);
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

    public Record createProduct2(Record r) {
        ContentValues values = new ContentValues();
        values.put(RecordHelper.COLUMN_MOVE, r.getMove());
        values.put(RecordHelper.COLUMN_TIME, r.getTime());
        values.put(RecordHelper.COLUMN_DATE, r.getDate());

        long insertId = database.insert(RecordHelper.TABLE_RECORDS, null, values);
        Log.d("data1", "Product " + insertId + "insert to database");
        r.setRecordId(insertId);
        return r;
    }
}
