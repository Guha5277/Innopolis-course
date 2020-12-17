import java.time.LocalDate;

public class SomeObject {
    private boolean aBoolean = true;
    private byte aByte = 100;
    private int anInt = 1;
    private char aChar = 'j';
    private double aDouble = 10.0;
    private String str = "abc";
    private LocalDate date = LocalDate.now();

    public boolean getBoolean() {
        return aBoolean;
    }

    public byte getByte() {
        return aByte;
    }

    public int getAnInt() {
        return anInt;
    }

    public char getChar() {
        return aChar;
    }

    public double getDouble() {
        return aDouble;
    }

    public String getStr() {
        return str;
    }

    public LocalDate getDate() {
        return date;
    }
}
