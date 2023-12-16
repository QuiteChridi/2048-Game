package jpp.numbergame;

import java.util.Objects;

public class Move {
    private final Coordinate2D from;
    private final  Coordinate2D to;
    private final int oldValue;
    private final int newValue;
    public Move(Coordinate2D from, Coordinate2D to, int oldValue, int newValue){
        if(oldValue < 1 || newValue < 1) throw new IllegalArgumentException();

        this.from = from;
        this.to = to;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public Move(Coordinate2D from, Coordinate2D to, int value){
        if(value < 1) throw new IllegalArgumentException();

        this.from = from;
        this.to = to;
        this.oldValue = value;
        this.newValue = value;
    }

    public Coordinate2D getFrom() {
        return from;
    }

    public Coordinate2D getTo() {
        return to;
    }

    public int getOldValue() {
        return oldValue;
    }

    public int getNewValue() {
        return newValue;
    }

    public boolean isMerge(){
        return oldValue != newValue;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(from);
        sb.append(" = ");
        sb.append(oldValue);
        sb.append(" -> ");
        sb.append(to);
        sb.append(" = ");
        sb.append(newValue);

        if(isMerge()){
            sb.append(" (M)");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return oldValue == move.oldValue && newValue == move.newValue && Objects.equals(from, move.from) && Objects.equals(to, move.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, oldValue, newValue);
    }
}

