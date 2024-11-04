package com.group_name.connectfourgame.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    static DatabaseHelper db = null;

    static final String ID = "id";
    private static final String DATABASE_NAME = "connectfour.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static void initialize(Context context) {
        if(db != null)
            db.close();
        db = new DatabaseHelper(context);
    }

    public static void closeDb() {
        if(db != null)
            db.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UserModel.createTable());
        db.execSQL(MatchHistoryModel.createTable());
        db.execSQL(GameConfigModel.createTable());
        db.execSQL(GameConfigModel.defaultSetting());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+UserModel.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+MatchHistoryModel.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+GameConfigModel.TABLE_NAME);
        onCreate(db);
    }
}
