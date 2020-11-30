package task01.sort;

import task01.model.Person;

public class BubbleSort extends SortImpl {
    public BubbleSort() {
        super("Bubble Sort");
    }

    @Override
    public Comparable[] sort(Comparable[] persons) {
        if (persons.length == 1) return persons;

        long startTime = System.currentTimeMillis();

        Comparable[] result = new Comparable[persons.length];
        System.arraycopy(persons, 0, result, 0, persons.length);

        for (int i = result.length - 1; i > 1; i--) {
            boolean swapped = false;
            for (int j = 0; j < i; j++) {
                int compRes = result[j].compareTo(result[j + 1]);
                if (compRes > 0) {
                    swapped = true;
                    Comparable temp = result[j];
                    result[j] = result[j + 1];
                    result[j + 1] = temp;
                }
            }
            if (!swapped) break;
        }

        showInfo(startTime, result.length);

        return result;
    }
}
