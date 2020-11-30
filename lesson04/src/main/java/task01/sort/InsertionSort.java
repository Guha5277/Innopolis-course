package task01.sort;

import task01.model.Person;

public class InsertionSort extends SortImpl {
    public InsertionSort() {
        super("Insertion Sort");
    }

    @Override
    public Comparable[] sort(Comparable[] persons) {
        if (persons.length == 1) return persons;

        long startTime = System.currentTimeMillis();

        Comparable[] result = new Comparable[persons.length];
        System.arraycopy(persons, 0, result, 0, persons.length);

        for (int i = 1; i < result.length; i++) {
            Comparable key = result[i];
            int j = i - 1;
            while (j >= 0 && (result[j].compareTo(key) > 0)) {
                result[j + 1] = result[j];
                j--;
            }
            result[j + 1] = key;
        }

        showInfo(startTime, result.length);
        return result;
    }
}
