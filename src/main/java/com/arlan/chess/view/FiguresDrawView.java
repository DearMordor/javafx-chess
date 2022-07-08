package com.arlan.chess.view;

import com.arlan.chess.controller.PieceController;
import com.arlan.chess.model.figures.*;
import com.arlan.chess.model.Board;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * The FiguresDrawView is responsible for drawing and erasing figures on UI
 */

public class FiguresDrawView {

    PieceController pieceController;
    Board board;
    public static final int OFFSET_TO_CENTER = 5;

    /**
     * FiguresDrawView constructor
     * @param pieceController object of pieceController for using
     */
    public FiguresDrawView(PieceController pieceController) {
        this.pieceController = pieceController;
    }

    /**
     * Draw black team in classic position
     * @param board JavaFx container where to put pieces
     * @param cellsOnBoard logic board which contains all data about current chessboard
     */

    void drawBlackTeam(Group board, HashMap<Cell, Piece> cellsOnBoard) {

        drawFigure(cellsOnBoard.get(Cell.A8), board);
        drawFigure(cellsOnBoard.get(Cell.B8), board);
        drawFigure(cellsOnBoard.get(Cell.C8), board);
        drawFigure(cellsOnBoard.get(Cell.D8), board);
        drawFigure(cellsOnBoard.get(Cell.E8), board);
        drawFigure(cellsOnBoard.get(Cell.F8), board);
        drawFigure(cellsOnBoard.get(Cell.G8), board);
        drawFigure(cellsOnBoard.get(Cell.H8), board);
        drawFigure(cellsOnBoard.get(Cell.A7), board);
        drawFigure(cellsOnBoard.get(Cell.B7), board);
        drawFigure(cellsOnBoard.get(Cell.C7), board);
        drawFigure(cellsOnBoard.get(Cell.D7), board);
        drawFigure(cellsOnBoard.get(Cell.E7), board);
        drawFigure(cellsOnBoard.get(Cell.F7), board);
        drawFigure(cellsOnBoard.get(Cell.G7), board);
        drawFigure(cellsOnBoard.get(Cell.H7), board);


    }

    /**
     * Draw white team in classic position
     * @param board JavaFx container where to put pieces
     * @param cellsOnBoard logic board which contains all data about current chessboard
     */

    void drawWhiteTeam(Group board, HashMap<Cell, Piece> cellsOnBoard) {

        drawFigure(cellsOnBoard.get(Cell.A1), board);
        drawFigure(cellsOnBoard.get(Cell.B1), board);
        drawFigure(cellsOnBoard.get(Cell.C1), board);
        drawFigure(cellsOnBoard.get(Cell.D1), board);
        drawFigure(cellsOnBoard.get(Cell.E1), board);
        drawFigure(cellsOnBoard.get(Cell.F1), board);
        drawFigure(cellsOnBoard.get(Cell.G1), board);
        drawFigure(cellsOnBoard.get(Cell.H1), board);
        drawFigure(cellsOnBoard.get(Cell.A2), board);
        drawFigure(cellsOnBoard.get(Cell.B2), board);
        drawFigure(cellsOnBoard.get(Cell.C2), board);
        drawFigure(cellsOnBoard.get(Cell.D2), board);
        drawFigure(cellsOnBoard.get(Cell.E2), board);
        drawFigure(cellsOnBoard.get(Cell.F2), board);
        drawFigure(cellsOnBoard.get(Cell.G2), board);
        drawFigure(cellsOnBoard.get(Cell.H2), board);
    }

    /**
     * Adds chess piece to the Group UI board.
     * @param piece chess piece that we want to draw
     * @param board JavaFX Group which already contains chess grid
     */

    public void drawFigure(Piece piece, Group board) {
        if (piece != null) {

                Cell pos = piece.getPosition();
                // Add children(Piece) to grid
                board.getChildren().add(
                        buildFigure(pos.getX() * 40 + 5, pos.getY() * 40 + 5, 30, piece.getPieceImageName(),
                                piece)
                );

        }
    }

    /**
     * Build figure. Declare figure's image (Java FX Rectangle class). Define image height, width.
     * Set figure's position.
     * @param x position on X axis in chessboard.
     * @param y position on Y axis in chessboard.
     * @param size image size
     * @param imagePath imageName to access in resources
     * @param piece chess piece
     *
     * @return JavaFX Rectangle (chess piece) to add to chessboard Grid (Group JavaFX)
     */

    private Rectangle buildFigure(int x, int y, int size, String imagePath, Piece piece) {
        Rectangle rect = new Rectangle();
        // Set Figure's coordinates on chess board
        rect.setX(x);
        rect.setY(y);

        // Set Height and Width
        rect.setHeight(size);
        rect.setWidth(size);

        // Get Figure's image path from resources folder
        Image image = new Image(Objects.requireNonNull(getClass().getClassLoader().getResource(imagePath)).toString());
        ImagePattern imagePattern = new ImagePattern(image);

        // inserts Piece's image
        rect.setFill(imagePattern);

        pieceController.setMouseEventOnPiece(rect, piece);

        piece.setImage(rect);
        return rect;
    }

//    public void redrawBoard() {
//        for (Piece piece: board.getFigures()) {
//            if (piece.getPosition() != null) {
//                piece.getImage().setX(piece.getPosition().getX() * 40 + 5);
//                piece.getImage().setY(piece.getPosition().getY() * 40 + 5);
//            }
//        }
//     }

    public void deleteFigureFromUiBoard(Piece deletePiece) {

        if (deletePiece != null) {
            ((Group) deletePiece.getImage().getParent()).getChildren().remove(deletePiece.getImage());
        }

    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void drawPieces(Group board, HashMap<Cell, Piece> cellsOnBoard) {
        for (HashMap.Entry<Cell, Piece> entry : cellsOnBoard.entrySet()) {
            Cell cell = entry.getKey();
            Piece piece = entry.getValue();
            drawFigure(piece,board);
        }
    }
}
