package com.arlan.chess.view;

import com.arlan.chess.controller.BoardController;
import com.arlan.chess.controller.GameController;
import com.arlan.chess.controller.PieceController;
import com.arlan.chess.model.Board;
import com.arlan.chess.model.figures.Cell;
import com.arlan.chess.model.figures.Piece;
import javafx.scene.Group;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.HashMap;

/**
 * The MainGameView is responsible for invoking other Views
 */

public class MainGameView {
    private final BoardController boardController;
    private final BoardDrawUtilsView boardDrawUtilsView;
    private final PieceController pieceController;
    private final FiguresDrawView figuresDrawView;
    private GameController gameController;

    /**
     * MainGameView constructor. All controllers and views are declared for using their methods and variables.
     *
     * @param boardController object of BoardController
     * @param boardDrawUtilsView object of BoardDrawUtils
     * @param pieceController object of pieceController
     * @param figuresDrawView object of figuresDrawview
     */
    public MainGameView(BoardController boardController, BoardDrawUtilsView boardDrawUtilsView,
                        PieceController pieceController, FiguresDrawView figuresDrawView) {
        this.boardController = boardController;
        this.boardDrawUtilsView = boardDrawUtilsView;
        this.pieceController = pieceController;
        this.figuresDrawView = figuresDrawView;
    }



    private HashMap<Cell, Piece> cellsOnBoard = new HashMap<>();

    /**
     * Draw chessboard and pieces in classic positions using JavaFX
     * @param border BorderPane to set chessboard with piece on center of BorderPane
     * @param board logic board which contains all data about current chessboard (Pieces position, etc.)
     */
    public void drawBoard(final BorderPane border, Board board) {

        // Group class (grid) consists of other elements like Rectangle (cells), Rectangle (pieces)
        Group grid = boardDrawUtilsView.drawCells();

        // Center chess board in BorderPane
        boardController.setUiBoard(grid);
        border.setCenter(grid);

        // Add Board Model to store pieces.

        cellsOnBoard = board.getCellsToPieces();
        // Draw classic chess layout
        if (gameController.isLoadGame()) {
            figuresDrawView.drawPieces(grid, board.getCellsToPieces());
        } else {
            drawClassicPiecesPositions(grid, cellsOnBoard, board);
        }
        // TODO layout for manual

    }

    /**
     * Draw chess pieces above chessboard grip in classic positions using JavaFX.
     *
     * @param board JavaFX Group container which is chess grid
     * @param cellsOnBoard HashMap contains active pieces on chessBoard
     * @param LogicBoard logic board which contains all data about current chessboard
     */
    private void drawClassicPiecesPositions(Group board, HashMap<Cell, Piece> cellsOnBoard, Board LogicBoard) {
        figuresDrawView.drawBlackTeam(board, cellsOnBoard);
        figuresDrawView.drawWhiteTeam(board, cellsOnBoard);
    }

    /**
     * Set text to show Game info for user such as: Which is turn, who is in check etc.
     * @param text JavaFX Text for showing text
     * @param color JavaFX to set text color
     * @param message set message as text
     */

    public void setText(Text text, Color color, String message) {
        text.setFill(color);
        text.setText(message);
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }
}
