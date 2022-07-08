package com.arlan.chess.controller;

import com.arlan.chess.model.figures.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * The SaveLoadController class is responsible for saving games and loading saved games.
 */
public class SaveLoadController {

    private String outputPath = "src/main/resources/saved-games/saved-game.txt";
    private GameController gameController;
    private boolean isVersusBot = false;
    private boolean turnToMove = false;

    /**
     * Set event on save button. When clicked, current game including pieces positions will be written in text file
     * @param button JavaFX button
     * @param logicBoard logic board where all main data about current chessboard is stored
     */

    public void setSaveButtonOnEvent(Button button, HashMap<Cell, Piece> logicBoard) {
        button.setOnAction(actionEvent -> {
            File file = new File(outputPath);
            BufferedWriter bf = null;
            try {
                // create new BufferedWriter for the output file
                bf = new BufferedWriter(new FileWriter(file));

                for (Map.Entry<Cell, Piece> entry : logicBoard.entrySet()) {
                    // put key and value separted by a colon
                    bf.write(entry.getKey() + ":" + entry.getValue().getClass().getSimpleName() + ":" + entry.getValue().isWhite());
                    // new line
                    bf.newLine();
                }
                bf.write("isVersusBot:" + gameController.isVersusBot());
                bf.newLine();
                bf.write("turnToMove:" + gameController.isTurnToMove());
                bf.newLine();

                bf.flush();
            } catch (IOException e) {
                Alert info = new Alert(Alert.AlertType.ERROR);
                info.setContentText(e.getClass().getName() +  " "  + e.getMessage());
                info.show();
            }
            finally {
                try {
                    assert bf != null;
                    bf.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        });
    }

    public void setActionOnLoadButton(Button button, Stage stage) {
        button.setOnAction(actionEvent -> {
            if (this.getClass().getResource("/saved-games/saved-game.txt") == null) {
                return;
            }
            gameController = new GameController();
            HashMap<Cell, Piece> cellToPieces = readHashMapFromTxtFile();
            String mode = this.isVersusBot ? "AI" : "Player";
            gameController.openWindow(stage, mode, true, cellToPieces);

        });
    }

    /**
     * This function opens text file where are located saved data about last game including pieces positions
     * and who moved last.
     *
     * @return HashMap which contains Pieces and cell where they are located
     */
    public HashMap<Cell, Piece> readHashMapFromTxtFile() {
        // TODO Check if file exists
        HashMap<Cell, Piece> logicBoard
                = new HashMap<Cell, Piece>();
        BufferedReader br = null;

        try {

            // create file object
            File file = new File(outputPath);

            // create BufferedReader object from the File
            br = new BufferedReader(new FileReader(file));

            String line = null;

            // read file line by line
            while ((line = br.readLine()) != null) {
                if (Objects.equals(line.split(":")[0], "isVersusBot")) {
                    if (Objects.equals(line.split(":")[1], "true")) {
                        setVersusBot(true);
                    }
                    continue;
                }
                if (Objects.equals(line.split(":")[0], "turnToMove")) {
                    if (Objects.equals(line.split(":")[1], "true")) {
                        setTurnToMove(true);
                    }
                    break;
                }

                String[] parts = line.split(":");

                String cellPosition = parts[0].trim();
                String piece = parts[1].trim();
                String isWhite = parts[2].trim();

                if (!cellPosition.equals("") && !piece.equals("") && !isWhite.equals("")) {
                    logicBoard.put(Cell.fromStringToCell(cellPosition), buildPiece(piece, cellPosition, isWhite));
                }
            }
        }
        catch (Exception e) {

            Alert info = new Alert(Alert.AlertType.ERROR);
            info.setContentText(e.getClass().getName() +  " "  + e.getMessage());
            info.show();

        }
        finally {

            // Always close the BufferedReader
            if (br != null) {
                try {
                    br.close();
                }
                catch (Exception e) {
                };
            }
        }

        return logicBoard;
    }

    /**
     * Build piece object to restore original previous objects.
     *
     * @param piece Chess piece we want to get
     * @param cell chess piece on chessboard
     * @param isWhite chess piece color. True if white, false if black
     *
     * @return chess piece
     */

    public Piece buildPiece(String piece, String cell, String isWhite) {
        Piece retPiece = null;
        Cell piecePosition = Cell.fromStringToCell(cell);
        boolean isWhiteBool = Boolean.parseBoolean(isWhite);

        switch (piece){
            case "Knight":
                retPiece = new Knight( isWhiteBool, piecePosition);
                break;
            case "Pawn":
                retPiece = new Pawn( isWhiteBool, piecePosition);
                break;
            case "Rook":
                retPiece = new Rook( isWhiteBool, piecePosition);
                break;
            case "Bishop":
                retPiece = new Bishop( isWhiteBool, piecePosition);
                break;
            case "Queen":
                retPiece = new Queen( isWhiteBool, piecePosition);
                break;
            case "King":
                retPiece = new King( isWhiteBool, piecePosition);
                break;
            default:
                break;
        }
        return retPiece;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public boolean isVersusBot() {
        return isVersusBot;
    }

    public void setVersusBot(boolean versusBot) {
        isVersusBot = versusBot;
    }

    public boolean isTurnToMove() {
        return turnToMove;
    }

    public void setTurnToMove(boolean turnToMove) {
        this.turnToMove = turnToMove;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }
}
