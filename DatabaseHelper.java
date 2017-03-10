package com.example.paul.paul_heijen_pset41;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Paul on 08/03/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {



    // Table Name
    private static final String DB_NAME = "TODOLISTDATABASE";
    private static final int DB_VERSION = 1;

    // Table columns
    public static final String _ID = "_id";
    public static final String TABLE_NAME = "TODOLIST";
    public static final String SUBJECT = "subject";
    public static final String DESC = "";



    // Creating table query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + SUBJECT + " TEXT NOT NULL, " + ");";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}

