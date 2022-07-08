package com.arlan.chess.controller;

import com.arlan.chess.model.Board;
import com.arlan.chess.model.Move;
import com.arlan.chess.model.MoveType;
import com.arlan.chess.model.figures.*;
import com.arlan.chess.view.BoardDrawUtilsView;
import com.arlan.chess.view.FiguresDrawView;
import com.arlan.chess.view.MainGameView;
import javafx.scene.Group;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The BoardController class is responsible for interacting with logic board
 */
public class BoardController {

    private final Board board;
    private FiguresDrawView figuresDrawView;
    private Group uiBoard;
    private PieceController pieceController;
    private GameController gameController;
    private static final Logger LOG = Logger.getLogger(PieceController.class.getName());
    private Text text;
    private MainGameView mainGameView;

    private static final int BOARD_EDGE_END = 325;
    private static final int BOARD_EDGE_START = 0;


    /**
     * BoardController constructor
     *
     * @param board logic board
     */
    public BoardController(Board board) {
        this.board = board;
    }

    /**
     * Calculate legal moves for all pieces on Logic board
     *
     * @param board logic board
     */
    public void calculateLegalMovesForAllPiecesOnClassicPositions(Board board) {
        board.getCellsToPieces().forEach((cell, piece) -> {
            piece.getLegalMoves(board);
        });
    }

    /**
     * Validate if user drags chess piece within chessboard. He can not move it outside of it.
     *
     * @param destination current destination of mouse drag
     * @return true if drag is within board, false if it is not
     */
    boolean isDragWithinBoard(Cell destination) {
        boolean ret = false;

        if (destination == null) {
            return false;
        }

        if (destination.getX() < BOARD_EDGE_END && destination.getY() < BOARD_EDGE_END &&
                destination.getX() > BOARD_EDGE_START && destination.getY() > BOARD_EDGE_START) {
            ret = true;

        } else {
            mainGameView.setText(text, Color.RED, "You can not drag figure beyond the board!");
        }
        return ret;
    }

    /**
     * Update Piece position in logic board
     *
     * @param x     x position by Cells (0-7)
     * @param y     y position by Cells (0-7)
     * @param piece piece that we want to move
     */
    public void updatePiecePositionInLogicBoard(int x, int y, Piece piece) {

        Cell end = Cell.fromIntToCell(x, y);
        Cell start = piece.getPosition();

        // Update piece's position on logic board
        board.getCellsToPieces().remove(start, piece);
        piece.setPosition(end);

        board.getCellsToPieces().put(end, piece);

        if (piece instanceof Pawn) {
            if (((Pawn) piece).isPromotion()) {
                board.getCellsToPieces().remove(end, piece);
            }
        }

        board.getCellsToPieces().values().remove(null);

    }

