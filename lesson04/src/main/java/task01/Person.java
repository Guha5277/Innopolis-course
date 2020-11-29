package task01;

public class Person implements Comparable<Person> {
    private Integer age;
    private Sex sex;
    private String name;

    public Person(Integer age, Sex sex, String name) {
        this.age = age;
        this.sex = sex;
        this.name = name;
    }

    @Override
    public int compareTo(Person person) {
        int result = 0;
        if (this == person) return result;

        result = sex.compareTo(person.getSex());
        if (result != 0) return result;

        result = age.compareTo(person.getAge());
        if (result != 0) return result;
        
        return name.compareTo(person.getName());
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
}
