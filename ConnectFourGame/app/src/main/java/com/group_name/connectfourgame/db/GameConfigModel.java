package com.group_name.connectfourgame.db;

import android.database.Cursor;

public class GameConfigModel {
    public static final String TABLE_NAME = "game_config";
    public static final String GRID_DIMENSION = "default_dimension";

    static String createTable() {
        return "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ("+
            GRID_DIMENSION+" CHARACTER(3) DEFAULT '8x8');";
    }

    static String defaultSetting() {
        return "INSERT INTO "+TABLE_NAME+" ("+GRID_DIMENSION+") SELECT '7x6' WHERE NOT EXISTS (SELECT 1 FROM "+TABLE_NAME+");";
    }

    public static void saveConfig(final String dimensions) {
        DatabaseHelper.db.getWritableDatabase().execSQL("UPDATE "+TABLE_NAME+" SET "+GRID_DIMENSION+" = ?;", new String[]{dimensions});
    }

    public static String getConfig() {
        try(Cursor cursor = DatabaseHelper.db.getReadableDatabase().rawQuery("SELECT * FROM "+TABLE_NAME+";", null)) {
            cursor.moveToNext();
            return cursor.getString(cursor.getColumnIndexOrThrow(GRID_DIMENSION));
        }
    }
}
