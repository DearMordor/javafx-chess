package com.arlan.chess.controller;

import com.arlan.chess.model.Board;
import com.arlan.chess.model.Move;
import com.arlan.chess.model.figures.Cell;
import com.arlan.chess.model.figures.Piece;
import com.arlan.chess.view.BoardDrawUtilsView;
import com.arlan.chess.view.FiguresDrawView;
import com.arlan.chess.view.MainGameView;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.HashMap;

public class GameController {

    Board board;
    private BoardController boardController;
    private BoardDrawUtilsView boardDrawUtilsView;
    private PieceController pieceController;
    private FiguresDrawView figuresDrawView;
    private boolean isOver = false;
    // If white king in check
    private boolean isWhiteCheck = false;
    // If black king in check
    private boolean isBlackCheck = false;
    // true = white move , false = black move
    private boolean turnToMove = true;
//    Color currentColor = Color.WHITE;
    private boolean firstMove = true;
    private MainGameView mainGameView;
    private boolean gameStarted = false;
    boolean isLoadGame = false;

    private boolean versusBot;
    private Text text = new Text();

    // Declare bot to play against
    private final BotController AI = new BotController(false);

    /**
     * Check if game is over. It controls if there is at least one available move. If there is not,
     * then it is over for who
     *
     * @return true if there is move, false if not
     */
    public boolean isGameOver() {
        boolean white = !board.getLastMove().getPieceMoved().isWhite();
        BotController bot = new BotController(white);
        bot.setGameController(this);
        bot.setBoardController(boardController);
        Move move = bot.findMove(board);
        return move == null;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public boolean isWhiteCheck() {
        return isWhiteCheck;
    }

    public void setWhiteCheck(boolean whiteCheck) {
        isWhiteCheck = whiteCheck;
    }

    public boolean isBlackCheck() {
        return isBlackCheck;
    }

    public void setBlackCheck(boolean blackCheck) {
        isBlackCheck = blackCheck;
    }

    /*
    * Only white piece can move first
    * */
    public boolean makeFirstMove(Piece piece) {
        if (turnToMove == piece.isWhite() && firstMove) {
            setFirstMove(false);
            return true;
        }

        return false;
    }

    /**
     * Open new JavaFX window where game starts
     * @param stage JavaFX stage
     * @param mode mode for playing. Two mods: Player, AI.
     */
    public void openWindow(Stage stage, String mode, boolean loadGame,  HashMap<Cell, Piece> cellToPieces) {

        if (loadGame) {
            isLoadGame = true;
        }

        if (mode.equals("Player")) {
            versusBot = false;
            System.out.println("Player vs Player");
        }

        if (mode.equals("AI")) {
            versusBot = true;
            System.out.println("Game versus Bot");
        }

        // Define new BorderPane where is chessboard, timer, game info and buttons located.

        BorderPane border = new BorderPane();
        HBox statusbar = new HBox();



//        statusbar.setStyle("-fx-border-color: black");
        statusbar.getChildren().addAll(text);
        statusbar.setAlignment(Pos.CENTER);
        statusbar.setSpacing(20);
        statusbar.setPrefHeight(20);

        text.setText("Whites move");
        text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));

        border.setTop(statusbar);

