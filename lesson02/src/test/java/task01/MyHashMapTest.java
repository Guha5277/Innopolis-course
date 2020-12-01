package task01;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class MyHashMapTest {
    MyHashMap<String, Object> map;

    @BeforeEach
    void init(){
        map = new MyHashMap<>();
    }

    @Test
    void emptySizeTest() {
        int size = map.size();
        assertEquals(0, size);
    }

    @Test
    void oneElementSizeTest(){
        map.put("Key", new Object());
        int size = map.size();
        assertEquals(1, size);
    }

    @Test
    void randomCountElementsSizeTest(){
        Random random = new Random();
        int expectedCount = random.nextInt(30);
        int index = expectedCount;
        while (index > 0){
            byte[] strBytes = new byte[15];
            random.nextBytes(strBytes);
            map.put(new String(strBytes), new Object());
            index--;
        }
        int realCount = map.size();
        assertEquals(expectedCount, realCount);
    }

    @Test
    void isEmpty() {
        boolean isEmpty = map.isEmpty();
        assertTrue(isEmpty);
    }

    @Test
    void isNotEmpty(){
        map.put("", new Object());
        boolean isEmpty = map.isEmpty();
        assertFalse(isEmpty);
    }

    @Test
    void containsKey() {
        String key = "Key";
        map.put(key, new Object());
        boolean containsKey = map.containsKey(key);
        assertTrue(containsKey);
    }

    @Test
    void containsValue() {
        Object value = new Object();
        map.put("", value);
        boolean containsValue = map.containsValue(value);
        assertTrue(containsValue);
    }

    @Test
    void dontContainsValue(){
        boolean containsValue = map.containsValue(new Object());
        assertFalse(containsValue);
    }

    @Test
    void putAndGet() {
        String key = "key";
        Object value = new Object();
        map.put(key, value);
        Object receivedValue = map.get(key);
        assertEquals(value, receivedValue);
    }

    @Test
    void put() {
        map.put("", new Object());
    }

    @Test
    void remove() {
        String key = "key";
        Object value = new Object();
        map.put(key, value);
        map.remove(key);
    }

    @Test
    void putAll() {

    }

    @Test
    void clear() {

    }

    @Test
    void keySet() {

    }

    @Test
    void values() {

    }

    @Test
    void entrySet() {
    }
}