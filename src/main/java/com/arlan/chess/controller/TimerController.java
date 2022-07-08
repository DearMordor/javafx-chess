package com.arlan.chess.controller;

import com.arlan.chess.model.Board;
import javafx.scene.text.Text;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The TimerController is responsible for timer going and presenting time.
 */
public class TimerController {

    private Text whiteTimer = new Text();
    private Text blackTimer = new Text();
    private GameController gameController = new GameController();
    private static final  Logger LOG = Logger.getLogger(TimerController.class.getName());
    int count = 3600;

    public TimerController() {

        final Object sync = new Object();

        Thread TimerWhite  = new Thread(new Runnable() {

            @Override
            public void run() {

                long startTime;
                long elapsedSeconds = 0;
                long elapsedTime = 0;

                while (elapsedSeconds != count && !gameController.isOver()) {

                    synchronized (sync) {
                        if (!gameController.isTurnToMove()) {
                            LOG.fine("whiteTimer is on");
                            sync.notifyAll();

                            try {
                                LOG.fine("White player timer is running");
                                sync.wait();
                            } catch (InterruptedException e) {
                                LOG.log(Level.SEVERE, "White timer caught error: ", e);
                            }
                        }
                    }

                    startTime = System.currentTimeMillis();

                    try {
                        LOG.fine("Sleep for a second for timer!");
                        Thread.sleep(50);

                    } catch (InterruptedException e) {
                        LOG.log(Level.SEVERE, "Error message", e);
                    }

                    elapsedTime += System.currentTimeMillis() - startTime;

                    elapsedSeconds = elapsedTime / 1000;
                    long elapsedMinutes = elapsedSeconds / 60;
                    long secondDisplay = elapsedSeconds % 60;

                    TimerController.this.whiteTimer.setText("White Player: " + elapsedMinutes + ":" + secondDisplay);
                }
            }
        });

        Thread TimerBlack  = new Thread(new Runnable() {
            @Override
            public void run() {
                long startTime;
                long seconds = 0;
                long time = 0;

                while (seconds != count && !gameController.isOver()) {

                    synchronized (sync) {
                        if (gameController.isTurnToMove()) {
                            LOG.fine("whiteTimer is on");
                            sync.notifyAll();

                            try {
                                LOG.fine("timerWhite is running");
                                sync.wait();
                            } catch (InterruptedException e) {
                                LOG.log(Level.SEVERE, "Black timer caught error: ", e);
                            }
                        }

                    }

                    startTime = System.currentTimeMillis();

                    try {
                        LOG.fine("Sleep for a second for timer!");
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        LOG.log(Level.SEVERE, "Error message from sleeping: ", e);
                    }

                    time += System.currentTimeMillis() - startTime;

                    seconds = time / 1000;

                    long elapsedMinutes = seconds / 60;
                    long secondDisplay = seconds % 60;

                    TimerController.this.whiteTimer.setText("Black " + elapsedMinutes + ":" + secondDisplay);

                }
            }
        });
        TimerWhite.setDaemon(true);
        TimerBlack.setDaemon(true);
        TimerWhite.start();
        TimerBlack.start();

    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public Text getWhiteTimer() {
        return whiteTimer;
    }

    public Text getBlackTimer() {
        return blackTimer;
    }
}
