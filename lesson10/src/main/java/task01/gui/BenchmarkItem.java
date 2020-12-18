package task01.gui;

import task01.core.GameField;

/**
 * Класс инкапсулирующий в себе промежуточные (или итоговые) результаты сравнения двух игровых полей
 * Он содержит общую информацию об игровых полях, характеристиках их запуска, текущем состоянии сравнения
 */
class BenchmarkItem {
    private boolean isFirstFieldCalculated;
    private boolean isAllFieldsCalculated;
    private GameField fieldFirstStart;
    private GameField fieldFirstEnd;
    private GameField fieldSecondStart;
    private GameField fieldSecondEnd;

    private int gensFirs;
    private int gensSecond;
    private int threadsFirst;
    private int threadsSecond;
    private long timeFirst;
    private long timeSecond;

    public BenchmarkItem(GameField fieldFirstStart, GameField fieldSecondStart, int gensFirs, int gensSecond, int threadsFirst, int threadsSecond) {
        this.fieldFirstStart = fieldFirstStart;
        this.fieldSecondStart = fieldSecondStart;
        this.gensFirs = gensFirs;
        this.gensSecond = gensSecond;
        this.threadsFirst = threadsFirst;
        this.threadsSecond = threadsSecond;
    }

    /**
     * Метод для определния того, было ли просчитано первое игровое поле
     * @return состояние первого игрового поля
     */
    public boolean isFirstFieldCalculated() {
        return isFirstFieldCalculated;
    }

    /**
     * Метод устанавливающий первое игровое поле как просчитаное
     */
    public void firstFieldCalculated() {
        isFirstFieldCalculated = true;
    }

    /**
     * Метод для опредения того, были ли все поля просчитаны
     * @return состояние всех игровых полей
     */
    public boolean isAllFieldsCalculated() {
        return isAllFieldsCalculated;
    }

    /**
     * Метод устанавливающий все игровые поля как просчитаные
     */
    public void allFieldsCalculated() {
        isAllFieldsCalculated = true;
    }

    public GameField getFieldFirstStart() {
        return fieldFirstStart;
    }

    public GameField getFieldFirstEnd() {
        return fieldFirstEnd;
    }

    public GameField getFieldSecondStart() {
        return fieldSecondStart;
    }

    public GameField getFieldSecondEnd() {
        return fieldSecondEnd;
    }

    public int getGensFirs() {
        return gensFirs;
    }

    public int getGensSecond() {
        return gensSecond;
    }

    public int getThreadsFirst() {
        return threadsFirst;
    }

    public int getThreadsSecond() {
        return threadsSecond;
    }

    public long getTimeFirst() {
        return timeFirst;
    }

    public long getTimeSecond() {
        return timeSecond;
    }

    public void setFieldFirstEnd(GameField fieldFirstEnd) {
        this.fieldFirstEnd = fieldFirstEnd;
    }

    public void setFieldSecondEnd(GameField fieldSecondEnd) {
        this.fieldSecondEnd = fieldSecondEnd;
    }

    public void setTimeFirst(long timeFirst) {
        this.timeFirst = timeFirst;
    }

    public void setTimeSecond(long timeSecond) {
        this.timeSecond = timeSecond;
    }
}
