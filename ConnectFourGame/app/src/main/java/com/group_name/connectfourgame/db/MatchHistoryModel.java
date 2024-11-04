package com.group_name.connectfourgame.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.group_name.connectfourgame.MatchHistory;
import com.group_name.connectfourgame.MatchHistory.ResultType;
import com.group_name.connectfourgame.User;

import java.util.ArrayList;
import java.util.List;

public class MatchHistoryModel {
    public static final String TABLE_NAME = "match_history";
    public static final String RESULT_TYPE = "result_type";
    public static final String PLAYER1_PLAYER = "player1_id";
    public static final String PLAYER2_PLAYER = "player2_id";

    static String createTable() {
        return "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ("+
            PLAYER1_PLAYER+" INT,"+
            RESULT_TYPE+ " TINYINT,"+
            PLAYER2_PLAYER+" INT,"+
            "FOREIGN KEY ("+PLAYER1_PLAYER+") REFERENCES users("+DatabaseHelper.ID+"),"+
            "FOREIGN KEY ("+PLAYER2_PLAYER+") REFERENCES users("+DatabaseHelper.ID+")"+
        ");";
    }

    private static void updateRecordWithWin(final User winner, final User loser) {
        if(winner != null)
            UserModel.incrementWinRecord(winner);
        if(loser != null)
            UserModel.incrementLossRecord(loser);
    }

    public static void storeResults(final User player1, final ResultType resultType, final User player2) {
        ContentValues results = new ContentValues();
        results.put(PLAYER1_PLAYER, player1 == null? null : player1.id);
        results.put(RESULT_TYPE, resultType.getId());
        results.put(PLAYER2_PLAYER, player2 == null? null : player2.id);
        long result = DatabaseHelper.db.getWritableDatabase().insert(TABLE_NAME, null, results);

        switch (resultType) {
            case PLAYER1_WIN:
                updateRecordWithWin(player1, player2);
                break;
            case PLAYER2_WIN:
                updateRecordWithWin(player2, player1);
                break;
            case DRAW:
                if(player1 != null)
                    UserModel.incrementDrawRecord(player1);
                if(player2 != null)
                    UserModel.incrementDrawRecord(player2);
                break;
        }
    }

    public static void storeAIResults(final User player, final MatchHistory.ResultType resultType) {
        if(player == null)
            return;
        switch (resultType) {
            case PLAYER1_WIN:
                UserModel.incrementAIWinRecord(player);
                break;
            case PLAYER2_WIN:
                UserModel.incrementAILossRecord(player);
                break;
            case DRAW:
                UserModel.incrementAIDrawRecord(player);
                break;
        }
    }

    public static List<MatchHistory> retrieveResults(final User user) {
        List<MatchHistory> matchResults = new ArrayList<>(16);
        try (Cursor cursor = DatabaseHelper.db.getReadableDatabase().rawQuery(
            "SELECT * FROM "+TABLE_NAME+" WHERE "+PLAYER1_PLAYER+" = ? OR "+PLAYER2_PLAYER+" = ? LIMIT 16;",
            new String[]{ Integer.toString(user.id), Integer.toString(user.id) })
        ) {
            final int player1ColIndex = cursor.getColumnIndexOrThrow(PLAYER1_PLAYER);
            final int player2ColIndex = cursor.getColumnIndexOrThrow(PLAYER2_PLAYER);
            while(cursor.moveToNext())
                matchResults.add(new MatchHistory(
                    cursor.isNull(player1ColIndex)? null : cursor.getInt(player1ColIndex),
                    ResultType.parse(cursor.getInt(cursor.getColumnIndexOrThrow(RESULT_TYPE))),
                    cursor.isNull(player2ColIndex)? null : cursor.getInt(player2ColIndex)
                ));
        }
        return matchResults;
    }
}
