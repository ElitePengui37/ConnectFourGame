package com.group_name.connectfourgame.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.group_name.connectfourgame.User;

import java.util.ArrayList;
import java.util.List;

public class UserModel {
    public static final String TABLE_NAME = "users";
    public static final String FIELD_USERNAME = "username";
    public static final String FIELD_AVATAR = "avatar";
    public static final String FIELD_PREFERRED_DISC_COLOR1 = "disc_color1";
    public static final String FIELD_PREFERRED_DISC_COLOR2 = "disc_color2";
    public static final String FIELD_PREFERRED_DISC_COLOR3 = "disc_color3";
    public static final String FIELD_PVP_WIN = "pvp_win_count";
    public static final String FIELD_PVP_LOSS = "pvp_loss_count";
    public static final String FIELD_PVP_DRAW = "pvp_draw_count";
    public static final String FIELD_AI_WIN = "ai_win_count";
    public static final String FIELD_AI_LOSS = "ai_loss_count";
    public static final String FIELD_AI_DRAW = "ai_draw_count";

    static String createTable() {
        return "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ("+
            DatabaseHelper.ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
            FIELD_USERNAME+" VARCHAR(64) NOT NULL,"+
            FIELD_AVATAR+" TINYINT NOT NULL,"+
            FIELD_PREFERRED_DISC_COLOR1+" TINYINT NOT NULL,"+
            FIELD_PREFERRED_DISC_COLOR2+" TINYINT NOT NULL,"+
            FIELD_PREFERRED_DISC_COLOR3+" TINYINT NOT NULL,"+
            FIELD_PVP_WIN+" INT DEFAULT 0,"+
            FIELD_PVP_LOSS+" INT DEFAULT 0,"+
            FIELD_PVP_DRAW+" INT DEFAULT 0,"+
            FIELD_AI_WIN+" INT DEFAULT 0,"+
            FIELD_AI_LOSS+" INT DEFAULT 0,"+
            FIELD_AI_DRAW+" INT DEFAULT 0"+
        ");";
    }

    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>(16);
        try (Cursor cursor = DatabaseHelper.db.getReadableDatabase().rawQuery("SELECT * FROM "+TABLE_NAME+";", null)) {
            while (cursor.moveToNext()) {
                users.add(new User(
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FIELD_USERNAME)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(FIELD_AVATAR)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(FIELD_PREFERRED_DISC_COLOR1)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(FIELD_PREFERRED_DISC_COLOR2)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(FIELD_PREFERRED_DISC_COLOR3)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(FIELD_PVP_WIN)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(FIELD_PVP_LOSS)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(FIELD_PVP_DRAW)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(FIELD_AI_WIN)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(FIELD_AI_LOSS)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(FIELD_AI_DRAW))
                ));
            }
        }
        return users;
    }

    public static List<User> getUserRankings() {
        List<User> users = new ArrayList<>(16);
        try (Cursor cursor = DatabaseHelper.db.getReadableDatabase().rawQuery(
                "SELECT * FROM "+TABLE_NAME+" ORDER BY ("+
                        FIELD_PVP_WIN+" + ("+ FIELD_PVP_DRAW +" * 0.5) - "+FIELD_PVP_LOSS+
                    ") DESC;", null)) {
            while (cursor.moveToNext()) {
                users.add(new User(
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(FIELD_USERNAME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(FIELD_AVATAR)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(FIELD_PREFERRED_DISC_COLOR1)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(FIELD_PREFERRED_DISC_COLOR2)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(FIELD_PREFERRED_DISC_COLOR3)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(FIELD_PVP_WIN)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(FIELD_PVP_LOSS)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(FIELD_PVP_DRAW)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(FIELD_AI_WIN)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(FIELD_AI_LOSS)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(FIELD_AI_DRAW))
                ));
            }
        }
        return users;
    }

    public static User getUser(int id) {
        try (Cursor cursor = DatabaseHelper.db.getReadableDatabase().rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+DatabaseHelper.ID+" = "+id+";", null)) {
            if(cursor.moveToNext())
                return new User(
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FIELD_USERNAME)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(FIELD_AVATAR)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(FIELD_PREFERRED_DISC_COLOR1)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(FIELD_PREFERRED_DISC_COLOR2)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(FIELD_PREFERRED_DISC_COLOR3)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(FIELD_PVP_WIN)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(FIELD_PVP_LOSS)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(FIELD_PVP_DRAW)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(FIELD_AI_WIN)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(FIELD_AI_LOSS)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(FIELD_AI_DRAW))
                );
            return null;
        }
    }

    public static User storeUser(String username, int avatar , int disc1 , int disc2 , int disc3) {
        ContentValues values = new ContentValues();
        values.put(FIELD_USERNAME, username);
        values.put(FIELD_AVATAR, avatar);
        values.put(FIELD_PREFERRED_DISC_COLOR1, disc1);
        values.put(FIELD_PREFERRED_DISC_COLOR2, disc2);
        values.put(FIELD_PREFERRED_DISC_COLOR3, disc3);
        long result = DatabaseHelper.db.getWritableDatabase().insert(TABLE_NAME, null, values);
        try(Cursor cursor = DatabaseHelper.db.getReadableDatabase().rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE rowid = ?", new String[]{Long.toString(result)})) {
            cursor.moveToNext();
            return new User(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.ID)),
                    username, avatar, disc1, disc2, disc3);
        }
    }

    public static User updateUser(User user) {
        ContentValues values = new ContentValues();
        values.put(FIELD_USERNAME, user.username);
        values.put(FIELD_AVATAR, user.avatar);
        values.put(FIELD_PREFERRED_DISC_COLOR1, user.disc1);
        values.put(FIELD_PREFERRED_DISC_COLOR2, user.disc2);
        values.put(FIELD_PREFERRED_DISC_COLOR3, user.disc3);
        values.put(FIELD_PVP_WIN, user.pvp_win_count);
        values.put(FIELD_PVP_LOSS, user.pvp_loss_count);
        values.put(FIELD_PVP_DRAW, user.pvp_draw_count);
        values.put(FIELD_AI_WIN, user.ai_win_count);
        values.put(FIELD_AI_LOSS, user.ai_loss_count);
        values.put(FIELD_AI_DRAW, user.ai_draw_count);
        int result = DatabaseHelper.db.getWritableDatabase().update(TABLE_NAME, values, DatabaseHelper.ID+" = ?", new String[]{String.valueOf(user.id)});
        return user;
    }

    public static void incrementWinRecord(final User user) {
        DatabaseHelper.db.getWritableDatabase().execSQL("UPDATE "+TABLE_NAME+" SET "+
                FIELD_PVP_WIN+" = "+FIELD_PVP_WIN+" + 1 WHERE id = ?;", new String[] {Integer.toString(user.id)});
    }

    public static void incrementLossRecord(final User user) {
        DatabaseHelper.db.getWritableDatabase().execSQL("UPDATE "+TABLE_NAME+" SET "+
                FIELD_PVP_LOSS+" = "+FIELD_PVP_LOSS+" + 1 WHERE id = ?;", new String[] {Integer.toString(user.id)});
    }

    public static void incrementDrawRecord(final User user) {
        DatabaseHelper.db.getWritableDatabase().execSQL("UPDATE "+TABLE_NAME+" SET "+
                FIELD_PVP_DRAW+" = "+FIELD_PVP_DRAW+" + 1 WHERE id = ?;", new String[] {Integer.toString(user.id)});
    }

    public static void incrementAIWinRecord(final User user) {
        DatabaseHelper.db.getWritableDatabase().execSQL("UPDATE "+TABLE_NAME+" SET "+
                FIELD_AI_WIN+" = "+FIELD_AI_WIN+" + 1 WHERE id = ?;", new String[] {Integer.toString(user.id)});
    }

    public static void incrementAILossRecord(final User user) {
        DatabaseHelper.db.getWritableDatabase().execSQL("UPDATE "+TABLE_NAME+" SET "+
                FIELD_AI_LOSS+" = "+FIELD_AI_LOSS+" + 1 WHERE id = ?;", new String[] {Integer.toString(user.id)});
    }

    public static void incrementAIDrawRecord(final User user) {
        DatabaseHelper.db.getWritableDatabase().execSQL("UPDATE "+TABLE_NAME+" SET "+
                FIELD_AI_DRAW+" = "+FIELD_AI_DRAW+" + 1 WHERE id = ?;", new String[] {Integer.toString(user.id)});
    }
}
