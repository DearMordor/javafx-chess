/**
 * @author nurkharl
 */

package com.arlan.chess.model.figures;

import com.arlan.chess.model.Board;
import com.arlan.chess.model.Move;
import com.arlan.chess.model.MoveType;

import java.util.ArrayList;
import java.util.List;

/**
 * Pawn piece class
 */

public class Pawn extends Piece {

    // Moves coordinates {x,y}. By default, for black pawn. For white pawn it will be multiplied by -1
    private final static int[][] movesCoordinates = {{-1,1},{1,1}, {0,1}};


    private boolean promotion = false;
    private boolean isFirstMove = true;
    private Piece promotedTo = null;

    /**
     *  Pawn class constructor
     *
     * @param white - true if pawn is white, false if pawn is black
     * @param position Cell position on chessboard. It is represented by enum class
     */

    public Pawn(boolean white, Cell position) {
        super(white, position);
    }

    @Override
    public List<Move> getLegalMoves(Board board) {
        List<Move> legalMoves = new ArrayList<>();

        // Decide moves direction by pawn's color.
        int direction = isWhite() ? -1 : 1;

        // Make move forward if conditions are OK
        Cell destination = Cell.fromIntToCell(getPosition().getX(), getPosition().getY() + direction);


        if (destination != null && board.getCellsToPieces().get(destination) == null) {
            MoveType moveType = null;

            // Distinguish Promotion and Movement type of move. It's PROMOTION when pawn gets to the enemy's first row,
            // else it is movement
            if( (isWhite()) && (getPosition().getY() + direction * movesCoordinates[2][1]) == 0 ||
                (!isWhite()) && (getPosition().getY() + direction * movesCoordinates[2][1]) == 7)  {
                moveType = MoveType.PROMOTION;
            } else {

                moveType = MoveType.MOVEMENT;

            }
            Move move = new Move(getPosition(), destination, null, this, moveType);
            legalMoves.add(move);
        }

        // Add double move for the first use only
        destination = Cell.fromIntToCell(getPosition().getX(), getPosition().getY() +
                (2 * direction * movesCoordinates[2][1]) );

        if (isFirstMove && destination != null && board.getCellsToPieces().get(destination) == null) {
            Move move = new Move(getPosition(), destination, null, this, MoveType.MOVEMENT);
            legalMoves.add(move);
        }

        // Add RIGHT Attack move for white pawn and left black pawn
        destination = Cell.fromIntToCell(getPosition().getX() + direction * movesCoordinates[0][0],
                                         getPosition().getY() + direction * movesCoordinates[0][1]);
        if (destination != null) {
            if (    board.getCellsToPieces().get(destination) != null //&&
//                    board.getCellsToPieces().get(destination).isWhite() != isWhite()) {
            ) {
                // If we attack piece on edge it can be promotion move! For example from D7 to H8
                MoveType moveType;

                if (isWhite() && (getPosition().getY() + direction * movesCoordinates[2][1]) == 0 ||
                    !isWhite() && (getPosition().getY() + direction * movesCoordinates[2][1]) == 7)  {
                    moveType = MoveType.PROMOTION;
                } else  {
                    moveType = MoveType.ATTACK;
                }

                Move move = new Move(getPosition(), destination,
                        board.getCellsToPieces().get(destination), this, moveType);
                legalMoves.add(move);
            } else  {
                Move lastMove = board.getLastMove();

                if (lastMove != null && lastMove.getPieceMoved() != null) {
                    if (lastMove.getPieceMoved() instanceof Pawn  &&
                            lastMove.getStart() == Cell.fromIntToCell(destination.getX(),
                                    destination.getY() + direction) &&
                            lastMove.getEnd() == Cell.fromIntToCell(destination.getX(),
                                    destination.getY() - direction)) {
                        Move move = new Move(getPosition(), destination, lastMove.getPieceMoved(),
                                this, MoveType.ENPASSANT);
                        legalMoves.add(move);
                    }
                }

            }
        }


        // Add RIGHT Attack move for Black pawn or Left attack for white pawn
        destination = Cell.fromIntToCell(getPosition().getX() + direction * movesCoordinates[1][0],
                getPosition().getY() + direction * movesCoordinates[1][1]);
        if (destination != null) {
            if (    board.getCellsToPieces().get(destination) != null //&&
//                    board.getCellsToPieces().get(destination).isWhite() != isWhite())  {
            ) {
                // If we attack piece on edge it can be promotion move! For example from D7 to H8
                MoveType moveType;

                if (isWhite() && (getPosition().getY() + direction * movesCoordinates[2][1]) == 0 ||
                        !isWhite() && (getPosition().getY() + direction * movesCoordinates[2][1]) == 7)  {
                    moveType = MoveType.PROMOTION;
                } else  {
                    moveType = MoveType.ATTACK;
                }

                Move move = new Move(getPosition(), destination,
                        board.getCellsToPieces().get(destination), this, moveType);
                legalMoves.add(move);
            } else  {
                Move lastMove = board.getLastMove();

                if (lastMove != null && lastMove.getPieceMoved() != null) {
                    if (lastMove.getPieceMoved() instanceof Pawn  &&
                            lastMove.getStart() == Cell.fromIntToCell(destination.getX(),
                                    destination.getY() + direction) &&
                            lastMove.getEnd() == Cell.fromIntToCell(destination.getX(),
                                    destination.getY() - direction)) {
                        Move move = new Move(getPosition(), destination, lastMove.getPieceMoved(),
                                this, MoveType.ENPASSANT);
                        legalMoves.add(move);
                    }
                }
            }
        }

        return legalMoves;
    }

    public void setFirstMove(boolean firstMove) {
        isFirstMove = firstMove;
    }

    public boolean isFirstMove() {
        return isFirstMove;
    }

    public boolean isPromotion() {
        return promotion;
    }

    public void setPromotion(boolean promotion) {
        this.promotion = promotion;
    }

    public Piece getPromotedTo() {
        return promotedTo;
    }

    public void setPromotedTo(Piece promotedTo) {
        this.promotedTo = promotedTo;
    }

}
