/**
 * @author nurkharl
 */


package com.arlan.chess.model;

import com.arlan.chess.model.figures.Cell;
import com.arlan.chess.model.figures.Piece;

/**
 * Move class provides information about type of move, moved piece, killed piece and start, end cords.
 */

public class Move {
    private Cell start;
    private Cell end;

    public MoveType moveType;
    private Piece pieceKilled;
    private Piece pieceMoved;

    /**
     * Move class constructor
     *
     * @param start - Chessboard cell where move starts
     * @param end - Chessboard cell where move ends
     * @param pieceKilled - Killed piece at the end of move destination. If there is an enemy piece.
     * @param pieceMoved - Piece that player moves
     */

    public Move(Cell start, Cell end, Piece pieceKilled, Piece pieceMoved, MoveType moveType) {
        setStart(start);
        setEnd(end);
        setPieceKilled(pieceKilled);
        setPieceMoved(pieceMoved);
        setMoveType(moveType);
    }

    public MoveType getMoveType() {
        return moveType;
    }

    public void setMoveType(MoveType moveType) {
        this.moveType = moveType;
    }

    public Cell getStart() {
        return start;
    }

    public void setStart(Cell start) {
        this.start = start;
    }

    public Cell getEnd() {
        return end;
    }

    public void setEnd(Cell end) {
        this.end = end;
    }

    public Piece getPieceKilled() {
        return pieceKilled;
    }

    public void setPieceKilled(Piece pieceKilled) {
        this.pieceKilled = pieceKilled;
    }

    public Piece getPieceMoved() {
        return pieceMoved;
    }

    public void setPieceMoved(Piece pieceMoved) {
        this.pieceMoved = pieceMoved;
    }
}
