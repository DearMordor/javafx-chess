package com.arlan.chess;

import com.arlan.chess.controller.SaveLoadController;
import com.arlan.chess.model.figures.Cell;
import com.arlan.chess.model.figures.Knight;
import com.arlan.chess.model.figures.Pawn;
import com.arlan.chess.model.figures.Piece;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class SaveLoadControllerTest {

    private final SaveLoadController saveLoadController = new SaveLoadController();

    @Test
    public void buildPieceReturnsPawnTest() {
        String expectedPiece = "Pawn";
        String expectedCell = "A7";
        String expectedIsWhite = "false";

        Piece testPiece = saveLoadController.buildPiece(expectedPiece, expectedCell, expectedIsWhite);

        assertTrue(testPiece instanceof Pawn);
    }

    @Test
    public void buildPieceCheckPieceParameters() {
        String expectedPiece = "King";
        String expectedCell = "E1";
        String expectedIsWhite = "true";

        Piece testPiece = saveLoadController.buildPiece(expectedPiece, expectedCell, expectedIsWhite);

        assertSame(testPiece.getPosition(), Cell.E1);
        assertTrue(testPiece.isWhite());
    }

    @Test
    public void readHashMapFromTxtFileTest() {
        // First change location for test data.
        saveLoadController.setOutputPath("src/test/java/com/arlan/chess/testSavedGames/test-saved-game");
        HashMap<Cell, Piece> cellsToPiece = saveLoadController.readHashMapFromTxtFile();

        // Check if hashmap contains White/Black pieces on B7 etc. Cells as defined in
        // `src/test/testSavedGames/test-saved-game`
        assertTrue((cellsToPiece.get(Cell.B7) instanceof Pawn) && !cellsToPiece.get(Cell.B7).isWhite());

        assertTrue((cellsToPiece.get(Cell.G4) instanceof Knight) && !cellsToPiece.get(Cell.G4).isWhite());

        assertTrue((cellsToPiece.get(Cell.B1) instanceof Knight) && cellsToPiece.get(Cell.B1).isWhite());

        assertTrue((cellsToPiece.get(Cell.E7) instanceof Pawn) && !cellsToPiece.get(Cell.E7).isWhite());

        assertTrue((cellsToPiece.get(Cell.B6) != null) && !cellsToPiece.get(Cell.B6).isWhite());

        // It should be true by test txt file
        assertTrue(saveLoadController.isVersusBot());
    }
}
