/**
 * @author nurkharl
 */


package com.arlan.chess.model;

import com.arlan.chess.model.figures.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Board class is responsible for chessboard backend mechanics. Invisible for user.
 */

public class Board {

    // Every Cell on board contains either null or piece
    private HashMap<Cell, Piece> cellsToPieces = new HashMap<>();
    private final List<Move> history = new ArrayList<>();
    // Contains active figures
    private final List<Piece> figures = new ArrayList<>();
    private King whiteKing;
    private King blackKing;

    /**
     * Get Piece if there is or null if it's empty.
     */

    public Piece getPiece(Cell cell) {

        return cellsToPieces.get(cell);
    }


    /**
     * Set the chess pieces on the classic position in HashMap.
     *
     */

    public void setPieceOnClassicPositions() {

        // Init black pieces
        Piece piece = new Rook(false, Cell.A8);
        figures.add(piece);
        cellsToPieces.put(Cell.A8,piece);

        piece = new Knight(false, Cell.B8);
        figures.add(piece);
        cellsToPieces.put(Cell.B8, piece);

        piece = new Bishop(false, Cell.C8);
        figures.add(piece);
        cellsToPieces.put(Cell.C8, piece);

        piece = new Queen(false, Cell.D8);
        figures.add(piece);
        cellsToPieces.put(Cell.D8, piece);

        King blackKing = new King(false, Cell.E8);
        figures.add(blackKing);
        this.blackKing = blackKing;
        cellsToPieces.put(Cell.E8, blackKing);

        piece =  new Bishop(false, Cell.F8);
        figures.add(piece);
        cellsToPieces.put(Cell.F8, piece);

        piece = new Knight(false, Cell.G8);
        figures.add(piece);
        cellsToPieces.put(Cell.G8, piece);

        piece = new Rook(false, Cell.H8);
        figures.add(piece);
        cellsToPieces.put(Cell.H8, piece);

        piece = new Pawn(false, Cell.A7);
        figures.add(piece);
        cellsToPieces.put(Cell.A7, piece);

        piece = new Pawn(false, Cell.B7);
        figures.add(piece);
        cellsToPieces.put(Cell.B7, piece);

        piece = new Pawn(false, Cell.C7);
        figures.add(piece);
        cellsToPieces.put(Cell.C7, piece);

        piece = new Pawn(false, Cell.D7);
        figures.add(piece);
        cellsToPieces.put(Cell.D7, piece);

        piece = new Pawn(false, Cell.E7);
        figures.add(piece);
        cellsToPieces.put(Cell.E7, piece);

        piece = new Pawn(false, Cell.F7);
        figures.add(piece);
        cellsToPieces.put(Cell.F7, piece);

        piece = new Pawn(false, Cell.G7);
        figures.add(piece);
        cellsToPieces.put(Cell.G7, piece);

        piece = new Pawn(false, Cell.H7);
        figures.add(piece);
        cellsToPieces.put(Cell.H7, piece);

        // Init white pieces
        piece = new Rook(true, Cell.A1);
        figures.add(piece);
        cellsToPieces.put(Cell.A1, piece);

        piece = new Knight(true, Cell.B1);
        figures.add(piece);
        cellsToPieces.put(Cell.B1, piece);

        piece = new Bishop(true, Cell.C1);
        figures.add(piece);
        cellsToPieces.put(Cell.C1, piece);

        piece = new Queen(true, Cell.D1);
        figures.add(piece);
        cellsToPieces.put(Cell.D1, piece);

        King whiteKing = new King(true, Cell.E1);
        figures.add(whiteKing);
        this.whiteKing = whiteKing;
        cellsToPieces.put(Cell.E1, whiteKing);

        piece = new Bishop(true, Cell.F1);
        figures.add(piece);
        cellsToPieces.put(Cell.F1, piece);

        piece = new Knight(true, Cell.G1);
        figures.add(piece);
        cellsToPieces.put(Cell.G1, piece);

        piece = new Rook(true, Cell.H1);
        figures.add(piece);
        cellsToPieces.put(Cell.H1, piece);

        piece = new Pawn(true, Cell.A2);
        figures.add(piece);
        cellsToPieces.put(Cell.A2, piece);

        piece =  new Pawn(true, Cell.B2);
        figures.add(piece);
        cellsToPieces.put(Cell.B2,piece);

        piece = new Pawn(true, Cell.C2);
        figures.add(piece);
        cellsToPieces.put(Cell.C2, piece);

        piece = new Pawn(true, Cell.D2);
        figures.add(piece);
        cellsToPieces.put(Cell.D2, piece);

        piece = new Pawn(true, Cell.E2);
        figures.add(piece);
        cellsToPieces.put(Cell.E2, piece);

        piece = new Pawn(true, Cell.F2);
        figures.add(piece);
        cellsToPieces.put(Cell.F2, piece);

        piece = new Pawn(true, Cell.G2);
        figures.add(piece);
        cellsToPieces.put(Cell.G2, piece);

        piece = new Pawn(true, Cell.H2);
        figures.add(piece);
        cellsToPieces.put(Cell.H2, piece);

    }

    public List<Move> getHistory() {
        return history;
    }

    public List<Piece> getFigures() {
        return figures;
    }

    public Move getLastMove() {
        if (history.size() > 0) {
            return history.get(history.size() - 1);
        } else {
            return null;
        }
    }

    public HashMap<Cell, Piece> getCellsToPieces() {
        return cellsToPieces;
    }

    public King getWhiteKing() {
        return whiteKing;
    }

    public King getBlackKing() {
        return blackKing;
    }

    public void setCellsToPieces(HashMap<Cell, Piece> cellsToPieces) {
        this.cellsToPieces = cellsToPieces;
    }

    /**
     * Set white and black king from loaded hashmap from previous game
     */
    public void setWhiteAndBlackKingFromHashMap() {
        for (Piece piece : getCellsToPieces().values()) {
            if (piece instanceof King) {
                if (piece.isWhite()) {
                    whiteKing = (King) piece;
                } else  {
                    blackKing = (King) piece;
                }
            }
        }
    }
}
