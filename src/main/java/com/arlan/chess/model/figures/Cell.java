/**
 * @author nurkharl
 */


package com.arlan.chess.model.figures;

import java.util.Arrays;

/**
 * Cell enum class represents signe cell on chess board (64 in total 8x8)
 */


public enum Cell {

    A1(0, 7), A2(0, 6), A3(0, 5), A4(0, 4), A5(0, 3), A6(0, 2), A7(0, 1), A8(0, 0),
    B1(1, 7), B2(1, 6), B3(1, 5), B4(1, 4), B5(1, 3), B6(1, 2), B7(1, 1), B8(1, 0),
    C1(2, 7), C2(2, 6), C3(2, 5), C4(2, 4), C5(2, 3), C6(2, 2), C7(2, 1), C8(2, 0),
    D1(3, 7), D2(3, 6), D3(3, 5), D4(3, 4), D5(3, 3), D6(3, 2), D7(3, 1), D8(3, 0),
    E1(4, 7), E2(4, 6), E3(4, 5), E4(4, 4), E5(4, 3), E6(4, 2), E7(4, 1), E8(4, 0),
    F1(5, 7), F2(5, 6), F3(5, 5), F4(5, 4), F5(5, 3), F6(5, 2), F7(5, 1), F8(5, 0),
    G1(6, 7), G2(6, 6), G3(6, 5), G4(6, 4), G5(6, 3), G6(6, 2), G7(6, 1), G8(6, 0),
    H1(7, 7), H2(7, 6), H3(7, 5), H4(7, 4), H5(7, 3), H6(7, 2), H7(7, 1), H8(7, 0);

    private int x;
    private int y;

    /**
     * Cell class constructor
     *
     * @param x - X coords
     * @param y - Y coords
     */

    Cell(int x, int y) {
        this.setX(x);
        this.setY(y);
    }


    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    /**
     * Return enum constant depending on input. If it is within 8x8 board, it returns valid cell on chessboard.
     * In case of invalid x or y, it will return null.
     *
     * @param x position on chessboard by X axis
     * @param y position on chessboard by Y axis
     * @return Cell if x and y are valid, else null
     */

    public static Cell fromIntToCell(int x, int y) {
        switch (x) {
            case 0:
                switch (y) {
                    case 0:
                        return A8;
                    case 1:
                        return A7;
                    case 2:
                        return A6;
                    case 3:
                        return A5;
                    case 4:
                        return A4;
                    case 5:
                        return A3;
                    case 6:
                        return A2;
                    case 7:
                        return A1;
                    default:
                        break;
                }
                break;
            case 1:
                switch (y) {
                    case 0:
                        return B8;
                    case 1:
                        return B7;
                    case 2:
                        return B6;
                    case 3:
                        return B5;
                    case 4:
                        return B4;
                    case 5:
                        return B3;
                    case 6:
                        return B2;
                    case 7:
                        return B1;
                    default:
                        break;
                }
                break;
            case 2:
                switch (y) {
                    case 0:
                        return C8;
                    case 1:
                        return C7;
                    case 2:
                        return C6;
                    case 3:
                        return C5;
                    case 4:
                        return C4;
                    case 5:
                        return C3;
                    case 6:
                        return C2;
                    case 7:
                        return C1;
                    default:
                        break;
                }
                break;
            case 3:
                switch (y) {
                    case 0:
                        return D8;
                    case 1:
                        return D7;
                    case 2:
                        return D6;
                    case 3:
                        return D5;
                    case 4:
                        return D4;
                    case 5:
                        return D3;
                    case 6:
                        return D2;
                    case 7:
                        return D1;
                    default:
                        break;
                }
                break;
            case 4:
                switch (y) {
                    case 0:
                        return E8;
                    case 1:
                        return E7;
                    case 2:
                        return E6;
                    case 3:
                        return E5;
                    case 4:
                        return E4;
                    case 5:
                        return E3;
                    case 6:
                        return E2;
                    case 7:
                        return E1;
                    default:
                        break;
                }
                break;
            case 5:
                switch (y) {
                    case 0:
                        return F8;
                    case 1:
                        return F7;
                    case 2:
                        return F6;
                    case 3:
                        return F5;
                    case 4:
                        return F4;
                    case 5:
                        return F3;
                    case 6:
                        return F2;
                    case 7:
                        return F1;
                    default:
                        break;
                }
                break;
            case 6:
                switch (y) {
                    case 0:
                        return G8;
                    case 1:
                        return G7;
                    case 2:
                        return G6;
                    case 3:
                        return G5;
                    case 4:
                        return G4;
                    case 5:
                        return G3;
                    case 6:
                        return G2;
                    case 7:
                        return G1;
                    default:
                        break;
                }
                break;
            case 7:
                switch (y) {
                    case 0:
                        return H8;
                    case 1:
                        return H7;
                    case 2:
                        return H6;
                    case 3:
                        return H5;
                    case 4:
                        return H4;
                    case 5:
                        return H3;
                    case 6:
                        return H2;
                    case 7:
                        return H1;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        return null;
    }

    public static Cell fromStringToCell(String cell) {
        String[] res = cell.split("");
        // -1 is invalid by default
        int x = -1, y = -1;

        switch (res[0]) {
            case "A":
                x = 0;
                break;
            case "B":
                x = 1;
                break;
            case "C":
                x = 2;
                break;
            case "D":
                x = 3;
                break;
            case "E":
                x = 4;
                break;
            case "F":
                x = 5;
                break;
            case "G":
                x = 6;
                break;
            case "H":
                x = 7;
                break;
            default:
                break;
        }

        switch (Integer.parseInt(res[1])) {
            case 1:
                y = 7;
                break;
            case 2:
                y = 6;
                break;
            case 3:
                y = 5;
                break;
            case 4:
                y = 4;
                break;
            case 5:
                y = 3;
                break;
            case 6:
                y = 2;
                break;
            case 7:
                y = 1;
                break;
            case 8:
                y = 0;
                break;
            default:
                break;
        }

        return fromIntToCell(x, y);
    }
}
