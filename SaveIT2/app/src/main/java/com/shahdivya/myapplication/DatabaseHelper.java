package com.shahdivya.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "NoteDatabase";

    public DatabaseHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sQuery = "CREATE TABLE Note (id INTEGER PRIMARY KEY AUTOINCREMENT , title TEXT,description TEXT)";
        db.execSQL(sQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sQuery = "DROP TABLE IF EXISTS Note";
        db.execSQL(sQuery);
        onCreate(db);
    }
}
