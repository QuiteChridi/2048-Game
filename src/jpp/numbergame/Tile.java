package jpp.numbergame;

import java.util.Objects;

public class Tile {
    private final Coordinate2D position;
    private final int value;
    public Tile(Coordinate2D coord, int value){
        if(value < 1) throw new IllegalArgumentException();
        this.position = coord;
        this.value = value;
    }

    public Coordinate2D getCoordinate() {
        return position;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tile tile = (Tile) o;
        return value == tile.value && Objects.equals(position, tile.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, value);
    }
}
