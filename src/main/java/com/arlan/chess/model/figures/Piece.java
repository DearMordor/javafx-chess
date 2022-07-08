/**
 * @author nurkharl
 */

package com.arlan.chess.model.figures;

import com.arlan.chess.model.Board;
import com.arlan.chess.model.Move;
import com.arlan.chess.model.MoveType;
import javafx.scene.shape.Rectangle;

import java.util.List;

/**
 * Piece abstract class represents information about single piece on board.
 */


public abstract class Piece {

    private boolean white = false;
    private Cell getPosition;
    public Rectangle image;

    /** Piece class constructor
     * @param white boolean true if piece is white or false if it is black
     * @param position Cell position on chessboard. It is represented by enum class
     */

    public Piece(boolean white, Cell position){
        this.setWhite(white);
        this.getPosition = position;
    }

    public void setPosition(Cell position) {
        this.getPosition = position;
    }

    public boolean isWhite() {
        return white;
    }

    public void setWhite(boolean white) {
        this.white = white;
    }

    public Cell getPosition() {
        return this.getPosition;
    }

    /**
     * Get piece image
    */
    public Rectangle getImage() {
        return image;
    }

    public void setImage(Rectangle image) {
        this.image = image;
    }

    /**
     * Get piece image name to get piece image in resources folder
     */
    public String getPieceImageName() {

        String color = this.white ? "white" : "black";

        return String.format(
                "%s-"+color+".png", getClass().getSimpleName()
        );
    }

    /**
     * Return true if move is valid, else black
     *
     * Firstly it checks if destination is null. If it is, then move is not valid.
     * Secondly it checks if there is no king on destination. Because we cannot kill King by chess rules
     * Thirdly it checks if destination is occupied in logic board, if destination is not the same color
     *
     * @param board logic board where all data about current chess board
     * @param legalMoves list of legal moves calculated for piece where checkMove method invoked
     * @param destination destination on chessboard
     * @return true if valid, else black
     */

    public boolean checkMove(Board board, List<Move> legalMoves, Cell destination) {

        // Prevent getting Cells with coordinates beyond the board
        if (destination == null) {
            return false;
        }

        // Prevent from killing the King
        if (board.getCellsToPieces().get(destination) instanceof King) {
            return false;
        }

        // If there is no piece in HashMap then destination cell on chessboard is not occupied
        if (board.getCellsToPieces().get(destination) == null ) {
            Move move = new Move(getPosition, destination, null, this, MoveType.MOVEMENT);
            legalMoves.add(move);
        } else if (board.getCellsToPieces().get(destination).white == this.white) {
            // Prevent from moving and killing on destination where is the same piece color
            return false;
        } else {
            Move move = new Move(getPosition, destination, board.getCellsToPieces().get(destination),
                    this, MoveType.ATTACK);
            legalMoves.add(move);
            return false;
        }

        return true;

    }

    /**
     * Calculates and returns valid castling move for King.
     *
     * @param board logic board where is data about current chessboard
     * @param king king object for castling move
     * @param rook rook object for castling move
     *
     * @return Valid move for castling
     */

    public Move getValidCastlingMove(Board board, King king, Rook rook) {

        if (rook.getPosition() == null || king.getPosition() == null ) {
            return null;
        }

        int rookY = rook.getPosition().getY();
        int dir = 1;
        int start, end;

        // Check if king and rook are on the same row. Return null if they are not
        if(king.getPosition().getY() - rook.getPosition().getY() != 0) {
            return null;
        }

        // Decide if it is long castling move, or short by king and rook X position on chessboard
        if (king.getPosition().getX() > rook.getPosition().getX()) {
            dir = -1;
            start = rook.getPosition().getX();
            end = king.getPosition().getX();
        } else  {
            start = king.getPosition().getX();
            end = rook.getPosition().getX();
        }

        // Check if there is at least one piece between rook and king. Return null if there is
        for (int px = start + 1; px < end; px++) {
            Cell destination = Cell.fromIntToCell(px,rookY);
            Piece piece = board.getCellsToPieces().get(destination);

            if (piece != null) {
                return null;
            }
        }

        Move move = new Move(Cell.fromIntToCell(king.getPosition().getX() + 2*dir, rookY),
                Cell.fromIntToCell(king.getPosition().getX() + dir, rookY), king, rook, MoveType.ROGUE);

        return move;
    }


    /**
     * Returns List which contains all legal moves (Move class) for piece
     * @param board - logic board where located all data information about current chessboard
     * @return list of legal moves
     */

    public abstract List<Move> getLegalMoves(Board board);
}
