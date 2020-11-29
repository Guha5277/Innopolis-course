package task01;

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

    @Override
    public int compareTo(Sex compSex) {
        if (sex.equals(compSex.getSex())) return 0;
        return (sex.equals(MAN)) ? -1 : 1;
    }
}
