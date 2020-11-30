package task01.model;

import java.util.Objects;

public class Sex implements Comparable<Sex>{
    public final static String MAN  = "MAN";
    public final static String WOMAN  = "WOMAN";

    private final String sex;

    public Sex(String sex) {
        if (!sex.equals(MAN) && !sex.equals(WOMAN)) throw new IllegalArgumentException("Wrong sex argument: " + sex);
        this.sex = sex;
    }

    public String getSex() {
        return sex;
    }

    public boolean isMale() {
        return sex.equals(MAN);
    }

    @Override
    public int compareTo(Sex compSex) {
        if (sex.equals(compSex.getSex())) return 0;
        return (sex.equals(MAN)) ? -1 : 1;
    }

    @Override
    public String toString() {
        return isMale() ? MAN : WOMAN;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sex sex1 = (Sex) o;
        return Objects.equals(sex, sex1.sex);
    }
}
