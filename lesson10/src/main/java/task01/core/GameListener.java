package task01.core;

import java.io.File;
import java.io.IOException;

public interface GameListener {
    /**
     * Извещает о начале игры
     * @param threadsCount количество потоков (1 - в случае однопоточной игры)
     */
    void onGameStarted(int threadsCount);

    /**
     * Извещает о смене поколения
     * @param currentGen номер текущего поколения
     * @param gameField игровое поле
     */
    void onGenChanged(GameField gameField, int currentGen);

    /**
     * Извещает об изменении состояния ячейки
     * @param state статус ячейки
     * @param x координата ячейки по оси X
     * @param y координата ячейки по оси Y
     */
    void fieldStateChanged(boolean state, int x, int y);

    /**
     * Оповещает о завершении игры
     * @param gameField экземпляр класса GameField в котором игра была окончена
     */
    void gameFinished(GameField gameField);

    /**
     * Оповещает о времени выполнения игры с момента её старта и до завершения
     * @param gameField экземпляр класса который был запущен и завершил игру
     * @param time затраченное экземпляром на игру время (мс)
     */
    void gameFinishedTime(GameField gameField, Long time);

    /**
     * Оповещает об успешной загрузки данных из файла
     * @param file загруженный файл
     * @param gameField полученное игровое поле из
     */
    void successfulLoadFromFile(File file, GameField gameField);

    /**
     * Оповещает об ошибке при попытке загрузить данные из файла
     * @param file файл при чтении которого возникла ошибка
     * @param e возникшее исключение
     */
    void failedToReadFile(File file, IOException e);

    /**
     * Опевещает об ошибке создания экземпляра GameField из класса
     * в виду ошибки формата файла
     * @param file загруженный файл
     * @param e возникшее исключение
     */
    void wrongFileFormat(File file, RuntimeException e);

    /**
     * Оповещает об успешном сохранение экземпляра GameField в файл
     * @param gameField сохранённый экземпляр
     * @param file файл в который произошла запись экземпляра
     */
    void successfulSaveToFile(GameField gameField, File file);

    /**
     * Оповещает об ошибке записи экземпляра GameField в файл
     * @param gameField экземпляр сохраняемого класса
     * @param file файд при записи в которой произошла ошибка
     */
    void failedToSaveToFile(GameField gameField, File file, IOException e);

    /**
     * Оповещает о возникновении любых других исключений
     * @param e возникшее исключение
     */
    void onGameException(Throwable e);
}
