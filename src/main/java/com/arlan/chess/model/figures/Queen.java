/**
 * @author nurkharl
 */

package com.arlan.chess.model.figures;

import com.arlan.chess.model.Board;
import com.arlan.chess.model.Move;

import java.util.ArrayList;
import java.util.List;

/**
 * Queen piece class
 */

public class Queen extends Piece{

    /** Queen class constructor
     *  @param white boolean true if Queen is white or false if it is black
     *  @param position Cell position on chessboard. It is represented by enum class
     */
    public Queen(boolean white, Cell position) {
        super(white, position);
    }

    @Override
    public List<Move> getLegalMoves(Board board) {
        List<Move> legalMoves = new ArrayList<>();

        // Get legal moves DIAGONAL LEFT UP way
        for(int d = 1; d <= 7; d++) {

            Cell destination = Cell.fromIntToCell(getPosition().getX() - d, getPosition().getY() - d);

            if (!checkMove(board, legalMoves, destination)) {
                break;
            }

        }

        // Get legal moves DIAGONAL LEFT DOWN way
        for(int d = 1; d <= 7; d++) {

            Cell destination = Cell.fromIntToCell(getPosition().getX() + d, getPosition().getY() + d);

            if (!checkMove(board, legalMoves, destination)) {
                break;
            }
        }

        // Get legal moves UP VERTICALLY way
        for(int d = 1; d <= 7; d++) {

            Cell destination = Cell.fromIntToCell(getPosition().getX(), getPosition().getY() + d);

            if (!checkMove(board, legalMoves, destination)) {
                break;
            }
        }

        // Get legal moves DOWN VERTICALLY way
        for(int d = 1; d <= 7; d++) {

            Cell destination = Cell.fromIntToCell(getPosition().getX(), getPosition().getY() - d);

            if (!checkMove(board, legalMoves, destination)) {
                break;
            }
        }

        // Get legal moves RIGHT HORIZONTALLY way
        for(int d = 1; d <= 7; d++) {

            Cell destination = Cell.fromIntToCell(getPosition().getX() + d , getPosition().getY());

            if (!checkMove(board, legalMoves, destination)) {
                break;
            }
        }

        // Get legal moves LEFT HORIZONTALLY way
        for(int d = 1; d <= 7; d++) {

            Cell destination = Cell.fromIntToCell(getPosition().getX() - d , getPosition().getY());

            if (!checkMove(board, legalMoves, destination)) {
                break;
            }
        }

        // Get legal moves LEFT DIAGONALLY DOWN way
        for(int d = 1; d <= 7; d++) {

            Cell destination = Cell.fromIntToCell(getPosition().getX() - d , getPosition().getY() + d);

            if (!checkMove(board, legalMoves, destination)) {
                break;
            }
        }

        // Get legal moves RIGHT DIAGONALLY UP way
        for(int d = 1; d <= 7; d++) {

            Cell destination = Cell.fromIntToCell(getPosition().getX() + d , getPosition().getY() - d);

            if (!checkMove(board, legalMoves, destination)) {
                break;
            }
        }

            return legalMoves;
    }
}
