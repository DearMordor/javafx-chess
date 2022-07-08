/**
 * @author nurkharl
 */

package com.arlan.chess.model.figures;

import com.arlan.chess.model.Board;
import com.arlan.chess.model.Move;

import java.util.ArrayList;
import java.util.List;

/**
 * Rook piece class
 */

public class Rook extends Piece{

    // isFirstMove variable in responsible for roque move
    private boolean isFirstMove = true;

    /** Rook class constructor
    *  @param white boolean true if rook is white or false if it is black
     * @param position Cell position on chessboard. It is represented by enum class
    */
    public Rook(boolean white, Cell position) {
        super(white, position);
    }

    @Override
    public List<Move> getLegalMoves(Board board) {

        List<Move> legalMoves = new ArrayList<>();

        // Get valid moves in VERTICAL DOWN way
        for (int d = 1; d <= 7; d++) {

            Cell destination = Cell.fromIntToCell(getPosition().getX(), getPosition().getY() + d);

            if (!checkMove(board, legalMoves, destination)) {
                break;
            }

        }

        // Get valid moves in VERTICAL UP way
        for (int d = 1; d <= 7; d++) {

            Cell c = Cell.fromIntToCell(getPosition().getX(), getPosition().getY() - d);

            if (!checkMove(board, legalMoves, c)) {
                break;
            }

        }

        // Get valid moves in HORIZON LEFT way
        for (int d = 1; d <= 7; d++) {
            Cell c = Cell.fromIntToCell(getPosition().getX() - d, getPosition().getY());

            if (!checkMove(board, legalMoves, c)) {
                break;
            }

        }

        // Get valid moves in HORIZON RIGHT way
        for (int d = 1; d <= 7; d++) {

            Cell c = Cell.fromIntToCell(getPosition().getX() + d, getPosition().getY());

            if (!checkMove(board, legalMoves, c)) {
                break;
            }
        }
        return legalMoves;
    }

    public boolean isFirstMove() {
        return isFirstMove;
    }

    public void setFirstMove(boolean firstMove) {
        isFirstMove = firstMove;
    }
}
