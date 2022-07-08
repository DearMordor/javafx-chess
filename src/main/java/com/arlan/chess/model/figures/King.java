/**
 * @author nurkharl
 */


package com.arlan.chess.model.figures;

import com.arlan.chess.model.Board;
import com.arlan.chess.model.Move;

import java.util.ArrayList;
import java.util.List;

/**
 * King piece class
 */


public class King extends Piece {

    private static final int[][] kingMovesCoordinate = new int[][]{
            {1, 0} , {1,-1}, {0, 1}, {0, -1}, {-1, 0}, {-1,-1}, {-1,1}, {1,1}
    };

    private boolean isCastlingDone = false;

    /**
     * King class constructor
     *
     * @param white - true if King is white, false if King is black
     * @param position - Cell position on chessboard. It is represented by enum class
     */

    public King(boolean white, Cell position){
        super(white, position);
    }

    public boolean isFirstMove = true;

    @Override
    public List<Move> getLegalMoves(Board board) {
        List<Move> legalMoves = new ArrayList<>();
        // Get all cells around king as possible move
        for (int i = 0; i < 8; i++) {
            Cell destination = Cell.fromIntToCell(getPosition().getX() + kingMovesCoordinate[i][0],
                                                  getPosition().getY() + kingMovesCoordinate[i][1]);
            checkMove(board, legalMoves, destination);
        }

        if (isFirstMove) {
            for (Piece piece : board.getFigures()) {
                if (piece.isWhite() == this.isWhite() && piece instanceof Rook && ((Rook) piece).isFirstMove()) {
                    Move move = getValidCastlingMove(board, this, (Rook) piece);

                    if (move != null) {
                        legalMoves.add(move);
                    }
                }
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
