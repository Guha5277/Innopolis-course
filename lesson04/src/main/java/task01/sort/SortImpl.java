package task01.sort;

public abstract class SortImpl implements Sort {
    private final String sortType;

    public SortImpl(String sortType) {
        this.sortType = sortType;
    }

    protected void showInfo(long startTime, int arrayLength) {
        System.out.println("--------" + sortType + "--------");
        System.out.println("Array length: " + arrayLength);
        long time = System.currentTimeMillis() - startTime;
        System.out.println("Time: " + time + "ms");
        System.out.println("------------------------------\n");
    }
}