    /**
     * Validate if move is valid
     *
     * @param end   Cell destination
     * @param piece that moves
     * @return true if it is valid, else false
     */
    public boolean isValidMove(Cell end, Piece piece) {

        if (piece.isWhite() != gameController.isTurnToMove() && !gameController.isVersusBot()) {
            LOG.log(Level.WARNING, "It is not your turn!");
            mainGameView.setText(text, Color.RED, "It is not your turn!");
            return false;
        }

        // If player plays against bot he can't move bot's pieces
        if (gameController.isVersusBot() && !piece.isWhite()) {
            LOG.log(Level.WARNING, "It is not your turn!");
            return false;
        }

        boolean ret = false;

        for (Move move : piece.getLegalMoves(board)) {

            if (move.getEnd() == end && move.getStart() != end) {

                if (isMoveLeadsToCheck(move)) {

                    if (move.getPieceMoved().isWhite()) {
                        gameController.setWhiteCheck(true);
                    } else {
                        gameController.setBlackCheck(true);
                    }
                    break;
                }

                ret = true;

                // MoveType validation
                if (move.getMoveType() == MoveType.ATTACK || move.getMoveType() == MoveType.ENPASSANT) {

                    // delete figure from logic board
                    deletePieceOnLogicBoard(move.getPieceKilled());

                    // delete figure from board UI
                    figuresDrawView.deleteFigureFromUiBoard(move.getPieceKilled());

                    addMoveToHistory(move);
                    break;

                } else if (move.getMoveType() == MoveType.PROMOTION) {

                    // Delete figure from logic board and UI board
                    if (move.getPieceKilled() != null) {
                        figuresDrawView.deleteFigureFromUiBoard(move.getPieceKilled());
                        deletePieceOnLogicBoard(move.getPieceKilled());
                    }

                    String[] piecesForPromotion = {"Knight", "Bishop", "Queen", "Rook"};

                    // JavaFx Choice window for picking piece
                    ChoiceDialog choiceDialog = new ChoiceDialog(piecesForPromotion[0], piecesForPromotion);
                    choiceDialog.setContentText("Choose your piece");
                    choiceDialog.showAndWait();

                    // Get user's choice
                    String result = (String) choiceDialog.getResult();

                    Cell promotePosition =
                            move.getPieceKilled() != null ? move.getPieceKilled().getPosition() :
                                    Cell.fromIntToCell(move.getPieceMoved().getPosition().getX(),
                                            move.getPieceMoved().getPosition().getY() - 1);

                    LOG.log(Level.INFO, "promotePosition: " + promotePosition);
                    if (result == null || result.equals("Knight")) {
                        promotePiece(new Knight(move.getPieceMoved().isWhite(),
                                promotePosition), move.getPieceMoved());
                    } else if (result.equals("Rook")) {
                        promotePiece(new Rook(move.getPieceMoved().isWhite(),
                                promotePosition), move.getPieceMoved());
                    } else if (result.equals("Queen")) {
                        promotePiece(new Queen(move.getPieceMoved().isWhite(),
                                promotePosition), move.getPieceMoved());
                    } else if (result.equals("Bishop")) {
                        promotePiece(new Bishop(move.getPieceMoved().isWhite(),
                                promotePosition), move.getPieceMoved());
                    }

                    addMoveToHistory(move);
                    break;

                } else if (move.getMoveType() == MoveType.MOVEMENT) {
                    addMoveToHistory(move);

                    break;
                }


            } else if (move.getMoveType() == MoveType.ROGUE && move.getStart() == end &&
                    move.getPieceMoved() instanceof Rook) {
                ret = true;
                makeRogueMove((Rook) board.getCellsToPieces().get(move.getPieceMoved().getPosition()), move);

                addMoveToHistory(move);
                break;
            }


        }

        return ret;

    }

    public void setFiguresDrawView(FiguresDrawView figuresDrawView) {
        this.figuresDrawView = figuresDrawView;
    }

    public void setUiBoard(Group uiBoard) {
        this.uiBoard = uiBoard;
    }

    /**
     * Promote piece by deleting promotedPawn in logic and UI board and adding new promoted piece.
     *
     * @param piece        that we want to get for promoted pawn
     * @param promotedPawn promoted pawn
     */
    public void promotePiece(Piece piece, Piece promotedPawn) {

        // Set promotion on true
        ((Pawn) promotedPawn).setPromotion(true);
        // Delete pawn used for promotion
        deletePieceOnLogicBoard(promotedPawn);

        // Add new promoted piece to list
        board.getCellsToPieces().put(piece.getPosition(), piece);

        // Delete pawn from UI board
        figuresDrawView.deleteFigureFromUiBoard(promotedPawn);
        // Draw new promoted piece
        figuresDrawView.drawFigure(piece, uiBoard);
    }

    public void deletePieceOnLogicBoard(Piece deletePiece) {
        if (board.getCellsToPieces().remove(deletePiece.getPosition(), deletePiece)) {
            LOG.log(Level.FINE, deletePiece + "It is not your turn!");
        }
    }

