package com.arlan.chess.model;

/*
*  MoveType enum class contains types of moves for move indication.
* */
public enum MoveType {
    /* MOVEMENT: piece moves without killing.
    * ATTACK: piece attacks enemies piece.
    * ENPASSANT: pawn attacks enemy's piece.
    * ROGUE: king and rook makes roque move.
    * PROMOTION: If piece reaches enemy's firs row
    * */
    MOVEMENT, ATTACK, ENPASSANT, ROGUE, PROMOTION
}
