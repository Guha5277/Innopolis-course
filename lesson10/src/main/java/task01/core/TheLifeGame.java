package task01.core;

import task01.utils.IOUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

public class TheLifeGame implements Game {
    private final GameListener listener;
    private GameEventsPublisher gameEventsPublisher;

    public TheLifeGame() {
        gameEventsPublisher = new GameEventsPublisher();
        this.listener = gameEventsPublisher;
    }

    public TheLifeGame(GameListener listener) {
        this.listener = listener;
    }

    public GameEventsPublisher getGameEventsPublisher() {
        return gameEventsPublisher;
    }

    @Override
    public GameField newInstance(int x, int y) {
        return new GameField(x, y, listener);
    }

    @Override
    public GameField loadFromFile(File file) {
        GameField gameField = null;
        try {
            char[] buf = IOUtils.readFile(file);
            try {
                gameField =  GameField.fromCharArray(buf);
                gameField.setGameListener(listener);
                listener.successfulLoadFromFile(file, gameField);
            } catch (IllegalArgumentException e) {
                listener.wrongFileFormat(file, e);
            }
        } catch (IOException e) {
            listener.failedToReadFile(file, e);
        }
        return gameField;
    }

    @Override
    public void saveToFile(GameField gameField, File file) {
        char[] buf = gameField.toCharArray();
        try {
            IOUtils.saveFile(buf, file);
            listener.successfulSaveToFile(gameField, file);
        } catch (IOException e) {
            listener.failedToSaveToFile(gameField, file, e);
        }
    }

    @Override
    public GameField copyFrom(GameField gameField) {
        return gameField.copy();
    }

    @Override
    public void fillRandom(GameField gameField) {
        gameField.randomize();
    }

    @Override
    public void fillRandom(GameField gameField, int fillPercent) {
        if (fillPercent < 0) {
            throw new IllegalArgumentException("The fill percentage should be a positive number. " +
                    "The current value is: " + fillPercent);
        }
        gameField.randomize(fillPercent);
    }

    @Override
    public void pastGens(GameField gameField, int gens, int threadsCount) {
        if (gens < 1 || threadsCount < 1 || gameField == null) {
            throw new IllegalArgumentException("The fields \"gens\" and \"threadsCount\" should be a positive number. " +
                    "The current values is: gens " + gens + " threadsCount " + threadsCount);
        }
        if (threadsCount > 1) {
            gameField.startMultithreadingGame(gens, threadsCount);
        } else {
            gameField.startSingleThreadGame(gens);
        }
        listener.gameFinished(gameField);
    }


    @Override
    public long pastGensWithTimeRecording(GameField gameField, int gens, int threadsCount) {
        long startTime = System.currentTimeMillis();
        pastGens(gameField, gens, threadsCount);
        listener.gameFinishedTime(gameField, System.currentTimeMillis() - startTime);
        return startTime;
    }

    @Override
    public boolean isGameFieldsEquals(GameField gameField1, GameField gameField2) {
        return gameField1.equals(gameField2);
    }

    @Override
    public boolean isFilesEquals(File file1, File file2) throws IOException {
        byte[] b1 = Files.readAllBytes(file1.toPath());
        byte[] b2 = Files.readAllBytes(file2.toPath());
        return Arrays.equals(b1, b2);
    }
}
