package task01.core;

import java.io.File;
import java.io.IOException;

/**
 * Интерфейс полного игрового функционала
 */
public interface Game {
    /**
     * Фабричный метод создания нового экземпляра GameField
     * @param x размер по оси x
     * @param y размер по оси y
     * @return новое игровое поле с заданными характеристиками
     */
    GameField newInstance(int x, int y);

    /**
     * Метод загрузки игрового поля из файла
     * @param file файл из которого необходимо произвести загрузку
     * @return полученное игровое поле
     */
    GameField loadFromFile(File file);

    /**
     * Сохранение игрового поля в файл
     * @param gameField игровое поле которое подлежит сохранению
     * @param file файл назначения
     */
    void saveToFile(GameField gameField, File file);

    /**
     * Дублирование игрового поля
     * @param gameField поле, которое необходимо скопировать
     * @return скопированное игровое поле
     */
    GameField copyFrom(GameField gameField);

    /**
     * Заполнение игрового поля случайными элементами.
     * @param gameField игровое поле подлежащее заполнению
     */
    void fillRandom(GameField gameField);

    /**
     * Заполнение игрового поля случайными элементами с утановленным процентом заполнения
     * @param gameField игровое поле подлежащее заполнению
     * @param fillPercent необходимы процент заполнения поля живыми клетками
     */
    void fillRandom(GameField gameField, int fillPercent);

    /**
     * Метод запуска игры (генерации поколений)
     * @param gameField игровое поле подлежащее запуску
     * @param gens необходимое количество поколений
     * @param thread необходимое количество потоков исполнения
     */
    void pastGens(GameField gameField, int gens, int thread);

    /**
     * Метод запуска игры (генерации поколений) с записью занятого на генерацию времени
     * @param gameField игрвоое поле подлежащее запуску
     * @param gens необходимое количество поколений
     * @param thread необходимое количество потоков исполнения
     * @return затраченное на игру время в миллисекундах
     */
    long pastGensWithTimeRecording(GameField gameField, int gens, int thread);

    /**
     * Сравнение двух игровых полей на эквивалентность
     * @param gameField1 первое поле для сравнения
     * @param gameField2 второе поле для сравнения
     * @return результат сравнения
     */
    boolean isGameFieldsEquals(GameField gameField1, GameField gameField2);

    /**
     * Сравнение двух игровых полей, записанных в файлы на эквивалентность
     * @param file1 первый файл для сравнения
     * @param file2 второй файл для сравнения
     * @return результат сравнения
     * @throws IOException в случае отсутствия файла, проблем с чтением
     */
    boolean isFilesEquals(File file1, File file2) throws IOException;
}
