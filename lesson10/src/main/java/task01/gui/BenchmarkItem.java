package task01.gui;

import task01.core.GameField;

class BenchmarkItem {
    private boolean isFirstRun = true;
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

    public boolean isFirstRun() {
        return isFirstRun;
    }

    public void setFirstRun(boolean firstRun) {
        isFirstRun = firstRun;
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
