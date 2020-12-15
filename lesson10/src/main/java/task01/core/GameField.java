package task01.core;

import com.google.common.primitives.Booleans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

public class GameField implements Thread.UncaughtExceptionHandler{
    private static final char LINE_SEPARATOR = '\n';
    private int x;
    private int y;
    private int gen;
    private boolean isInterrupted;
    private boolean[][] fields;
    private GameListener gameListener;

    public GameField(int x, int y) {
        this.x = x;
        this.y = y;
        fields = new boolean[y][x];
        this.gameListener = new GameEventsPublisher();
    }

    public GameField(int x, int y, GameListener gameListener) {
        this.x = x;
        this.y = y;
        this.fields =  new boolean[y][x];
        this.gameListener = gameListener;
    }

    public void randomize() {
        Random random = new Random();
        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {
                fields[i][j] = random.nextBoolean();
            }
        }
    }

    public void randomize(int fillPercent) {
        if (fillPercent < 0 || fillPercent > 100) {
            throw new IllegalArgumentException("Wrong percent argument: " + fillPercent);
        }
        int cellsCount = x * y;
        int neededCount = (cellsCount * fillPercent) / 100;
        int filledCount = 0;

        Random random = new Random();

        while (filledCount < neededCount) {
            int randomX = random.nextInt(x);
            int randomY = random.nextInt(y);
            boolean targetCell = getCell(randomX, randomY);
            if (targetCell) {
                continue;
            }
            filledCount++;
            setCell(true, randomX, randomY);
        }
    }

    public static GameField fromCharArray(char[] contains) {
        if (contains == null) {
            throw new IllegalArgumentException("The input array should not be a null!", new Throwable("Input array is null!"));
        }
        String tempStr = new String(contains);
        int xFromArr = tempStr.indexOf(LINE_SEPARATOR);
        int yFromArr = (int) tempStr.chars().filter(c -> c == LINE_SEPARATOR).count();
        contains = tempStr.replaceAll(String.valueOf(LINE_SEPARATOR), "").toCharArray();

        if (xFromArr <= 0 || yFromArr <= 0 || contains.length % yFromArr != 0) {

            throw new IllegalArgumentException("The array is empty or missing a CRNL symbols. Check the file format", new Throwable("Wrong the input char array format!"));
        }

        GameField gameField = new GameField(xFromArr, yFromArr);
        int arrIndex = 0;
        for (int yCord = 0; yCord < yFromArr; yCord++) {
            for (int xCord = 0; xCord < xFromArr; xCord++) {
                gameField.setCell(contains[arrIndex] == '1', xCord, yCord);
                arrIndex++;
            }
        }

        return gameField;
    }

    public GameField copy() {
        GameField gameField = new GameField(x, y);
        gameField.setFields(copyFields());
        gameField.setGen(gen);
        gameField.setGameListener(gameListener);
        return gameField;
    }

    public char[] toCharArray() {
        char[] buf = new char[size() + y];
        int index = 0;
        for (int yCord = 0; yCord < y; yCord++) {
            for (int xCord = 0; xCord < x; xCord++) {
                buf[index++] = fields[yCord][xCord] ? '1' : '0';
            }
            buf[index++] = LINE_SEPARATOR;
        }
        return buf;
    }

    public int getFillPercentage() {
        long fillCellsCount = Arrays.stream(fields)
                .map(boolArr -> Booleans.asList(boolArr)
                        .stream()
                        .filter(bool -> bool)
                        .count())
                .reduce(Long::sum).orElse(0L);
        long size = size();
        return (int) (fillCellsCount * 100 / size);
    }

    private void nextGeneration() {
        GameField tempNextGen = new GameField(x, y);

        for (int yCord = 0; yCord < y; yCord++) {
            for (int xCord = 0; xCord < x; xCord++) {
                boolean previousCellValue = fields[yCord][xCord];
                boolean[] cells = getSurroundingCells(xCord, yCord);
                int result = 0;
                for (boolean b : cells) {
                    result += b ? 1 : 0;
                }
                boolean newCellValue = !previousCellValue && result == 3 || (previousCellValue && result >= 2 && result <= 3);
                tempNextGen.setCell(newCellValue, xCord, yCord);
                if (previousCellValue != newCellValue) {
                    gameListener.fieldStateChanged(newCellValue, xCord, yCord);
                }
            }
        }
        gen++;
        gameListener.onGenChanged(copy(), gen);
        this.fields = tempNextGen.copyFields();
    }

    public void startSingleThreadGame(int gens) {
        isInterrupted = false;
        gameListener.onGameStarted(1);

        for (int i = 0; i < gens; i++) {
            if (isEmpty() || isInterrupted) {
                System.out.println("The game is over at " + i + " gen (singleThread)");
                break;
            }
            nextGeneration();
        }
    }

    public void startMultithreadingGame(int gens, int threadsCount) {
        isInterrupted = false;
        gameListener.onGameStarted(threadsCount);
        ExecutorService executor = Executors.newFixedThreadPool(threadsCount);
        Phaser phaser = new Phaser(threadsCount + 1);
        Separator separator = new Separator(this, threadsCount);
        Separator.Coordinates[] subFieldsCord = separator.getSubFieldsCoordinates();

        GameField nextGen = new GameField(x, y);

        MultiThread[] threads = new MultiThread[threadsCount];
        for (int i = 0; i < threadsCount; i++) {
            threads[i] = new MultiThread(nextGen, subFieldsCord[i], phaser);
            executor.execute(threads[i]);
        }

        passGensMultiThread(gens, threadsCount, phaser, nextGen);

        for (int i = 0; i < threadsCount; i++) {
            threads[i].interrupt();
        }
        phaser.arriveAndDeregister();
        phaser.forceTermination();
        executor.shutdown();
    }

    public int size() {
        return x * y;
    }

    public boolean getCell(int x, int y) {
        return fields[y][x];
    }

    public void setGameListener(GameListener gameListener) {
        this.gameListener = gameListener;
    }

    public void setCell(boolean state, int x, int y) {
        if (x < 0 || y < 0 || y > this.y || x > this.x) {
            throw new IllegalArgumentException("Wrong input value: " + x + ":" + y);
        }
        fields[y][x] = state;
    }

    private void setFields(boolean[][] fields) {
        this.fields = fields;
    }

    public void setSurroundingCell(int x, int y) {
        int rightX = getRightX(x);
        int leftX = getLeftX(x);
        int upY = getUpY(y);
        int downY = getDownY(y);

        setCell(true, leftX, upY); //left-up
        setCell(true, x, upY); //up
        setCell(true, rightX, upY); //right-up
        setCell(true, rightX, y); //right
        setCell(true, rightX, downY); //right-down
        setCell(true, x, downY); //down
        setCell(true, leftX, downY); //left-down
        setCell(true, leftX, y); //left
    }

    public void clear() {
        for (int yCord = 0; yCord < y; yCord++) {
            for (int xCord = 0; xCord < x; xCord++) {
                setCell(false, xCord, yCord);
            }
        }
    }

    public GameField getSubField(int xFrom, int xTo, int yFrom, int yTo) {
        if (xFrom < 0 || yFrom < 0 || xTo >= x || yTo >= y) {
            throw new IllegalArgumentException("Invalid method arguments for getSubFieldsMethod!");
        }
        int xSize = (xTo - xFrom) + 1;
        int ySize = (yTo - yFrom) + 1;
        GameField gameField = new GameField(xSize, ySize);

        for (int innerY = 0; innerY <= yTo; innerY++) {
            for (int innerX = 0; innerX <= xTo; innerX++) {
                gameField.setCell(getCell(innerX, innerY), innerX, innerY);
            }
        }

        return gameField;
    }

    public boolean[] getSurroundingCells(int x, int y) {
        int rightX = getRightX(x);
        int leftX = getLeftX(x);
        int upY = getUpY(y);
        int downY = getDownY(y);

        return new boolean[]{
                fields[upY][leftX], fields[upY][x], fields[upY][rightX],
                fields[y][rightX], fields[downY][rightX], fields[downY][x],
                fields[downY][leftX], fields[y][leftX]};
    }

    public void interrupt() {
        isInterrupted = true;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getGen() {
        return gen;
    }

    public void setGen(int gen) {
        this.gen = gen;
    }

    public boolean isEmpty() {
        for (int yCord = 0; yCord < y; yCord++) {
            for (int xCord = 0; xCord < x; xCord++) {
                if (fields[yCord][xCord]) return false;
            }
        }
        return true;
    }

    private int getLeftX(int x) {
        int result = (x - 1) % this.x;
        return result < 0 ? (this.x - 1) : result;
    }

    private int getRightX(int x) {
        return (x + 1) % this.x;
    }

    private int getUpY(int y) {
        int result = (y - 1) % this.y;
        return result < 0 ? (this.y - 1) : result;
    }

    private int getDownY(int y) {
        return (y + 1) % this.y;
    }

    private boolean[][] copyFields() {
        boolean[][] result = new boolean[y][x];
        for (int i = 0; i < result.length; i++) {
            result[i] = Arrays.copyOf(fields[i], fields[i].length);
        }
        return result;
    }

    private void passGensMultiThread(int gens, int threadsCount, Phaser phaser, GameField nextGen) {
        for (int i = 0; i < gens; i++) {
            if (this.isEmpty() || isInterrupted) {
                System.out.println("The game is over at " + (i) + " gen");
                break;
            }

            while (phaser.getArrivedParties() != threadsCount) {
            }
            fields = nextGen.copyFields();
            nextGen.clear();
            gameListener.onGenChanged(copy(), gen);
            gen++;

            if (i == gens - 1) {
                break;
            }

            phaser.arrive();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof GameField) {
            boolean gens = this.gen == ((GameField) obj).gen;
            boolean size = (this.x == ((GameField) obj).x) && (this.y == ((GameField) obj).y);
            if (gens && size) {
                for (int i = 0; i < fields.length; i++) {
                    if (!Arrays.equals(fields[i], ((GameField) obj).fields[i])) {
                        return false;
                    }
                }
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("-----Game Field-----");
        sb.append("\n");

        for (int yCord = 0; yCord < y; yCord++) {
            for (int xCord = 0; xCord < x; xCord++) {
                sb.append(getCell(xCord, yCord) ? "[x]" : "[ ]");
            }
            sb.append('\n');
        }

        ArrayList<Integer> integers = new ArrayList<>();

        return sb.toString();
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        gameListener.onGameException(e);
    }

    private class MultiThread implements Runnable {
        private boolean isInterrupted;
        private GameField gameField;
        private Phaser phaser;
        private Separator.Coordinates coordinates;

        public MultiThread(GameField gameField, Separator.Coordinates coordinates, Phaser phaser) {
            this.gameField = gameField;
            this.phaser = phaser;
            this.coordinates = coordinates;
        }

        @Override
        public void run() {
            while (!isInterrupted) {
                for (int yCord = coordinates.getYFrom(); yCord <= coordinates.getYTo(); yCord++) {
                    for (int xCord = coordinates.getXFrom(); xCord <= coordinates.getXTo(); xCord++) {
                        boolean previousCellValue = fields[yCord][xCord];
                        boolean[] cells = getSurroundingCells(xCord, yCord);
                        int result = 0;
                        for (boolean b : cells) {
                            result += b ? 1 : 0;
                        }
                        boolean newCellValue = !previousCellValue && result == 3 || (previousCellValue && result >= 2 && result <= 3);
                        gameField.setCell(newCellValue, xCord, yCord);
                        if (previousCellValue != newCellValue) {
                            gameListener.fieldStateChanged(newCellValue, xCord, yCord);
                        }
                    }
                }
                phaser.arriveAndAwaitAdvance();
            }
            phaser.arriveAndDeregister();
        }

        public void interrupt() {
            isInterrupted = true;
        }
    }
}
