package com.arlan.chess.controller;

import com.arlan.chess.model.Board;
import com.arlan.chess.model.Move;
import com.arlan.chess.model.MoveType;
import com.arlan.chess.model.figures.*;
import com.arlan.chess.view.BoardDrawUtilsView;
import com.arlan.chess.view.FiguresDrawView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The BotController class is responsible for AI. It can search available moves and validate them.
 */
public class BotController {

    private boolean isWhite;
    private BoardController boardController;
    private GameController gameController;
    private FiguresDrawView figuresDrawView;
    private PieceController pieceController;

    /**
     * BotController constructor
     *
     * @param isWhite true if bot is White player, false if he is Black Player
     */
    public BotController(boolean isWhite) {
        this.isWhite = isWhite;
    }

    public void setBoardController(BoardController boardController) {
        this.boardController = boardController;
    }

    /**
     * Returns random move if there is some. If there is no return null
     *
     * @param board logic board for operating with current chessboard data
     * @return legal available move
     */

    public Move findMove(Board board) {

        Piece piece;
        List<Move> possibleMoves = new ArrayList<>();

        // Choose random piece
        for (Cell cell : Cell.values()) {
            piece = board.getCellsToPieces().get(cell);

            if (piece == null) {
                continue;
            }

            if (piece.isWhite() == this.isWhite) {
                for (Move move : piece.getLegalMoves(board)) {
                    if (isValidMove(move)) {
                        possibleMoves.add(move);
                    }
                }
            }
        }

        if (possibleMoves.size() > 0) {
            Random r = new Random();
            return possibleMoves.get(r.nextInt(possibleMoves.size()));
        } else {
            return null;
        }

    }

    /**
     * Validate given move.
     *
     * @param move random move
     * @return true if move passes all checks, false if it does not
     */
    private boolean isValidMove(Move move) {
        if (move == null) {
            return false;
        }

        if (move.getPieceMoved().isWhite() == boardController.getBoard().getLastMove().getPieceMoved().isWhite()) {
            return false;
        }

        if (move.getMoveType() != MoveType.ROGUE && move.getPieceKilled() instanceof King) {
            return false;
        }

        if (boardController.isMoveLeadsToCheck(move)) {
            return false;
        }

        return true;
    }

    /**
     * Make move on chessboard (both UI and logic)
     *
     * @param move given valid move
     */
    public void makeMove(Move move) {


        if (move == null) {
            return;
        }

        if (move.getPieceKilled() != null && move.getPieceKilled().isWhite() == move.getPieceMoved().isWhite()) {
            return;
        }

        Board board = boardController.getBoard();

        if (move.getMoveType() == MoveType.ATTACK || move.getMoveType() == MoveType.ENPASSANT) {

            // delete figure from logic board
            boardController.deletePieceOnLogicBoard(move.getPieceKilled());

            // delete figure from board UI
            figuresDrawView.deleteFigureFromUiBoard(move.getPieceKilled());

            boardController.addMoveToHistory(move);

        } else if (move.getMoveType() == MoveType.PROMOTION) {
            // Delete figure from logic board and UI board
            if (move.getPieceKilled() != null) {
                figuresDrawView.deleteFigureFromUiBoard(move.getPieceKilled());
                boardController.deletePieceOnLogicBoard(move.getPieceKilled());
            }
            String[] piecesForPromotion = {"Knight", "Bishop", "Queen", "Rook"};

            Random r = new Random();
            int randomIndex = r.nextInt(piecesForPromotion.length);

            Cell promotePosition =
                    move.getPieceKilled() != null ? move.getPieceKilled().getPosition() :
                            Cell.fromIntToCell(move.getPieceMoved().getPosition().getX(),
                                    move.getPieceMoved().getPosition().getY() - 1);

            String randomChoice = piecesForPromotion[randomIndex];

            switch (randomChoice) {
                case "Knight":
                    boardController.promotePiece(new Knight(move.getPieceMoved().isWhite(),
                            promotePosition), move.getPieceMoved());
                    break;
                case "Rook":
                    boardController.promotePiece(new Rook(move.getPieceMoved().isWhite(),
                            promotePosition), move.getPieceMoved());
                    break;
                case "Queen":
                    boardController.promotePiece(new Queen(move.getPieceMoved().isWhite(),
                            promotePosition), move.getPieceMoved());
                    break;
                case "Bishop":
                    boardController.promotePiece(new Bishop(move.getPieceMoved().isWhite(),
                            promotePosition), move.getPieceMoved());
                    break;
            }

            boardController.addMoveToHistory(move);

        } else if (move.getMoveType() == MoveType.MOVEMENT) {
            boardController.addMoveToHistory(move);
        } else if (move.getMoveType() == MoveType.ROGUE) {
            boardController.makeRogueMove((Rook) board.getCellsToPieces().get(move.getPieceMoved().getPosition()), move);
            boardController.addMoveToHistory(move);
        }

        if (!(move.getMoveType() == MoveType.ROGUE) &&
                !(move.getMoveType() == MoveType.PROMOTION)) {
            boardController.updatePiecePositionInLogicBoard(move.getEnd().getX(),
                    move.getEnd().getY(),
                    move.getPieceMoved());
        }

        pieceController.setRectPosition(move.getPieceMoved().getImage(), move.getEnd().getX() *
                        BoardDrawUtilsView.CELL_PIXEL_SIZE + FiguresDrawView.OFFSET_TO_CENTER,
                move.getEnd().getY() *
                        BoardDrawUtilsView.CELL_PIXEL_SIZE + FiguresDrawView.OFFSET_TO_CENTER);

        // Evident first Pawn move
        if (move.getPieceMoved() instanceof Pawn && ((Pawn) move.getPieceMoved()).isFirstMove()) {
            ((Pawn) move.getPieceMoved()).setFirstMove(false);
        }

        // Evident if king was moved for Rogue condition
        if (move.getPieceMoved() instanceof King && ((King) move.getPieceMoved()).isFirstMove()) {
//                                System.out.println("King was moved! Castling cannot be performed!");
            ((King) move.getPieceMoved()).setFirstMove(false);
        }

        // Evident if Rook was moved for Rogue condition
        if (move.getPieceMoved() instanceof Rook && ((Rook) move.getPieceMoved()).isFirstMove()) {
            ((Rook) move.getPieceMoved()).setFirstMove(false);
        }


    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public void setPieceController(PieceController pieceController) {
        this.pieceController = pieceController;
    }

    public void setFiguresDrawView(FiguresDrawView figuresDrawView) {
        this.figuresDrawView = figuresDrawView;
    }
}
