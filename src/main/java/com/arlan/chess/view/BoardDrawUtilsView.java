package com.arlan.chess.view;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * The BoardDrawUtilsView is responsible for grid cells
 */
public class BoardDrawUtilsView {

    private static final int COUNT_OF_CELLS = 8;
    public static final int CELL_PIXEL_SIZE = 40;

    /**
     * Draw chessboard cells grid as JavaFX Group
     */

    public Group drawCells() {
        Group board = new Group();
        boolean white = true;

        for (int i = 0; i < COUNT_OF_CELLS; i++ ) {
            for (int j = 0; j < COUNT_OF_CELLS; j++) {
                if ( (i + j) % 2 == 0 ) {
                    white = false;
                } else {
                    white = true;
                }
                board.getChildren().add(buildRectangle(i, j, CELL_PIXEL_SIZE, white));

            }
        }

        return board;
    }

    /**
     * Build one piece of chessboard grid.
     *
     * @param x position in chessboard UI on X-axis
     * @param y position in chessboard UI on Y-axis
     * @param size rectangle (chessboard grid) size
     * @param white if it is white cell. It alternates.
     *
     * @return JavaFX Rectangle (Chessboard Cell)
     */
    private Rectangle buildRectangle(int x, int y, int size, boolean white) {
        Rectangle rectangle = new Rectangle();
        rectangle.setX(x * size);
        rectangle.setY(y * size);
        rectangle.setHeight(size);
        rectangle.setWidth(size);
        rectangle.setStroke(Color.BLACK);

        if(white) {
            rectangle.setFill(Color.LIGHTYELLOW);
        } else {
            rectangle.setFill(Color.FORESTGREEN);
        }

        return rectangle;
    }
    // TODO look at it
//    private BoardDrawUtilsView() {
//
//    }
}
