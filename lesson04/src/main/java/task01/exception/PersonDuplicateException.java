package task01.exception;

import task01.model.Person;

public class PersonDuplicateException extends RuntimeException {
    public PersonDuplicateException (Person p1, Person p2){

    }
}
