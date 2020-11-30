package task01.exception;

import task01.model.Person;

public class PersonDuplicateException extends RuntimeException {
    private Person p1;
    private Person p2;

    public PersonDuplicateException(Person p1, Person p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    @Override
    public String getMessage() {
        return "Person: " + p1.toString() + " are duplicated!";
    }
}
