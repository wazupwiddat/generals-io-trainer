package com.jdub.generals.trainer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jameswarren on 4/10/17.
 */
public class Game {
    private int turn;
    private GameMap map;
    private GameReplay gameReplay;

    public Game(GameReplay gameReplay) {
        this.gameReplay = gameReplay;
        this.map = new GameMap(gameReplay.getMapWidth().intValue(), gameReplay.getMapHeight().intValue());

        for (int idx = 0; idx < gameReplay.getCities().size(); idx++) {
            Long index = gameReplay.getCities().get(idx);
            Long armyValue = gameReplay.getCityArmies().get(idx);
            this.map.addCity(index.intValue(), armyValue.intValue());
        }
        for (Long index : gameReplay.getMountains()) {
            this.map.addMountain(index.intValue());
        }
        Long playerIndex = 0l;
        for (Long index : gameReplay.getGenerals()) {
            this.map.addGeneral(index.intValue(), playerIndex);
            playerIndex++;
        }
        this.turn = 0;
    }

    private void updateState() {
        if (this.turn % 50 == 0) {
            this.map.updateFarms();
        }
        if (this.turn == 0 || this.turn % 2 == 0) {
            this.map.updateCitiesAndGenerals();
        }
        this.map.updateVisibleFields();
    }

    private void setTurn(int turn) {
        for (int idx = 0; this.turn < turn; this.turn++) {
            updateState();
        }
    }
    public void simulateGame() {
        for (GameMove move : this.gameReplay.getMoves()) {
            setTurn(move.getTurn().intValue());
            // this will save the map prior to move
            this.map.saveMapDataSelection(this.gameReplay.getId(), move);
            this.map.handleAttack(move);
        }
    }
}
