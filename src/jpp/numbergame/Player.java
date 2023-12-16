package jpp.numbergame;

public class Player {
    private int score;
    private String name;

    public Player(String name, int score) {
        this.score = score;
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }
}
