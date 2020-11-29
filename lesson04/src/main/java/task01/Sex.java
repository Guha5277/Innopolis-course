package task01;

public class Sex implements Comparable<Sex>{
    private final String MAN  = "MAN";
    private final String WOMAN  = "WOMAN";

    private final String sex;

    public Sex(String sex) {
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
