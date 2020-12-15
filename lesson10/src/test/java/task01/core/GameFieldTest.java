package task01.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class GameFieldTest {
    int x = 10;
    int y = 10;
    int defaultGen = 0;
    char[] inputArray = new char[]
            {
                    48, 49, 48, 49, '\n',
                    48, 49, 48, 49, '\n',
                    48, 49, 48, 49, '\n',
                    48, 49, 48, 49, '\n'
            }; //input GameField

    char[] outputArray = new char[]{
            48, 49, 48, '\n',
            49, 49, 49, '\n',
            48, 49, 48, '\n'
    }; //output GameField

    char[] incorrectInputChar = new char[]
            {
                    48, 49, 48, 49,
                    48, 49, 48, 49,
                    48, 49, 48, 49,
                    48, 49, 48, 49
            };

    char[] incorrectInputChar2 = new char[]
            {
                    48, 49, 48, 49, '\n',
                    48, 49, 48, 49, '\n',
                    48, 49, 48, 49, '\n',
                    48, 49, 48, '\n'
            };


    GameField mainField;

    @BeforeEach
    void setUp() {
        mainField = new GameField(x, y);
    }

    @Test
    void newInstanceTest() {
        GameField gameField = new GameField(x, y);
        assertNotNull(gameField);
        assertEquals(x, gameField.getX());
        assertEquals(y, gameField.getY());
        assertEquals(gameField.getGen(), defaultGen);
    }

    @Test
    void instanceFromCharArray() {
        GameField gameField = GameField.fromCharArray(inputArray);
        assertEquals(inputArray.length - gameField.getY(), gameField.size());
        assertEquals(4, gameField.getX());
        assertEquals(4, gameField.getY());
        assertEquals(defaultGen, 0);
        assertTrue(gameField.getCell(1, 0));
        assertTrue(gameField.getCell(3, 0));
        assertTrue(gameField.getCell(1, 1));
        assertTrue(gameField.getCell(3, 1));
        assertTrue(gameField.getCell(1, 2));
        assertTrue(gameField.getCell(3, 2));
        assertTrue(gameField.getCell(1, 3));
        assertTrue(gameField.getCell(3, 3));
    }

    @Test
    void toCharArray() {
        GameField gameField = new GameField(3, 3);
        gameField.setCell(true, 1, 0);
        gameField.setCell(true, 0, 1);
        gameField.setCell(true, 1, 1);
        gameField.setCell(true, 2, 1);
        gameField.setCell(true, 1, 2);

        char[] actual = gameField.toCharArray();
        assertTrue(Arrays.equals(outputArray, actual));
    }

    @Test
    void fromCharAndToCharArray() {
        GameField gameField = GameField.fromCharArray(inputArray);
        char[] actualArray = gameField.toCharArray();
        assertTrue(Arrays.equals(inputArray, actualArray));
    }

    @Test
    void wrongInputCharArrTest(){
        assertThrows(IllegalArgumentException.class, () -> GameField.fromCharArray(null));
        assertThrows(IllegalArgumentException.class, () -> GameField.fromCharArray(incorrectInputChar));
        assertThrows(IllegalArgumentException.class, () -> GameField.fromCharArray(incorrectInputChar2));
    }

    @Test
    void randomize() {
        boolean b1 = false;
        mainField.randomize();

        for (int y = 0; y < mainField.getY(); y++) {
            for (int x = 0; x < mainField.getX(); x++) {
                if (b1) break;
                b1 = mainField.getCell(x, y);
            }
        }
        assertTrue(b1);
    }

    @Test
    void randomizeWithFillPercent() {
        int expectedFillPercentage = 10;
        mainField.randomize(expectedFillPercentage);
        int actualFillPercentage = mainField.getFillPercentage();
        assertEquals(expectedFillPercentage, actualFillPercentage);

        expectedFillPercentage = 50;
        mainField.clear();
        mainField.randomize(expectedFillPercentage);
        actualFillPercentage = mainField.getFillPercentage();

        assertEquals(expectedFillPercentage, actualFillPercentage);

        expectedFillPercentage = 100;
        mainField.clear();
        mainField.randomize(expectedFillPercentage);
        actualFillPercentage = mainField.getFillPercentage();
        assertEquals(expectedFillPercentage, actualFillPercentage);
    }

    @Test
    void randomizeNegativeTest(){
        assertThrows(IllegalArgumentException.class, () -> mainField.randomize(-1));
        assertThrows(IllegalArgumentException.class, () -> mainField.randomize(101));
    }

    @Test
    void zeroRandomizeTest() {
        mainField.randomize(0);

        int fillCellsCount = 0;
        int emptyCellsCount = 0;

        for (int y = 0; y < mainField.getY(); y++) {
            for (int x = 0; x < mainField.getX(); x++) {
                boolean currentCell = mainField.getCell(x, y);
                fillCellsCount += currentCell ? 1 : 0;
                emptyCellsCount += currentCell ? 0 : 1;
            }
        }
        assertEquals(0, fillCellsCount);
        assertEquals(mainField.size(), emptyCellsCount);
    }

    @Test
    void clearTest() {
        mainField.randomize(100);
        assertTrue(mainField.getCell(0, 0));
        mainField.clear();
        assertFalse(mainField.getCell(0, 0));
    }

    @Test
    void copyTest() {
        GameField generatedField = new GameField(10, 10);
        generatedField.randomize(50);
        GameField copy1 = generatedField.copy();
        GameField copy2 = generatedField.copy();
        GameField notCopy = new GameField(10, 10);

        assertEquals(generatedField, copy1);
        assertEquals(generatedField, copy2);
        assertEquals(copy1, copy2);
        assertNotEquals(generatedField, notCopy);
        copy1.clear();
        assertNotEquals(copy1, copy2);
        assertNotEquals(copy1, generatedField);
    }

    @Test
    void sizeTest() {
        GameField gameField1 = new GameField(3, 3);
        GameField gameField2 = new GameField(4, 4);
        GameField gameField3 = new GameField(5, 5);
        GameField gameField4 = new GameField(10, 10);
        GameField gameField5 = new GameField(100, 100);

        assertEquals(9, gameField1.size());
        assertEquals(16, gameField2.size());
        assertEquals(25, gameField3.size());
        assertEquals(100, gameField4.size());
        assertEquals(10000, gameField5.size());
    }

    @Test
    void setCellNegative() {
        assertThrows(IllegalArgumentException.class, () -> mainField.setCell(true, -1, 0));
        assertThrows(IllegalArgumentException.class, () -> mainField.setCell(true, 0, -1));
        assertThrows(IllegalArgumentException.class, () -> mainField.setCell(true, 0, 1000000));
    }

    @Test
    void singleThreadGameTest() {
        mainField.randomize(100);
        GameField notExpected = mainField.copy();
        mainField.startSingleThreadGame(5);

        assertNotEquals(notExpected, mainField);
        assertEquals(1, mainField.getGen());
    }

    @Test
    void multiThreadGameStart() {
        mainField.randomize(100);
        GameField notExpected = mainField.copy();
        mainField.startMultithreadingGame(5, 2);

        assertNotEquals(notExpected, mainField);
        assertEquals(1, mainField.getGen());
    }

    @Test
    void genTest() {
        int gens = 5;
        GameField singleThreadGame = GameField.fromCharArray(inputArray);
        GameField multiThreadGame = GameField.fromCharArray(inputArray);

        singleThreadGame.startSingleThreadGame(gens);
        multiThreadGame.startMultithreadingGame(gens, 2);

        assertEquals(5, singleThreadGame.getGen());
        assertEquals(5, multiThreadGame.getGen());
    }

}