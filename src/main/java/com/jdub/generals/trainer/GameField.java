package com.jdub.generals.trainer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jameswarren on 5/1/17.
 */
public class GameField {
    private Long playerIndex = -1l;
    private Integer mapIndex = -1;
    private Integer armyValue = 0;
    private Boolean city = false;
    private Boolean general = false;
    private Boolean mountain = false;
    private MapPosition mapPosition;
    private Map<Long, Boolean> visible = new HashMap<Long, Boolean>();
    private List<MapPosition> neighbors = new ArrayList<>();
    private List<MapPosition> visibleNeighbors = new ArrayList<>();

    public GameField(MapPosition mapPosition, Integer mapIndex) {
        this.mapIndex = mapIndex;
        this.mapPosition = mapPosition;

        int row = mapPosition.getRow();
        int col = mapPosition.getCol();
        neighbors.add(new MapPosition(col,row + 1));
        neighbors.add(new MapPosition(col,row - 1));
        neighbors.add(new MapPosition(col + 1, row));
        neighbors.add(new MapPosition(col - 1, row));

        visibleNeighbors.add(new MapPosition(col - 1, row - 1));
        visibleNeighbors.add(new MapPosition(col - 1, row + 1));
        visibleNeighbors.add(new MapPosition(col + 1, row + 1));
        visibleNeighbors.add(new MapPosition(col + 1, row - 1));
    }

    public Boolean isMountain() {
        return mountain;
    }

    public void setMountain(Boolean mountain) {
        this.mountain = mountain;
    }

    public Long getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(Long playerIndex) {
        this.playerIndex = playerIndex;
        makeVisible(playerIndex);
    }

    public Integer getArmyValue() {
        return armyValue;
    }

    public void setArmyValue(Integer armyValue) {
        this.armyValue = armyValue;
    }

    public Boolean isCity() {
        return city;
    }

    public void setCity(Boolean city) {
        this.city = city;
    }

    public Boolean isGeneral() {
        return general;
    }

    public void setGeneral(Boolean general) {
        this.general = general;
    }

    public Boolean isEmpty() {
        return playerIndex == -1l;
    }

    public List<MapPosition> getNeighbors() {
        return neighbors;
    }

    public List<MapPosition> getVisibleNeighbors() {
        return visibleNeighbors;
    }

    public Boolean isVisible(Long playerIndex) {
        return visible.containsKey(playerIndex) ? true : false;
    }

    public void makeVisible(Long playerIndex) {
        this.visible.put(playerIndex, true);
    }

    public void clearVisible() {
        this.visible.clear();
    }

    public void incrementArmy() {
        this.armyValue++;
    }

    public Integer getMapIndex() {
        return mapIndex;
    }

}
