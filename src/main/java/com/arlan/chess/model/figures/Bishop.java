/**
 * @author nurkharl
 */


package com.arlan.chess.model.figures;

import com.arlan.chess.model.Board;
import com.arlan.chess.model.Move;

import java.util.ArrayList;
import java.util.List;

/**
 * Bishop piece class represents a Bishop piece on board
 */

public class Bishop extends Piece{

    /**
     * Bishop class constructor
     *
     * @param white - true if Bishop is white, false if Bishop is black
     * @param position - Cell position on chessboard. It is represented by enum class
     */

    public Bishop(boolean white, Cell position) {
        super(white, position);
    }

    @Override
    public List<Move> getLegalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        // Calculate legal moves on right-way diagonal down
        for (int d = 1; d <= 7; d++) {
            Cell destination = Cell.fromIntToCell(getPosition().getX() + d, getPosition().getY() + d);

            if(!checkMove(board, legalMoves, destination)) {
                break;
            }

        }

        // Calculate legal moves on left-way diagonal up
        for (int d = 1; d <= 7; d++) {
            Cell destination = Cell.fromIntToCell(getPosition().getX() - d, getPosition().getY() - d);

            if(!checkMove(board, legalMoves, destination)) {
                break;
            }

        }

        // Calculate legal moves on right-way diagonal up
        for (int d = 1; d <= 7; d++) {
            Cell destination = Cell.fromIntToCell(getPosition().getX() + d, getPosition().getY() - d);

            if(!checkMove(board, legalMoves, destination)) {
                break;
            }

        }


        // Calculate legal moves on left-way diagonal down
        for (int d = 1; d <= 7; d ++) {
            Cell destination = Cell.fromIntToCell(getPosition().getX() - d, getPosition().getY() + d);

            if(!checkMove(board, legalMoves, destination)) {
                break;
            }
        }


        return legalMoves;
    }
}
