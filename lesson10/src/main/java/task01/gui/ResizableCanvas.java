package task01.gui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.util.Duration;
import task01.core.GameField;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ResizableCanvas extends Canvas {
    private static final int PADDING = 15;
    private boolean isHidden;
    private int frameTime;
    private double x;
    private GraphicsContext graphicsContext;
    private GameField gameField;
    private ArrayDeque<GameField> gameFieldsQueue;
    private Timeline timeline;
    private CanvasEventsListener listener;

    public ResizableCanvas(CanvasEventsListener listener) {
        this.graphicsContext = getGraphicsContext2D();
        gameFieldsQueue = new ArrayDeque<>();
        this.listener = listener;
    }

    public void preview(GameField gameField) {
        isHidden = false;
        this.gameField = gameField;
        repaintGameField(gameField);
    }

    public void startPainting() {
        isHidden = false;
        timeline = new Timeline();
        timeline.setOnFinished((event) -> {
            listener.onAnimationEnd();
        });

        long timeIndex = frameTime;

        while (!gameFieldsQueue.isEmpty()) {
            GameField tempGameField = gameFieldsQueue.poll();

            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(timeIndex), actionEvent -> {
                this.gameField = tempGameField;
                repaintGameField(tempGameField);
            }));
            timeIndex += frameTime;
        }

        timeline.play();
    }

    public void clear() {
        graphicsContext.clearRect(0, 0, getWidth(), getHeight());
    }

    public void addToQueue(GameField tempGameField) {
        gameFieldsQueue.addLast(tempGameField);
    }

    public void hide() {
        isHidden = true;
    }

    public void stopAnimation() {
        timeline.stop();
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public void resize(double width, double height) {
        super.setWidth(width);
        super.setHeight(height);
        invalidatePaint();
    }

    private void invalidatePaint() {
        if (gameField == null) {
            return;
        }
        repaintGameField(gameField);
    }

    private void repaintGameField(GameField gameField) {
        if (isHidden) {
            return;
        }
        clear();
        drawNewGameField(graphicsContext, gameField);
    }

    private void drawNewGameField(GraphicsContext context, GameField gameField) {
        double width = getWidth();
        double height = getHeight();

        int columnsCount = gameField.getX();
        int linesCount = gameField.getY();

        double cellSize = calculateCellSize(width, height, columnsCount, linesCount);

        double rectWidth = columnsCount * cellSize;
        double rectHeight = linesCount * cellSize;

        this.x = calculateX(gameField.getX(), gameField.getY(), cellSize, width, rectWidth);

        drawMainRectangle(context, rectWidth, rectHeight);
        drawGrid(context, gameField, cellSize);
        fillCells(context, gameField, cellSize);
        context.strokeRoundRect(x, PADDING, rectWidth, rectHeight, 5, 5);
    }

    private double calculateCellSize(double width, double height, int x, int y) {
        double cellWidth = (width - PADDING * 2) / x;
        double cellHeight = (height - PADDING * 2) / y;
        return Math.min(cellHeight, cellWidth);
    }

    private double calculateX(int columnsCount, int linesCount, double cellSize, double width, double rectWidth) {
        return rectWidth > width ? 0 : (width / 2) - (rectWidth / 2);
    }

    private void drawMainRectangle(GraphicsContext context, double rectWidth, double rectHeight) {
        Stop[] stops = new Stop[]{
                new Stop(0, Color.AQUA),
                new Stop(1, Color.RED)
        };
        LinearGradient lg = new LinearGradient(0, 0, 1, 1, true, CycleMethod.REFLECT, stops);
        context.setFill(lg);
        context.fillRoundRect(x, PADDING, rectWidth, rectHeight, 5, 5);
    }

    private void drawGrid(GraphicsContext context, GameField gameField, double cellSize) {
        if (cellSize > 6) {
            double lineWidth = context.getLineWidth();
            double tempX = x;
            //draw vertical lines
            for (int i = 0; i < gameField.getX(); i++) {
                context.strokeLine(tempX, PADDING, tempX, PADDING + (gameField.getY() * cellSize));
                tempX += cellSize;
            }

            //draw horizontal lines
            double y = cellSize + lineWidth + PADDING;
            for (int i = 0; i < gameField.getY() - 1; i++) {
                context.strokeLine(x, y, x + (gameField.getX() * cellSize), y);
                y += cellSize;
            }
        }
    }

    private void fillCells(GraphicsContext context, GameField gameField, double cellSize) {
        Color cellColor = Color.WHITE;
        context.setFill(cellColor);

        for (int y = 0; y < gameField.getY(); y++) {
            for (int x = 0; x < gameField.getX(); x++) {
                if (gameField.getCell(x, y)) {
                    context.fillRoundRect(this.x + (x * cellSize), (y * cellSize) + PADDING, cellSize, cellSize, 15, 15);
                }
            }
        }
    }

    public void setFrameTime(int frameTime) {
        this.frameTime = frameTime;
    }
}
