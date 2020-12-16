package task01.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;


class TheLifeGameTest {
    File incorrectFile = new File("");

    @Mock
    private GameListener gameListener = Mockito.mock(GameListener.class);

    @InjectMocks
    private Game lifeGame;

    @BeforeEach
    void setUp() {
        lifeGame = new TheLifeGame(gameListener);
    }

    @Test
    void loadFromEmptyFile() {
        GameField loadedGameField = lifeGame.loadFromFile(incorrectFile);
        assertNull(loadedGameField);
        verify(gameListener, times(1)).failedToReadFile(eq(incorrectFile), anyObject());
    }

    @Test
    void saveNegativeTest(){
        GameField gameField = new GameField(5,5);
        lifeGame.saveToFile(gameField, incorrectFile);
        verify(gameListener, times(1)).failedToSaveToFile(eq(gameField), eq(incorrectFile), anyObject());
    }

    @Test
    void runGameTest(){
        GameField gameField = lifeGame.newInstance(10,10);
        lifeGame.fillRandom(gameField);
        lifeGame.pastGens(gameField, 10, 1);
        verify(gameListener, times(1)).gameFinished(gameField);
    }

    @Test
    void runGameWithTimeRecording(){
        GameField gameField = lifeGame.newInstance(10,10);
        lifeGame.fillRandom(gameField);
        long time = lifeGame.pastGensWithTimeRecording(gameField, 10, 1);
        assertTrue(time > 0);
        verify(gameListener, times(1)).gameFinished(any(GameField.class));
        verify(gameListener, times(1)).gameFinishedTime(any(GameField.class), anyLong());
    }

    @Test
    void wrongInputArgumentsToRunMethod(){
        assertThrows(IllegalArgumentException.class, () -> lifeGame.pastGens(null, -1, -1));
    }

    @Test
    void timeComparingTest(){
        lifeGame = new TheLifeGame();
        GameField first = lifeGame.newInstance(1000, 1000);
        lifeGame.fillRandom(first, 50);
        GameField second = lifeGame.copyFrom(first);
        assertEquals(first, second);
        long firstGameTime = lifeGame.pastGensWithTimeRecording(first, 100, 1);
        long secondGameTime = lifeGame.pastGensWithTimeRecording(second, 100, 4);
        assertTrue(firstGameTime > secondGameTime);
        assertEquals(first, second);
    }
}