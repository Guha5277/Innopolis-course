package task01;

public class Sex implements Comparable<Sex>{
    private final String MAN  = "MAN";
    private final String WOMAN  = "WOMAN";

    private final String sex;

    public Sex(String sex) {
        this.sex = sex;
    }

    @Override
    public int compareTo(Sex o) {
        return 0;
    }
}
