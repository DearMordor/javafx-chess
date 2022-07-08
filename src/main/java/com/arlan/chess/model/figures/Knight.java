/**
 * @author nurkharl
 */

package com.arlan.chess.model.figures;

import com.arlan.chess.model.Board;
import com.arlan.chess.model.Move;

import java.util.ArrayList;
import java.util.List;

/**
 * Knight piece class
 */

public class Knight extends Piece{

    // Move coordinates {x,y} for Knight
    public static final int[][] movesCoordinates = new int[][]{ {-2, -1}, {2, -1},{-2, 1}, {2, 1}, {1,2},
                                                             {1,-2},{-1,-2}, {-1,2} };

    /**
     * Knight class constructor
     *
     * @param white - true if Knight is white, false if Knight is black
     * @param position - Cell position on chessboard. It is represented by enum class
     */

    public Knight(boolean white, Cell position) {
        super(white, position);
    }

    @Override
    public List<Move> getLegalMoves(Board board) {

        List<Move> legalMoves = new ArrayList<>();
        for(int i = 0; i < 8; i++) {

            Cell destination = Cell.fromIntToCell(getPosition().getX() + movesCoordinates[i][0],
                                                  getPosition().getY() + movesCoordinates[i][1]);
            checkMove(board, legalMoves, destination);

        }

        return legalMoves;
    }
}
