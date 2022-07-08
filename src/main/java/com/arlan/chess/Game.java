/**
 * @author nurkharl
 */


package com.arlan.chess;

import com.arlan.chess.controller.GameController;
import com.arlan.chess.controller.SaveLoadController;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Main class where game starts.
 */

public class Game extends Application {

    GameController gameController = new GameController();
    SaveLoadController saveLoadController = new SaveLoadController();

    @Override
    public void start(Stage stage) throws Exception {

        Text text = new Text("Choose mode");
        text.setFill(Color.WHITE);
        text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 36));

        Font BtnFont = Font.font("Courier New", FontWeight.BOLD, 20);

        Button vsPlayerBtn = new Button("Versus Player");
        Button vsAiBot = new Button("Versus AI");
        Button loadGame = new Button("Load game");

        vsPlayerBtn.setFont(BtnFont);
        vsAiBot.setFont(BtnFont);
        loadGame.setFont(BtnFont);
        vsPlayerBtn.setStyle("-fx-pref-width: 200px");
        vsAiBot.setStyle("-fx-pref-width: 200px");
        loadGame.setStyle("-fx-pref-width: 200px");

        vsPlayerBtn.setOnMouseClicked(
                event -> {
                    gameController.openWindow(stage, "Player", false, null);
                }
        );

        vsAiBot.setOnMouseClicked(
                even -> {
                    gameController.openWindow(stage, "AI", false, null);
                });

        saveLoadController.setActionOnLoadButton(loadGame, stage);

        VBox root = new VBox(30, text, vsPlayerBtn, vsAiBot, loadGame);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #282c34");
        Scene scene = new Scene(root, 400, 400);
        stage.setScene(scene);
        stage.show();

    }


    /**
     * Main function to start game
     */

    public static void main(String[] args) {
        launch(args);
    }

}