    /**
     * Make roque move by moving King and Rook and updating their positions
     *
     * @param rook      rook piece for roque move
     * @param rogueMove valid roque move which contains King object and all data around it
     */

    public void makeRogueMove(Rook rook, Move rogueMove) {

        // Move rook on ui board
        rogueMove.getPieceMoved().getImage().setX(rogueMove.getEnd().getX() * BoardDrawUtilsView.CELL_PIXEL_SIZE +
                FiguresDrawView.OFFSET_TO_CENTER);
        rogueMove.getPieceMoved().getImage().setY(rogueMove.getEnd().getY() * BoardDrawUtilsView.CELL_PIXEL_SIZE +
                FiguresDrawView.OFFSET_TO_CENTER);

        // Remove previous rook position on logic board
        board.getCellsToPieces().remove(rogueMove.getPieceMoved().getPosition());

        // Remove previous king position on logic board
        board.getCellsToPieces().remove(rogueMove.getPieceKilled().getPosition());

        // Set rook position
        rogueMove.getPieceMoved().setPosition(rogueMove.getEnd());

        // Set king position
        rogueMove.getPieceKilled().setPosition(rogueMove.getStart());

        // Update rook position on logic board
        board.getCellsToPieces().put(rogueMove.getEnd(), rogueMove.getPieceMoved());

        // Update king position on logic board
        board.getCellsToPieces().put(rogueMove.getStart(), rogueMove.getPieceKilled());
    }

    public void setPieceController(PieceController pieceController) {
        this.pieceController = pieceController;
    }

    public void addMoveToHistory(Move move) {
        board.getHistory().add(move);
    }

    /**
     * Check if king is in check. It checks all possible attack moves (Vertical, horizontal, around and Knight).
     *
     * @param king king object to check if it is in danger
     * @return true if it is check, false if it is not
     */
    public boolean isCheck(King king) {
        Cell kingPosition = king.getPosition();

        if (kingPosition == null) {
            return false;
        }

        // Check all possible attack positions in all direction including Knight moves

        if (    checkPossibleChecksForCheck(1, 0, kingPosition, king)   ||
                checkPossibleChecksForCheck(-1, 0, kingPosition, king)  ||
                checkPossibleChecksForCheck(-1, -1, kingPosition, king) ||
                checkPossibleChecksForCheck(+1, -1, kingPosition, king) ||
                checkPossibleChecksForCheck(1, 1, kingPosition, king)   ||
                checkPossibleChecksForCheck(-1, 1, kingPosition, king)    ) {
            return true;
        }

        for (int d = 1; d <= 7; d++) {
            Cell destination = Cell.fromIntToCell(kingPosition.getX(), kingPosition.getY() + d);

            if (checkCellForCheck(destination, false, king)) {
                return true;
            }

            if (board.getCellsToPieces().get(destination) != null) {
                break;
            }
        }

        for (int d = 1; d <= 7; d++) {
            Cell destination = Cell.fromIntToCell(kingPosition.getX(), kingPosition.getY() - d);

            if (checkCellForCheck(destination, false, king)) {
                return true;
            }

            if (board.getCellsToPieces().get(destination) != null) {
                break;
            }
        }

        // Check if Knight attacks King

        for (int i = 0; i < 8; i++) {
            Cell destination = Cell.fromIntToCell(kingPosition.getX() + Knight.movesCoordinates[i][0], kingPosition.getY()
                    + Knight.movesCoordinates[i][1]);
            if (destination != null && board.getCellsToPieces().get(destination) != null &&
                    board.getCellsToPieces().get(destination).isWhite() != king.isWhite() &&
                    board.getCellsToPieces().get(destination) instanceof Knight)
                return true;
        }
        return false;

    }

    /**
     * Check one cell if it contains enemies piece.
     *
     * @param cell     cell in chessboard for check
     * @param diagonal for bishop and rook. Bishop attack in diagonal and rook does not. For validation.
     * @param king     king who may be under attack
     * @return true if cell is dangerous, false if it is not
     */

