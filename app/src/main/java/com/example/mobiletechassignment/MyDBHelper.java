package com.example.mobiletechassignment;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class MyDBHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "events";
    public static final String
            COLUMN_ID = "id";
    private static final String COLUMN_HEADER = "header";
    public MyDBHelper(Context context, String name,
                      SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " +
                        TABLE_NAME +
                        "(" +
                        COLUMN_ID + " integer primary key, " +
                        COLUMN_HEADER + " text" +
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public long insertEvent(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(
                COLUMN_HEADER,name);
        long id = db.insert(
                TABLE_NAME, null, contentValues);
        return id;
    }

    public String selectAllEvent(){
        String eventName = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);
        if (cursor.moveToFirst()){
            eventName = cursor.getString(cursor.getColumnIndex(COLUMN_HEADER));
        }
        cursor.close();
//        cursor.moveToFirst();
//        while (cursor.isAfterLast() == false) {
//            String name = cursor.getString(cursor.getColumnIndex(COLUMN_HEADER));
//            eventName += name + ",";
//            cursor.moveToNext();
//        }
        return eventName;
    }


}
