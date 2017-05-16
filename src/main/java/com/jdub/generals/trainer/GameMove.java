package com.jdub.generals.trainer;

import org.json.simple.JSONObject;

/**
 * Created by jameswarren on 4/10/17.
 */
public class GameMove {
    private Long playerIndex;
    private Long start, end;
    private boolean is50;
    private Long turn;

    public Long getPlayerIndex() {
        return playerIndex;
    }

    public Long getStart() {
        return start;
    }

    public Long getEnd() {
        return end;
    }

    public boolean isIs50() {
        return is50;
    }

    public Long getTurn() {
        return turn;
    }

    public GameMove(JSONObject move) {
        this.playerIndex = (Long)move.get("index");
        this.start = (Long)move.get("start");
        this.end = (Long)move.get("end");
        this.is50 = (Long)move.get("is50") > 0 ? true : false;
        this.turn = (Long)move.get("turn");
    }
}
