package task01.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс реализующий паттерн Observer (наблюдатель) выступающий посредником в оповещнии о случившихся игровых процессах
 */
public class GameEventsPublisher implements GameListener{
    private boolean isAnyOneFollow;
    private List<GameListener> listeners;

    public GameEventsPublisher() {
        listeners = new ArrayList<>();
    }

    /**
     * Добавление слушателя игровых событий
     * @param listener подписываемый слушатель
     */
    public void subscribeListener(GameListener listener){
        listeners.add(listener);
        isAnyOneFollow = true;
    }

    /**
     * Удаление слушателя игровых событий
     * @param listener удаляемый слушатель
     */
    public void unsubscribeListener(GameListener listener){
        listeners.remove(listener);
        isAnyOneFollow = (!listeners.isEmpty());
    }

    @Override
    public void onGameStarted(int threadsCount) {
        if(isAnyOneFollow){
            for (GameListener l : listeners){
                l.onGameStarted(threadsCount);
            }
        }
    }

    @Override
    public void onGenChanged(GameField gameField, int currentGen) {
        if(isAnyOneFollow){
            for (GameListener l : listeners){
                l.onGenChanged(gameField, currentGen);
            }
        }
    }

    @Override
    public void fieldStateChanged(boolean state, int x, int y) {
        if(isAnyOneFollow){
            for (GameListener l : listeners){
                l.fieldStateChanged(state, x, y);
            }
        }
    }

    @Override
    public void gameFinished(GameField gameField) {
        if(isAnyOneFollow){
            for (GameListener l : listeners){
                l.gameFinished(gameField);
            }
        }
    }

    @Override
    public void gameFinishedTime(GameField gameField, Long time) {
        if(isAnyOneFollow){
            for (GameListener l : listeners){
                l.gameFinishedTime(gameField, time);
            }
        }
    }

    @Override
    public void successfulLoadFromFile(File file, GameField gameField) {
        if(isAnyOneFollow){
            for (GameListener l : listeners){
                l.successfulLoadFromFile(file, gameField);
            }
        }
    }

    @Override
    public void failedToReadFile(File file, IOException e) {
        if(isAnyOneFollow){
            for (GameListener l : listeners){
                l.failedToReadFile(file, e);
            }
        }
    }

    @Override
    public void wrongFileFormat(File file, RuntimeException e) {
        if(isAnyOneFollow){
            for (GameListener l : listeners){
                l.wrongFileFormat(file, e);
            }
        }
    }

    @Override
    public void successfulSaveToFile(GameField gameField, File file) {
        if(isAnyOneFollow){
            for (GameListener l : listeners){
                l.successfulSaveToFile(gameField, file);
            }
        }
    }

    @Override
    public void failedToSaveToFile(GameField gameField, File file, IOException e) {
        if(isAnyOneFollow){
            for (GameListener l : listeners){
                l.failedToSaveToFile(gameField, file, e);
            }
        }
    }

    @Override
    public void onGameException(Throwable e) {
        if(isAnyOneFollow){
            for (GameListener l : listeners){
                l.onGameException(e);
            }
        }
    }
}
