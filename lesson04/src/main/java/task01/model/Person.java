package task01.model;

import task01.utils.ArraysOfNames;

import java.util.Objects;
import java.util.Random;

public class Person implements Comparable<Person> {
    private int age;
    private Sex sex;
    private String name;

    public Person(int age, Sex sex, String name) {
        this.age = age;
        this.sex = sex;
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public Sex getSex() {
        return sex;
    }

    public String getName() {
        return name;
    }

    public static Person gerRandomInstance() {
        Random random = new Random();
        int age = random.nextInt(99);
        Sex sex = random.nextBoolean() ? Sex.MAN : Sex.WOMAN;
        String name = (sex == Sex.MAN) ? ArraysOfNames.getRandomMaleName() : ArraysOfNames.getRandomFemaleName();

        return new Person(age, sex, name);
    }

    @Override
    public int compareTo(Person person) {
        int result = 0;
        if (this.equals(person)) return result;

        result = sex.compareTo(person.getSex());
        if (result != 0) return result;

        result = Integer.compare(person.getAge(), age);
        if (result != 0) return result;

        return name.compareTo(person.getName());
    }

    @Override
    public String toString() {
        char spaceChar = '_';
        StringBuilder sb = new StringBuilder();

        sb.append("Sex:");
        int space = (sex == Sex.MAN) ? 4 : 2;
        while (space > 0) {
            sb.append(spaceChar);
            space--;
        }
        sb.append(sex);

        sb.append(" | Age:");
        space = ((age / 10) >= 1) ? 4 : 5;
        while (space > 0) {
            sb.append(spaceChar);
            space--;
        }
        sb.append(age);

        sb.append(" | Name:");
        space = 12 - name.length();
        while (space > 0) {
            sb.append(spaceChar);
            space--;
        }
        sb.append(name);

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return age == person.age &&
                Objects.equals(sex, person.sex) &&
                Objects.equals(name, person.name);
    }
}