    private boolean checkCellForCheck(Cell cell, boolean diagonal, King king) {

        if (board.getCellsToPieces().get(cell) == null) {
            return false;
        }

        if (board.getCellsToPieces().get(cell).isWhite() == king.isWhite()) {
            return false;
        }

        // Check if Queen currently attacks King
        if (board.getCellsToPieces().get(cell) instanceof Queen
                || (board.getCellsToPieces().get(cell) instanceof Pawn &&
                ((Pawn) board.getCellsToPieces().get(cell)).getPromotedTo() instanceof Queen)) {
            LOG.log(Level.WARNING, "King is under Queen attack!");
            return true;
        }

        // Check if Bishop currently attacks King
        if (diagonal && (board.getCellsToPieces().get(cell) instanceof Bishop ||
                (board.getCellsToPieces().get(cell) instanceof Pawn &&
                        ((Pawn) board.getCellsToPieces().get(cell)).getPromotedTo() instanceof Bishop))) {
            LOG.log(Level.WARNING, "King is under Bishop attack!");
            return true;
        }

        // Check if Rook currently attacks King
        if (!diagonal && (board.getCellsToPieces().get(cell) instanceof Rook ||
                (board.getCellsToPieces().get(cell) instanceof Pawn &&
                        ((Pawn) board.getCellsToPieces().get(cell)).getPromotedTo() instanceof Rook))) {

            LOG.log(Level.WARNING, "King is under Rook attack!");
            return true;
        }

        // Check if Pawn currently attacks King
        if (board.getCellsToPieces().get(cell) instanceof Pawn) {
            for (Move m : board.getCellsToPieces().get(cell).getLegalMoves(board)) {
                if (m.getStart() == cell && m.getPieceKilled() instanceof King) {
                    LOG.log(Level.WARNING, "King is under Pawn attack!");
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Predict if move will lead to possible check
     *
     * @param move move for checking
     * @return true if move is dangerous, false if it is safe
     */
    boolean isMoveLeadsToCheck(Move move) {
        King king = move.getPieceMoved().isWhite() ? board.getWhiteKing() : board.getBlackKing();
        Piece tmpPiece = null;
        // Make move to foresee check

        if (board.getCellsToPieces().get(move.getEnd()) != null) {
            tmpPiece = board.getCellsToPieces().get(move.getEnd());
        }

        // Remove piece we want to move to from logic board
        board.getCellsToPieces().remove(move.getPieceMoved().getPosition(), move.getPieceMoved());
        // Insert piece in desired position in logic board
        board.getCellsToPieces().put(move.getEnd(), move.getPieceMoved());

        // Change king pos for escape moves
        if (move.getPieceMoved() instanceof King) {
            king.setPosition(move.getEnd());
        }

        // Get result in case of that move
        boolean res = isCheck(king);

        // Return king pos
        if (move.getPieceMoved() instanceof King) {
            king.setPosition(move.getStart());
        }
        // Return piece back on his position on logic board
        board.getCellsToPieces().remove(move.getEnd(), move.getPieceMoved());
        board.getCellsToPieces().put(move.getPieceMoved().getPosition(), move.getPieceMoved());

        if (tmpPiece != null) {
            board.getCellsToPieces().put(move.getEnd(), tmpPiece);
        }

        return res;
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

    public Board getBoard() {
        return board;
    }


    private boolean checkPossibleChecksForCheck(int x_direction, int y_direction, Cell kingPosition, King king) {

        for (int d = 1; d <= 7; d++) {
            Cell destination = Cell.fromIntToCell(kingPosition.getX() + x_direction * d,
                    kingPosition.getY() + y_direction * d);

            if (checkCellForCheck(destination, false, king)) {
                return true;
            }

            if (board.getCellsToPieces().get(destination) != null) {
                break;
            }
        }

        return false;
    }


}

