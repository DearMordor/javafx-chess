package com.arlan.chess.controller;

import com.arlan.chess.model.Board;
import com.arlan.chess.model.Move;
import com.arlan.chess.model.MoveType;
import com.arlan.chess.model.figures.*;
import com.arlan.chess.view.FiguresDrawView;
import com.arlan.chess.view.MainGameView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * The PieceController is responsible for dragging Pieces on chessboard.
 */
public class PieceController {

    private final BoardController boardController;
    private GameController gameController;
    private Board board;
    private static final Logger LOG = Logger.getLogger(PieceController.class.getName());
    private Text text;
    private MainGameView mainGameView;

    /**
     * PieceController constructor
     * @param boardController boardController object for using Board Model and functions related to the logic Board
     */

    public PieceController(BoardController boardController) {
        this.boardController = boardController;
    }

    /**
     * Set events in Chess piece. Events are: dragging and releasing. It has validation for valid moving.
     *
     * @param rect chess piece javafx rectangle which is piece image
     * @param piece chess piece
     */
    public void setMouseEventOnPiece(Rectangle rect, Piece piece) {

        AtomicInteger currentPieceX = new AtomicInteger(piece.getPosition().getX() * 40 + 5);
        AtomicInteger currentPieceY = new AtomicInteger(piece.getPosition().getY() * 40 + 5);

        // Set mouse event for drag move
        rect.setOnMouseDragged(
                event -> {

                    if (gameController.isOver()) {
                        return;
                    }

                    if (event.getX() < 0 || event.getY() < 0) {
                        return;
                    }

                    if (event.getX() >= 320 || event.getY() >= 320 ) {
                        return;
                    }

                    // Hold piece at the cursor end
                    rect.setX(event.getX() - 30 / 2);
                    rect.setY(event.getY() - 30 / 2);
                }


        );

        // Set mouse event
        rect.setOnMouseReleased(

                event -> {

                    int releaseX = ((int) event.getX() / 40) * 40 + 5;
                    int releaseY = ((int) event.getY() / 40) * 40 + 5;

                    Cell end = Cell.fromIntToCell(releaseX / 40, releaseY / 40);

                    if(gameController.isTurnToMove()) {
                        mainGameView.setText(text, Color.BLACK, "Black moves!");
                    } else {
                        mainGameView.setText(text, Color.BLACK, "White moves!");
                    }

                    if (!gameController.isOver()) {
                        if (boardController.isDragWithinBoard(end) &&
                                boardController.isValidMove(end, piece)) {

                            setRectPosition(rect, releaseX, releaseY);
                            if (!(board.getLastMove().getMoveType() == MoveType.ROGUE) &&
                                    !(board.getLastMove().getMoveType() == MoveType.PROMOTION)) {
                                boardController.updatePiecePositionInLogicBoard(releaseX / 40,
                                        releaseY / 40,
                                        piece);
                            }

                            // Evident first Pawn move
                            if (piece instanceof Pawn && ((Pawn) piece).isFirstMove()) {
                                ((Pawn) piece).setFirstMove(false);
                            }

                            // Evident if king was moved for Rogue condition
                            if (piece instanceof King && ((King) piece).isFirstMove()) {
//                                System.out.println("King was moved! Castling cannot be performed!");
                                ((King) piece).setFirstMove(false);
                            }

                            // Evident if Rook was moved for Rogue condition
                            if (piece instanceof Rook && ((Rook) piece).isFirstMove()) {
                                ((Rook) piece).setFirstMove(false);
                            }

                            currentPieceX.set(releaseX);
                            currentPieceY.set(releaseY);

                            if (boardController.isCheck(board.getBlackKing())) {
                                mainGameView.setText(text, Color.BLACK, "Black King check!");
                                System.out.println("Black King check!");

                            }

                            if (boardController.isCheck(board.getWhiteKing())) {
                                mainGameView.setText(text, Color.BLACK, "White King check!");
                                System.out.println("White king check!");
                            }

                            if (gameController.isVersusBot()) {
                                Move move = gameController.getAI().findMove(board);
                                gameController.flipTurn();

                                if(gameController.isTurnToMove()) {
                                    mainGameView.setText(text, Color.BLACK, "Black moves!");
                                } else {
                                    mainGameView.setText(text, Color.BLACK, "White moves!");
                                }

                                if (move != null) {
                                    // Wait 1 second to move
                                    try {
                                        TimeUnit.SECONDS.sleep(1);
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }

                                    gameController.getAI().makeMove(move);


                                } else {
                                    gameController.setOver(true);
                                    LOG.log(Level.WARNING, "AI could not find a move");
                                }
                            }

                            if(gameController.isGameOver()) {
                                gameController.setOver(true);
                            } else {
                                gameController.flipTurn();

                            }

                        } else {
                            LOG.log(Level.WARNING, "Invalid move!");
                            mainGameView.setText(text, Color.RED, "Invalid move!");
                            setRectPosition(rect, currentPieceX.get(), currentPieceY.get());

                        }

                    }
                    if (gameController.isOver()) {
                        if(gameController.isTurnToMove()) {
                            System.out.println("WHITE WINS!");
                            mainGameView.setText(text, Color.BLACK, "White Wins!");
                        } else {
                            System.out.println("BLACK WINS!");
                            mainGameView.setText(text, Color.BLACK, "Black Wins!");
                        }
                    }
                }
        );

    }

    /*
     *  Set piece position on chess board ( piece is represented by JavaFX Rectangle, which is image)
     * */
    public void setRectPosition(Rectangle rect, int x, int y) {
        rect.setX(x);
        rect.setY(y);
    }


    public void setBoard(Board board) {
        this.board = board;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public void setText(Text text) {
        this.text = text;
    }

    public void setMainGameView(MainGameView mainGameView) {
        this.mainGameView = mainGameView;
    }
}
