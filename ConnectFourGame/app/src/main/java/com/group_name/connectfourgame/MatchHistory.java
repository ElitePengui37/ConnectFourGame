package com.group_name.connectfourgame;

import androidx.annotation.NonNull;

public class MatchHistory {
    public enum ResultType {
        PLAYER1_WIN(0),
        PLAYER2_WIN(1),
        DRAW(2);

        private final int id;
        ResultType(final int p_id) {
            id = p_id;
        }
        public int getId() {
            return id;
        }
        public static ResultType parse(int id) {
            switch(id) {
                case 0: return ResultType.PLAYER1_WIN;
                case 1: return ResultType.PLAYER2_WIN;
                case 2: return ResultType.DRAW;
                default: throw new Error("Invalid id for ResultType");
            }
        }

        @Override @NonNull
        public String toString() {
            switch(id) {
                case 0: return "Win";
                case 1: return "Lose";
                case 2: return "Draw";
                default: throw new Error("Invalid id for ResultType");
            }
        }

        public String toP2String() {
            switch(id) {
                case 0: return "Lose";
                case 1: return "Win";
                case 2: return "Draw";
                default: throw new Error("Invalid id for ResultType");
            }
        }
    };

    public final ResultType resultType;
    public final Integer player1_id;
    public final Integer player2_id;

    public MatchHistory(final Integer p_player1_id, final ResultType p_resultType, final Integer p_player2_id) {
        player1_id = p_player1_id;
        resultType = p_resultType;
        player2_id = p_player2_id;
    }

    public MatchHistory(final User player1, final ResultType p_resultType, final User player2) {
        player1_id = player1 == null? null : player1.id;
        resultType = p_resultType;
        player2_id = player2 == null? null : player2.id;
    }
}