        // Start new game
        newGame(border, loadGame, cellToPieces);

//        border.setStyle("-fx-background-color: #282c34");
        Scene scene = new Scene(border);
        stage.setScene(scene);
        stage.setTitle("Chess");
        stage.setWidth(500);
        stage.setHeight(500);
        stage.show();
    }

    /**
     * Start new game by declaring all needed controllers and views.
     * @param mainGameWindow main border pane where is chessboard, timers, game info and board are located.
     */
    private void newGame(BorderPane mainGameWindow, boolean loadGame,  HashMap<Cell, Piece> cellsToPieces) {

        board = new Board();

        // Set controllers and views
        boardController = new BoardController(board);
        pieceController = new PieceController(boardController);
        figuresDrawView = new FiguresDrawView(pieceController);
        boardDrawUtilsView = new BoardDrawUtilsView();
        boardController.setFiguresDrawView(figuresDrawView);
        MainGameView mainGameView = new MainGameView(boardController, boardDrawUtilsView,
                pieceController, figuresDrawView);
        figuresDrawView.setBoard(board);
        boardController.setPieceController(pieceController);
        boardController.setGameController(this);
        pieceController.setBoard(board);
        this.setBoard(board);
        pieceController.setGameController(this);
        this.setBoardController(boardController);
        this.setMainGameView(mainGameView);
        pieceController.setText(text);
        boardController.setText(text);
        pieceController.setMainGameView(mainGameView);
        boardController.setMainGameView(mainGameView);
        mainGameView.setGameController(this);

        // Set controllers for AI
        AI.setGameController(this);
        AI.setBoardController(boardController);
        AI.setPieceController(pieceController);
        AI.setFiguresDrawView(figuresDrawView);
        HBox timers = makeTimer();
        timers.setAlignment(Pos.CENTER);

        // Build classic chess layout in Board logical Model
        if (loadGame && cellsToPieces != null) {
            board.setCellsToPieces(cellsToPieces);
            board.setWhiteAndBlackKingFromHashMap();
        } else {
            board.setPieceOnClassicPositions();
        }
        mainGameView.drawBoard(mainGameWindow, board);
        mainGameWindow.setBottom(timers);

        VBox buttons = new VBox();
        Button saveGameBtn = new Button("Save game");
        Button exit = new Button("Exit");

        exit.setOnAction(actionEvent -> {
            Platform.exit();
        });

        SaveLoadController saveLoadController = new SaveLoadController();
        saveLoadController.setGameController(this);
        saveLoadController.setSaveButtonOnEvent(saveGameBtn, board.getCellsToPieces());
        if (loadGame) {
            turnToMove = saveLoadController.isTurnToMove();
        }


        saveGameBtn.setMinWidth(buttons.getPrefWidth());
        exit.setMinWidth(buttons.getPrefWidth());

        buttons.getChildren().addAll(saveGameBtn, exit);
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(50);
        mainGameWindow.setRight(buttons);

    }

    /**
     * Make white and black timers as Hbox
     * @return Hbox which contains timers as JavaFX Texts
     */
    private HBox makeTimer() {
        TimerController timerController = new TimerController();
        timerController.setGameController(this);

        timerController.getWhiteTimer().setFont(Font.font("verdana",20));
        timerController.getBlackTimer().setFont(Font.font("verdana",20));

        HBox timers = new HBox();
        timers.setAlignment(Pos.CENTER);
        timers.setPrefHeight(50);
        timers.setSpacing(50);
        timers.getChildren().addAll(timerController.getBlackTimer(), timerController.getWhiteTimer());
        return timers;
    }

    public boolean isFirstMove() {
        return firstMove;
    }

    public void setFirstMove(boolean firstMove) {
        this.firstMove = firstMove;
    }

    public void setBoardController(BoardController boardController) {
        this.boardController = boardController;
    }

    public void setMainGameView(MainGameView mainGameView) {
        this.mainGameView = mainGameView;
    }

    public boolean isVersusBot() {
        return versusBot;
    }

    public BotController getAI() {
        return AI;
    }

    public void flipTurn() {
        turnToMove = !turnToMove;
    }

    public boolean isOver() {
        return isOver;
    }

    public void setOver(boolean over) {
        isOver = over;
    }

    public boolean isTurnToMove() {
        return turnToMove;
    }

    public void setTurnToMove(boolean turnToMove) {
        this.turnToMove = turnToMove;
    }

    public boolean isLoadGame() {
        return isLoadGame;
    }

    public void setLoadGame(boolean loadGame) {
        isLoadGame = loadGame;
    }
}
