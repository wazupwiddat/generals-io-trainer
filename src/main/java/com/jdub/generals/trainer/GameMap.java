package com.jdub.generals.trainer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Created by jameswarren on 4/11/17.
 */
public class GameMap {

    public static final String TRAININGDATA_SELECTION_FILES = "/trainingdata/%s_sel.txt";
    private Integer width, height;
    private Map<MapPosition, GameField> map = new HashMap<>();

    public GameMap(Integer width, Integer height) {
        this.width = width;
        this.height = height;

        for (int idx = 0; idx < getSize(); idx++) {
            MapPosition mapPosition = createMapPositionFromIndex(idx);
            map.put(mapPosition, new GameField(mapPosition, idx));
        }
    }

    public Integer getSize() {
        return this.width * this.height;
    }

    private MapPosition createMapPositionFromIndex(Integer index) {
        return new MapPosition((index % width), Math.floorDiv(index, width));
    }

    public void updateFarms() {
        for (GameField gameField : map.values()) {
            if (gameField.isEmpty())
                continue;

            if (gameField.isGeneral() || gameField.isCity())
                continue;

            if (gameField.getPlayerIndex() >= 0) {
                gameField.incrementArmy();
            }
        }
    }

    public void updateCitiesAndGenerals() {
        for (GameField gameField : map.values()) {
            if (gameField.isEmpty())
                continue;

            if (gameField.isCity() || gameField.isGeneral()) {
                gameField.incrementArmy();
            }
        }
    }

    public void addCity(Integer index, Integer armyValue) {
        MapPosition position = createMapPositionFromIndex(index);

        GameField cityField = this.map.get(position);
        cityField.setCity(true);
        cityField.setArmyValue(armyValue);
    }

    public void addMountain(Integer index) {
        MapPosition position = createMapPositionFromIndex(index);
        this.map.get(position).setMountain(true);
    }

    public void addGeneral(Integer index, Long playerIndex) {
        MapPosition position = createMapPositionFromIndex(index);
        GameField general = this.map.get(position);
        general.setPlayerIndex(playerIndex);
        general.setArmyValue(1);
        general.setGeneral(true);
    }

    public void handleAttack(GameMove move) {
        //  General rules of the game here
        if (!validMove(move)) return;

        MapPosition startPosition = createMapPositionFromIndex(move.getStart().intValue());
        MapPosition endPosition = createMapPositionFromIndex(move.getEnd().intValue());

        GameField startField = this.map.get(startPosition);
        GameField endField = this.map.get(endPosition);
        Integer reserve = move.isIs50() ? (int)Math.ceil(startField.getArmyValue() / 2.0) : 1;

        if (endField.getArmyValue() >= startField.getArmyValue() - reserve) {
            // non-takeover
            endField.setArmyValue(endField.getArmyValue() - startField.getArmyValue() - reserve);
            return;
        }

        endField.setArmyValue(startField.getArmyValue() - reserve - endField.getArmyValue());
        endField.setPlayerIndex(startField.getPlayerIndex());

        startField.setArmyValue(reserve);

    }

    public void updateVisibleFields() {
        this.map.values().stream().forEach(gameField -> {
            gameField.clearVisible();
        });

        this.map.values().stream().filter(this::isFieldOwned).forEach(gameField -> {
            gameField.makeVisible(gameField.getPlayerIndex());
            gameField.getNeighbors().stream().forEach(mapPosition -> {
                GameField neighbor = this.map.get(mapPosition);
                if (neighbor != null) {
                    neighbor.makeVisible(gameField.getPlayerIndex());
                }
            });
            gameField.getVisibleNeighbors().stream().forEach(mapPosition -> {
                GameField neighbor = this.map.get(mapPosition);
                if (neighbor != null) {
                    neighbor.makeVisible(gameField.getPlayerIndex());
                }
            });
        });


    }

    private boolean isFieldOwned(GameField gameField) {
        return !gameField.isEmpty();
    }

    private boolean validMove(GameMove move) {
        if (!isValidField(move.getStart())) return false;
        if (!isValidField(move.getEnd())) return false;

        MapPosition startPosition = createMapPositionFromIndex(move.getStart().intValue());
        MapPosition endPosition = createMapPositionFromIndex(move.getEnd().intValue());
        if (!isAdjacent(startPosition, endPosition)) return false;

        GameField endField = this.map.get(endPosition);
        if (endField.isMountain()) return false;

        GameField startField = this.map.get(startPosition);
        if (startField.getArmyValue() <= 1) return false;

        return true;
    }

    private boolean isAdjacent(MapPosition startPosition, MapPosition endPosition) {
        return Math.abs(startPosition.getCol() - endPosition.getCol()) +
                Math.abs(startPosition.getRow() - endPosition.getRow()) == 1;
    }

    private boolean isValidField(Long index) {
        return index > 0 && index <= this.map.size();
    }

    public void saveMapDataSelection(String replayId, GameMove move) {
        if (!validMove(move)) return;
        String trainingData = buildTrainingData(move);

        String selectData = String.format(TRAININGDATA_SELECTION_FILES, replayId);
        File file = new File(selectData);
        try {
            // if file doesn't exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }
            Files.write(Paths.get(selectData), trainingData.getBytes(), StandardOpenOption.APPEND);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String buildTrainingData(GameMove move) {
        // Will train selecting the start field
        // X = all fields (armyvalue, isvisible, iscity, isgeneral, index, isenemy, ismountain)
        // y = actual move start field

        StringJoiner stringJoiner = new StringJoiner(",");

        for (int index = 0; index < (30*30); index++) {  // fixed amount of fields
            MapPosition position = createMapPositionFromIndex(index);
            GameField gameField = this.map.get(position);
            if (gameField == null) {  // just so we don't have to write dummy code
                gameField = new GameField(position, index);
            }
            boolean isVisible = gameField.isVisible(move.getPlayerIndex());
            stringJoiner.add(booleanAsIntStringified(isVisible));
            stringJoiner.add(booleanAsIntStringified(gameField.isMountain()));

            if (isVisible) {
                stringJoiner.add(gameField.getArmyValue().toString());
                stringJoiner.add(booleanAsIntStringified(gameField.isCity()));
                stringJoiner.add(booleanAsIntStringified(gameField.isGeneral()));
                stringJoiner.add(booleanAsIntStringified(gameField.isEmpty()));
                boolean isEnemy = false;
                if (!gameField.isEmpty())
                    isEnemy = gameField.getPlayerIndex().intValue() != move.getPlayerIndex().intValue();

                stringJoiner.add(booleanAsIntStringified(isEnemy));
            } else {
                stringJoiner.add("0");
                stringJoiner.add("0");
                stringJoiner.add("0");
                stringJoiner.add("0");
                stringJoiner.add("0");
            }
        }

        // include the move as the label (y)
        stringJoiner.add(move.getStart().toString());
        return stringJoiner.toString().concat("\n");
    }

    private String booleanAsIntStringified(Boolean value) {
        return Integer.toString(value.compareTo(false));
    }
}
