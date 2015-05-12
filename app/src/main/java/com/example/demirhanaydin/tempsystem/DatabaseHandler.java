package com.example.demirhanaydin.tempsystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by demirhanaydin on 12/05/15.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    // Database version
    private static final int DATABASE_VERSION = 1;
    // database name
    private static final String DATABASE_NAME = "entryLists_development";
    // table name
    private static final String TABLE_ENTRY = "entryLists";
    // table columns
    public static final String COL_KEY_ID = "_id";
    public static final String COL_TEMP = "temp";
    public static final String COL_HUMIDITY = "humidity";
    public static final String COL_DESC = "description";
    public static final String COL_LAT = "lat";
    public static final String COL_LNG = "lng";
    public static final String COL_CREATED_AT = "created_at";
    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TODOLIST_TABLE = "CREATE TABLE " + TABLE_ENTRY + "("
                + COL_KEY_ID + " INTEGER PRIMARY KEY,"
                + COL_TEMP + " DOUBLE,"
                + COL_HUMIDITY + " DOUBLE,"
                + COL_DESC + " TEXT,"
                + COL_LAT + " DOUBLE,"
                + COL_LNG + " DOUBLE,"
                + COL_CREATED_AT + " INTEGER"
                + ")";
        db.execSQL(CREATE_TODOLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        //-- drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTRY);
        //-- create tables again
        onCreate(db);
    }

    public Cursor getEntries(){
        SQLiteDatabase db = this.getWritableDatabase();
        // query - select all
        String selectQuery = "SELECT * FROM " + TABLE_ENTRY;
        // run the query
        Cursor cursor = db.rawQuery(selectQuery, null);
        // return cursor object
        return cursor;
    }

    public long addEntry(Entry entry){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // put values to content
        values.put(COL_TEMP, entry.getTemp());
        values.put(COL_HUMIDITY, entry.getHumidity());
        values.put(COL_DESC, entry.getDescription());
        values.put(COL_LAT, entry.getLat());
        values.put(COL_LNG, entry.getLng());
        values.put(COL_CREATED_AT, entry.getCreated_at());
        // insert new row
        long result = db.insert(
                TABLE_ENTRY,
                null,
                values);
        db.close();
        return result;
    }

    public int deleteEntry(Entry entry){
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(
                TABLE_ENTRY,
                COL_KEY_ID + " = ?",
                new String[] { String.valueOf(entry.getId()) }
        );
        db.close();
        return result;
    }
}
