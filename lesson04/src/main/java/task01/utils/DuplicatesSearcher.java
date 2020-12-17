package task01.utils;

import task01.exception.PersonDuplicateException;
import task01.model.Person;

import java.util.*;
import java.util.stream.Collectors;

public class DuplicatesSearcher {
    public static void search(Person[] persons) {
        Set<Person> personSet = new HashSet<>(Arrays.asList(persons));
        List<Person> personArrayList = new ArrayList<>(Arrays.asList(persons));
        List<Person> duplicates;

        duplicates = personSet.stream()
                .filter(person -> Collections.frequency(personArrayList, person) > 1)
                .peek(person -> {
                    try {
                        throw new PersonDuplicateException(person, person);
                    } catch (PersonDuplicateException e) {
                        System.out.println(e.getMessage());
                    }
                }).collect(Collectors.toList());

        System.out.println("Total duplicates items: " + duplicates.size());
    }
}
