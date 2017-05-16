package com.jdub.generals.trainer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jameswarren on 4/10/17.
 */
public class GameReplay {
    private boolean invalidReplay = false;

    private List<Long> generals = new ArrayList<>();
    private List<String> usernames = new ArrayList<>();
    private List<Long> cities = new ArrayList<>();
    private List<Long> cityArmies = new ArrayList<>();
    private List<Long> mountains = new ArrayList<>();
    private List<Long> stars = new ArrayList<>();
    private List<GameMove> moves = new ArrayList<>();
    private Long version;
    private String id;

    private Long mapHeight;
    private Long mapWidth;

    public GameReplay(JSONObject parsedFile) {
        try {
            if (parsedFile.get("teams") != null) {
                invalidReplay = true;
                System.out.println(this);
                return;
            }
            this.mapHeight = (Long)parsedFile.get("mapHeight");
            this.mapWidth = (Long)parsedFile.get("mapWidth");
            if (this.mapHeight * this.mapWidth > 900) { // train data will be no bigger than 30*30
                invalidReplay = true;
            }
            JSONArray gens = (JSONArray)parsedFile.get("generals");
            this.generals.addAll(Utils.toLongArray(gens));
            this.cities.addAll(Utils.toLongArray((JSONArray)parsedFile.get("cities")));
            this.cityArmies.addAll(Utils.toLongArray((JSONArray)parsedFile.get("cityArmies")));
            this.mountains.addAll(Utils.toLongArray((JSONArray)parsedFile.get("mountains")));

            this.version = (Long)parsedFile.get("version") ;
            this.id = (String)parsedFile.get("id");
            this.usernames.addAll(Utils.toStringArray((JSONArray)parsedFile.get("usernames")));
            this.stars.addAll(Utils.toLongArray((JSONArray)parsedFile.get("stars")));

            this.moves.addAll(Utils.toGameMoveArray((JSONArray)parsedFile.get("moves")));
        } catch (Exception e) {
            invalidReplay = true;
        }

        System.out.println(this);
    }

    public boolean isInvalidReplay() {
        return invalidReplay;
    }

    public List<Long> getCities() {
        return cities;
    }

    public List<Long> getCityArmies() {
        return cityArmies;
    }

    public List<Long> getMountains() {
        return mountains;
    }

    public List<GameMove> getMoves() {
        return moves;
    }

    public String getId() {
        return id;
    }

    public Long getMapHeight() {
        return mapHeight;
    }

    public Long getMapWidth() {
        return mapWidth;
    }

    public List<Long> getGenerals() {
        return generals;
    }

    @Override
    public String toString() {
        if (invalidReplay)
            return String.format("GameReplay (INVALID)");

        return String.format("GameReplay (id = %s, " +
                "width = %s, " +
                "height = %s, " +
                "version = %s)",
                this.id, this.mapWidth, this.mapHeight, this.version);
    }
}
