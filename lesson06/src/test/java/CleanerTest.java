import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CleanerTest {
    SomeObject object;

    @BeforeEach
    void init() {
        object = new SomeObject();
    }

    @Test
    void beforeClean() {
        boolean b = object.getBoolean();
        int i = object.getAnInt();
        byte bt = object.getByte();
        char ch = object.getChar();
        double d = object.getDouble();
        String str = object.getStr();
        LocalDate date = object.getDate();

        assertNotEquals(false, b);
        assertNotEquals(0, i);
        assertNotEquals(0, bt);
        assertNotEquals('\u0000', ch);
        assertNotEquals(0.0, d);
        assertNotEquals(null, str);
        assertNotEquals(null, date);
    }

    @Test
    void clean() {
        Cleaner cleaner = new Cleaner();

        Set<String> fieldsToCleanup = new HashSet<>();
        fieldsToCleanup.add("aByte");
        fieldsToCleanup.add("anInt");
        fieldsToCleanup.add("aChar");
        fieldsToCleanup.add("aBoolean");
        fieldsToCleanup.add("aDouble");
        fieldsToCleanup.add("str");
        fieldsToCleanup.add("date");

        cleaner.cleanup(object, fieldsToCleanup, new HashSet<>());

        boolean b = object.getBoolean();
        int i = object.getAnInt();
        byte bt = object.getByte();
        char ch = object.getChar();
        double d = object.getDouble();
        String str = object.getStr();
        LocalDate date = object.getDate();

        assertFalse(b);
        assertEquals(0, i);
        assertEquals(0, bt);
        assertEquals('\u0000', ch);
        assertEquals(0.0, d);
        assertNull(str);
        assertNull(date);
    }

    @Test
    void expectedExceptionCauseWrongArgs() {
        Cleaner cleaner = new Cleaner();

        Set<String> fieldsToCleanup = new HashSet<>();
        fieldsToCleanup.add("wrongFieldName");

        assertThrows(IllegalArgumentException.class, () -> {
            cleaner.cleanup(object, fieldsToCleanup, new HashSet<>());
        });

        assertThrows(IllegalArgumentException.class, () -> {
            cleaner.cleanup(object, new HashSet<>(), fieldsToCleanup);
        });
    }

    @Test
    void outputTest() {
        Cleaner cleaner = new Cleaner();
        Set<String> outputSet = new HashSet<>();
        Set<String> cleanupSet = new HashSet<>();

        outputSet.add("aBoolean");
        outputSet.add("anInt");
        outputSet.add("aChar");
        outputSet.add("aDouble");
        outputSet.add("date");

        cleaner.cleanup(object, cleanupSet, outputSet);
    }

    @Test
    void mapTest() {
        Map<String, String> map = new HashMap<>();
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        String key4 = "key4";
        String key5 = "key5";

        String val1 = "val1";
        String val2 = "val2";
        String val3 = "val3";
        String val4 = "val4";
        String val5 = "val5";

        map.put(key1, val1);
        map.put(key2, val2);
        map.put(key3, val3);
        map.put(key4, val4);
        map.put(key5, val5);

        Set<String> cleanupSet = new HashSet<>();
        Set<String> outputSet = new HashSet<>();

        cleanupSet.add(key1);
        cleanupSet.add(key2);

        outputSet.add(key3);
        outputSet.add(key4);
        outputSet.add(key5);

        Cleaner cleaner = new Cleaner();
        cleaner.cleanup(map, cleanupSet, outputSet);
    }
}