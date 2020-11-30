package task01.utils;

import task01.exception.PersonDuplicateException;
import task01.model.Person;

import java.util.Arrays;

public class DuplicatesSearcher {
    public static void search(Person[] persons) {
                for (Person person : persons) {
           Arrays.stream(persons)
                    .filter(p -> p.equals(person))
                    .peek(p -> {
                        try {
                            throw new PersonDuplicateException(person, p);
                        } catch (PersonDuplicateException e) {
                            System.out.println(e.getMessage());
                        }
                    })
                    .count();
        }

    }
}
