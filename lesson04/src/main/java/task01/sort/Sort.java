package task01.sort;

import task01.model.Person;

public interface Sort <T extends Comparable>{
    T[] sort(T[] persons);
}
