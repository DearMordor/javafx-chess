package com.arlan.chess;

import com.arlan.chess.model.figures.Cell;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CellTest {

    @Test
    public void fromStringToCellTest() {

        Cell expectedCell = Cell.B7;
        Cell resultCell = Cell.fromStringToCell("B7");

        assertEquals(expectedCell, resultCell);
    }
}
