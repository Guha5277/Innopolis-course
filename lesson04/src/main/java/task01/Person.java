package task01;

public class Person implements Comparable<Person>{
    private int age;
    private Sex sex;
    private String name;

    @Override
    public int compareTo(Person o) {
        return 0;
    }
}
