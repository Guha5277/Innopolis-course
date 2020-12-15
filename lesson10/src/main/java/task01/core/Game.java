package task01.core;

import task01.core.GameField;

import java.io.File;
import java.io.IOException;

public interface Game {
    GameField newInstance(int x, int y);
    GameField loadFromFile(File file);
    void saveToFile(GameField gameField, File file);
    GameField copyFrom(GameField gameField);
    void fillRandom(GameField gameField);
    void fillRandom(GameField gameField, int fillPercent);
    void pastGens(GameField gameField, int gens, int thread);
    long pastGensWithTimeRecording(GameField gameField, int gens, int thread);
    boolean isGameFieldsEquals(GameField gameField1, GameField gameField2);
    boolean isFilesEquals(File file1, File file2) throws IOException;
}
