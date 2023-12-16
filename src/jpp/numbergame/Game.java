package jpp.numbergame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {

    private final Tile [] [] board;
    private int score;

    public Game(int width, int height){
        if(width < 1 || height < 1) throw new IllegalArgumentException();

        this.board = new Tile[width][height];
        this.score = 0;
    }

    public Game(int width, int height, int initialTiles){
        if(initialTiles < 0 || initialTiles > width*height) throw new IllegalArgumentException();

        this.board = new Tile[width][height];
        this.score = 0;

        for(int i = 0; i < initialTiles; i++){
            addRandomTile();
        }
    }

    public int getScore(){
        return score;
    }

    public boolean isFull(){
        for(Tile[] line : board){
            for(Tile tile : line){
                if(tile == null) return false;
            }
        }
        return true;
    }

    public Tile addRandomTile(){
        if(isFull()) throw new TileExistsException();

        Tile newTile = null;
        Random ran = new Random();
        boolean tilePlaced = false;
        int value, x, y;

        if(ran.nextInt(10) == 0){
            value = 4;
        } else {
            value = 2;
        }

        while(!tilePlaced){
            x = ran.nextInt(board.length);
            y = ran.nextInt(board[0].length);

            if(board[x][y] == null){
                 newTile = addTile(x,y, value);
                 tilePlaced = true;
            }
        }

        return newTile;
    }

    public Tile addTile(int x, int y, int value){
        if(board[x][y] != null) throw new TileExistsException();

        Tile tile = new Tile(new Coordinate2D(x,y), value);
        board[x][y] = tile;
        return tile;
    }

    public List<Move> move(Direction dir){
        return switch (dir) {
            case DOWN -> moveDOWN();
            case UP -> moveUP();
            case LEFT -> moveLEFT();
            case RIGHT -> moveRight();
        };
    }

    private List<Move> moveUP(){
        List<Move> moves = new ArrayList<>();
        Coordinate2D lastTile, to;


        for(int x = 0; x < board.length; x++){
            lastTile = null;

            for(int y = 0; y < board[0].length; y++){

                if(board[x][y] != null){
                    int value = board[x][y].getValue();

                    if(canMerge(lastTile, value)){
                        board[lastTile.getX()][lastTile.getY()] = new Tile(lastTile, value*2);
                        moves.add(new Move(new Coordinate2D(x,y), lastTile, value, value*2));
                        board[x][y] = null;
                        score += value*2;
                        lastTile = null;
                    } else {

                        int distance = 0;
                        while (y-distance > 0 && board[x][y-distance-1] == null){
                            distance++;
                        }

                        if(distance > 0){
                            to = new Coordinate2D(x,y-distance);
                            moves.add(new Move(new Coordinate2D(x,y), to, value, value));
                            board[to.getX()][to.getY()] = new Tile(to, value);
                            board[x][y] = null;
                            lastTile = to;
                        } else{
                            lastTile = new Coordinate2D(x,y);
                        }
                    }
                }
            }
        }
        return moves;
    }

    private List<Move> moveDOWN(){
        List<Move> moves = new ArrayList<>();
        Coordinate2D lastTile, to;
        for(int x = 0; x < board.length; x++){
            lastTile = null;
            for(int y = board[0].length-1; y >= 0; y--){
                if(board[x][y] != null){
                    int value = board[x][y].getValue();

                    //if merge possible
                    if(canMerge(lastTile, value)){
                        board[lastTile.getX()][lastTile.getY()]= new Tile(lastTile, value*2);
                        moves.add(new Move(new Coordinate2D(x,y), lastTile, value, value*2));
                        board[x][y] = null;
                        score += value*2;
                        //reset last tile, so tile cant be merged twice
                        lastTile = null;
                    } else {
                        int distance = 0;
                        while (y+distance < board.length-1 && board[x][y+distance+1] == null){
                            distance++;
                        }

                        if(distance > 0){
                            to = new Coordinate2D(x,y+distance);
                            moves.add(new Move(new Coordinate2D(x,y), to, value, value));
                            board[to.getX()][to.getY()] = new Tile(to, value);
                            board[x][y] = null;
                            //set tile as last tile for other tiles to merge
                            lastTile = to;
                        } else {
                            lastTile = new Coordinate2D(x,y);
                        }
                    }
                }
            }
        }
        return moves;
    }


    private List<Move> moveLEFT(){
        List<Move> moves = new ArrayList<>();
        Coordinate2D lastTile,to;
        for(int y = 0; y < board[0].length; y++){
            lastTile = null;
            for(int x = 0; x < board.length; x++) {
                if(board[x][y] != null){
                    int value = board[x][y].getValue();

                    //if merge possible
                    if(canMerge(lastTile, value)){
                        board[lastTile.getX()][lastTile.getY()]= new Tile(lastTile, value*2);
                        moves.add(new Move(new Coordinate2D(x,y), lastTile, value, value*2));
                        board[x][y] = null;
                        score += value*2;
                        //reset last tile, so tile cant be merged twice
                        lastTile = null;
                    } else {
                        int distance = 0;
                        while (x-distance > 0 && board[x-distance-1][y] == null){
                            distance++;
                        }

                        if(distance > 0){
                            to = new Coordinate2D(x-distance,y);
                            moves.add(new Move(new Coordinate2D(x,y), to, value, value));
                            board[to.getX()][to.getY()] = new Tile(to, value);
                            board[x][y] = null;
                            //set tile as last tile for other tiles to merge
                            lastTile = to;
                        } else {
                            lastTile = new Coordinate2D(x,y);
                        }
                    }
                }
            }
        }
        return moves;
    }

    private List<Move> moveRight(){
        List<Move> moves = new ArrayList<>();
        Coordinate2D lastTile,to;
        for(int y = 0; y < board[0].length; y++) {
            lastTile = null;
            for (int x = board.length-1; x >= 0; x--) {
                if (board[x][y] != null) {
                    int value = board[x][y].getValue();

                    //if merge possible
                    if (canMerge(lastTile, value)) {
                        board[lastTile.getX()][lastTile.getY()] = new Tile(lastTile, value * 2);
                        moves.add(new Move(new Coordinate2D(x, y), lastTile, value, value * 2));
                        board[x][y] = null;
                        score += value * 2;
                        //reset last tile, so tile cant be merged twice
                        lastTile = null;
                    } else {
                        int distance = 0;
                        while (x+distance < board.length-1 && board[x+distance+1][y] == null){
                            distance++;
                        }

                        if (distance > 0) {
                            to = new Coordinate2D(x + distance, y);
                            moves.add(new Move(new Coordinate2D(x, y), to, value, value));
                            board[to.getX()][to.getY()] = new Tile(to, value);
                            board[x][y] = null;
                            //set tile as last tile for other tiles to merge
                            lastTile = to;
                        } else {
                            lastTile = new Coordinate2D(x, y);
                        }
                    }
                }
            }
        }
        return moves;
    }

    public boolean canMove(){
        for(Direction dir : Direction.values()){
            if(canMove(dir)){
                return true;
            }
        }
        return false;
    }

    public boolean canMove(Direction dir){

        for(int x = 0; x < board.length; x++){
            for(int y = 0; y < board[0].length; y++) {
                if (board[x][y] != null) {
                    int value = board [x][y].getValue();
                    Coordinate2D newSpot = null;

                    switch (dir) {
                        case UP -> {
                            if(y-1 >= 0){newSpot = new Coordinate2D(x, y-1);}
                        }
                        case DOWN -> {
                            if(y+1 < board[0].length){newSpot = new Coordinate2D(x, y+1);}
                        }
                        case RIGHT -> {
                            if(x+1 < board.length){newSpot = new Coordinate2D(x+1,y);}
                        }
                        case LEFT -> {
                            if(x-1 >= 0){newSpot = new Coordinate2D(x-1,y);}
                        }
                    }

                    if(newSpot != null && (getTile(newSpot) == null || canMerge(newSpot, value))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private Tile getTile(Coordinate2D spot){
        if(spot == null) throw new IllegalArgumentException();

        return board[spot.getX()][spot.getY()];
    }
    private boolean canMerge(Coordinate2D tileToMerge, int value) {
        return tileToMerge != null && board[tileToMerge.getX()][tileToMerge.getY()].getValue() == value;
    }
}
