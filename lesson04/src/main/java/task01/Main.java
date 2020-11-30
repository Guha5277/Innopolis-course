package task01;

import task01.model.Person;
import task01.sort.BubbleSort;
import task01.sort.InsertionSort;
import task01.sort.Sort;
import task01.utils.DuplicatesSearcher;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Person[] persons = new Person[100];
        for (int i = 0; i < persons.length; i++) {
            persons[i] = Person.gerRandomInstance();
        }

        //Print array to console before sort
//        for (Person p : persons) {
//            System.out.println(p);
//        }

        long startTime = System.currentTimeMillis();
        Comparable[] sortedList = new Comparable[persons.length];
        System.arraycopy(persons, 0, sortedList, 0, persons.length);
        Arrays.sort(persons);
        System.out.println("--------Integrated Sort--------");
        System.out.println("Array length: " + persons.length);
        long time = System.currentTimeMillis() - startTime;
        System.out.println("Time: " + time + "ms");
        System.out.println("------------------------------\n");

        Sort sorter = new InsertionSort();
        Comparable[] sortedList2 = sorter.sort(persons);

        sorter = new BubbleSort();
        Comparable[] sortedList3 = sorter.sort(persons);

        //Print array to console after sort
//        for (Person p : persons) {
//            System.out.println(p);
//        }

        //Search for duplicates
        //DuplicatesSearcher.search(persons);
    }
}
