package jpp.numbergame.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import jpp.numbergame.*;
import java.util.*;
import java.util.stream.Collectors;

public class NumberGui extends Application{

    @FXML private Pane board;
    @FXML private HBox topBar;
    @FXML private StackPane gameOverScreen;
    @FXML private Label scoreDisplayGameOver;
    @FXML private TextField playerName;
    @FXML private VBox scoreboard;
    @FXML private Button startGameButton;

    private static final Paint TEXT_WHITE = Paint.valueOf("#dedfe3");
    private static final Paint TEXT_BLACK = Paint.valueOf("#313131");
    private static final Paint EMPTY_TILE_BACKGROUND = Paint.valueOf("#998675");
    private static final int BOARD_HEIGHT = 4;
    private static final int BOARD_WIDTH = 4;
    private final StackPane[][] tiles = new StackPane[4][4];
    private final Map<Integer, Paint> tileBackgroundColors = new HashMap<>();
    private final Label scoreDisplay = new Label();
    private List<Player> scores = new ArrayList<>();
    private Game gameController;



    public static void main(String[] args) {
        Application.launch(NumberGui.class, args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("NumberGui.fxml")));
        primaryStage.setTitle("2048");
        Scene scene = new Scene(root, 800, 830);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @FXML
    public void initialize(){
        setupBoard();
        setupTileBackgroundColors();
        setupScoreDisplay();
        startGame();
    }

    private void setupBoard() {

        int x = 0,y = 0;
        for(Node n : board.getChildren()){
            StackPane tile = (StackPane) n;

            tiles[x][y] = tile;

            x++;
            if(x == BOARD_WIDTH) {
                x = 0;
                y++;
            }
        }

        for(StackPane[] row : tiles){
            for(StackPane tile : row)
                setupTile(tile);
        }
    }

    private static void setupTile(StackPane tile) {
        Rectangle background = new Rectangle(180,180);
        background.setFill(EMPTY_TILE_BACKGROUND);
        tile.setStyle("-fx-background-color: #bbada0");

        Label lbl = new Label();
        lbl.setFont(new Font("Arial", 100));
        tile.getChildren().add(background);
        tile.getChildren().add(lbl);
    }


    private void setupScoreDisplay() {
        scoreDisplay.setFont(Font.font("System", FontWeight.BOLD,22));
        topBar.getChildren().add(scoreDisplay);
    }

    private void setupTileBackgroundColors() {
        tileBackgroundColors.put(2, Paint.valueOf("#eee4da"));
        tileBackgroundColors.put(4, Paint.valueOf("#ede0c8"));
        tileBackgroundColors.put(8, Paint.valueOf("#f2b179"));
        tileBackgroundColors.put(16, Paint.valueOf("#f59563"));
        tileBackgroundColors.put(32, Paint.valueOf("#f67c5f"));
        tileBackgroundColors.put(64, Paint.valueOf("#f65e3b"));
        tileBackgroundColors.put(128, Paint.valueOf("#edcf72"));
        tileBackgroundColors.put(256, Paint.valueOf("#edcc61"));
        tileBackgroundColors.put(512, Paint.valueOf("#edc850"));
        tileBackgroundColors.put(1024, Paint.valueOf("#edc53f"));
        tileBackgroundColors.put(2048, Paint.valueOf("#edc22e"));
        tileBackgroundColors.put(4096, Paint.valueOf("#3c3a32"));
        tileBackgroundColors.put(8192, Paint.valueOf("#3c3a32"));
    }

    public void startGame(){
        hideAllTiles();

        gameController = new Game(BOARD_WIDTH,BOARD_HEIGHT);
        displayTile(gameController.addRandomTile());
        displayTile(gameController.addRandomTile());

        scoreDisplay.setText("Score: 0");
        gameOverScreen.setVisible(false);

        Platform.runLater(() -> board.requestFocus());
    }

    public void keyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getCode()){
            case  UP -> move(Direction.UP);
            case DOWN -> move(Direction.DOWN);
            case RIGHT -> move(Direction.RIGHT);
            case LEFT -> move(Direction.LEFT);
        }

        if(!gameController.canMove()) gameOver();
    }

    public void submitScore(){
        String name = playerName.getText();
        int score = gameController.getScore();

        if(name.isEmpty() || name.isBlank()){
            playerName.clear();
            playerName.setPromptText("Name must not be empty");
        } else {
            scores.add(new Player(name, score));
            scores.sort(Comparator.comparingInt(Player::getScore));
            Collections.reverse(scores);
            scores = scores.stream().limit(5).collect(Collectors.toList());
            showScoreboard();
            startGameButton.disableProperty().set(false);
        }
    }

    private void move(Direction dir){
        if(gameController.canMove(dir)){
            List<Move> moves = gameController.move(dir);
            for(Move move : moves){
                hideTile(move.getFrom());
                displayTile(move.getNewValue(), move.getTo());
            }
            displayTile(gameController.addRandomTile());
            scoreDisplay.setText("Score: " + gameController.getScore());
        }
    }

    private void gameOver(){
        playerName.clear();
        playerName.setPromptText("My Name");

        gameOverScreen.setVisible(true);
        startGameButton.disableProperty().set(true);

        Platform.runLater(() -> scoreboard.requestFocus());

        scoreDisplayGameOver.setText("Score: " + gameController.getScore());
        showScoreboard();
    }

    private void showScoreboard(){
        for(int i = 0; i < scores.size(); i++){
            HBox scoreLine = (HBox) scoreboard.getChildren().get(i+1);
            Label name = (Label) scoreLine.getChildren().get(0);
            Label score = (Label) scoreLine.getChildren().get(1);

            score.setText(scores.get(i).getName());
            name.setText(String.valueOf(scores.get(i).getScore()));
        }
    }

    private void displayTile(Tile tile){
        displayTile(tile.getValue(), tile.getCoordinate());
    }

    private void displayTile(int value, Coordinate2D pos){
        Label label = (Label) tiles[pos.getX()][pos.getY()].getChildren().get(1);
        Rectangle background = (Rectangle) tiles[pos.getX()][pos.getY()].getChildren().get(0);

        label.setText(Integer.toString(value));

        if(value <= 4){
            label.setTextFill(TEXT_BLACK);
        } else {
            label.setTextFill(TEXT_WHITE);
        }

        background.setFill(tileBackgroundColors.get(value));
    }
    private void hideAllTiles() {
        for(int x = 0; x < BOARD_WIDTH; x++){
            for(int y = 0; y < BOARD_HEIGHT; y++){
                hideTile(new Coordinate2D(x,y));
            }
        }
    }

    private void hideTile(Coordinate2D pos){
        Label label = (Label) tiles[pos.getX()][pos.getY()].getChildren().get(1);
        Rectangle background = (Rectangle) tiles[pos.getX()][pos.getY()].getChildren().get(0);

        label.setTextFill(EMPTY_TILE_BACKGROUND);
        background.setFill(EMPTY_TILE_BACKGROUND);
    }
}
