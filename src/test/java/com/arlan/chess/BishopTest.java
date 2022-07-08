package com.arlan.chess;

import com.arlan.chess.model.Board;
import com.arlan.chess.model.Move;
import com.arlan.chess.model.MoveType;
import com.arlan.chess.model.figures.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class BishopTest {
    private final Board board = new Board();
    private final Bishop bishop = new Bishop(true, Cell.A1);

    /**
     * If we set one bishop on empty chessboard at A1 Cell. Bishop will have 7 valid moves: B2, C3, ..., H8.
     */
    @Test
    public void testGetValidMoves() {

        initEmptyBoardWithOneBishop();
        int expectedSize = 7;

        List<Move> validMoves = bishop.getLegalMoves(board);

        assertEquals(expectedSize, validMoves.size());
    }

    /**
     * At the start of game bishop has not valid moves.
     */
    @Test
    public void testGetValidMovesAllPieceInStartClassicPositions() {
        board.setPieceOnClassicPositions();
        int expectedSize = 0;

        List<Move> validMoves = bishop.getLegalMoves(board);
        assertEquals(expectedSize, validMoves.size());
    }

    @Test
    public void testGetValidMovesWithEnemyOnTheRoad() {
        // Init our bishop
        board.getCellsToPieces().put(bishop.getPosition(), bishop);

        // Init enemy
        board.getCellsToPieces().put(Cell.C3, new Knight(false, Cell.C3));

        List<Move> validMoves = bishop.getLegalMoves(board);

        // Our bishop will have only two possible moves. From A1 to B2 as movement and from A1 to C3 as attack move
        int expectedSize = 2;
        assertEquals(expectedSize, validMoves.size());
    }

    @Test
    public void testGetValidAttackMove() {
        // Init our bishop
        board.getCellsToPieces().put(bishop.getPosition(), bishop);

        // Init enemy
        Knight knight = new Knight(false, Cell.C3);
        board.getCellsToPieces().put(Cell.C3, knight);

        MoveType expectedMoveType = MoveType.ATTACK;
        List<Move> validMoves = bishop.getLegalMoves(board);

        Cell expectedStart = Cell.A1;
        Cell expectedEnd = Cell.C3;
        Piece expectedPieceMoved = bishop;


        Move possibleMove = null;

        for (Move move:
             validMoves) {
            if(move.getMoveType() == MoveType.ATTACK) {
                possibleMove = move;
                break;
            }
        }

        assert possibleMove != null;
        assertEquals(expectedStart, possibleMove.getStart());
        assertEquals(expectedEnd, possibleMove.getEnd());
        assertEquals(expectedPieceMoved, possibleMove.getPieceMoved());
        assertEquals(knight, possibleMove.getPieceKilled());
        assertEquals(expectedMoveType, possibleMove.getMoveType());
    }

    /**
     * By chess rules we cannot eat King
     */
    @Test
    public void testEatKing() {
        // Init our bishop
        board.getCellsToPieces().put(bishop.getPosition(), bishop);

        // Init enemy
        King king = new King(false, Cell.C3);
        board.getCellsToPieces().put(Cell.C3, king);

        Move move = new Move(bishop.getPosition(), king.getPosition(), king, bishop, MoveType.ATTACK);
        boolean isValidMove = bishop.checkMove(board, bishop.getLegalMoves(board), king.getPosition());

        assertFalse(isValidMove);
    }

    public void initEmptyBoardWithOneBishop() {
        board.getCellsToPieces().put(bishop.getPosition(), bishop);
    }
}
