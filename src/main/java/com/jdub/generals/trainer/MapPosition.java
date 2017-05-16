package com.jdub.generals.trainer;

/**
 * Created by jameswarren on 5/1/17.
 */
public class MapPosition {
    private Integer row, col;

    public MapPosition(Integer col, Integer row) {
        this.col = col;
        this.row = row;
    }

    public Integer getRow() {
        return row;
    }

    public Integer getCol() {
        return col;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + row;
        hash = 31 * hash + col;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || (obj.getClass() != this.getClass())) return false;
        MapPosition location = (MapPosition)obj;
        return location.getCol() == this.col && location.getRow() == this.row;
    }

    @Override
    public String toString() {
        return String.format("Row: %s, Col: %s", this.row, this.col);
    }
}
