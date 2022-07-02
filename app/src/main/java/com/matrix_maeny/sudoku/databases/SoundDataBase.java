package com.matrix_maeny.sudoku.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SoundDataBase extends SQLiteOpenHelper {
    public SoundDataBase(@Nullable Context context) {
        super(context, "Sound.db", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create Table Sound(name TEXT primary key, state TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop Table if exists Sound");
    }


    public boolean insertData(String name, String state) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("name", name);
        cv.put("state", state);

        long result = db.insert("Sound", null, cv);

        return result != -1;


    }

    public boolean updateData(String name,String state){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("state", state);

        long result = db.update("Sound",cv,"name=?",new String[]{name});

        return result != -1;
    }
    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();

        return db.rawQuery("Select * from Sound",null);
    }
}
