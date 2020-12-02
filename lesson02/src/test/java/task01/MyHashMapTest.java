package task01;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MyHashMapTest {
    MyHashMap<String, Object> map;
    String key1 = "key1";
    String key2 = "key2";
    String key3 = "key3";
    String key4 = "key4";
    String key5 = "key5";

    Object val1 = new Object();
    Object val2 = new Object();
    Object val3 = new Object();
    Object val4 = new Object();
    Object val5 = new Object();

    @BeforeEach
    void init() {
        map = new MyHashMap<>();
    }

    @Test
    void emptySizeTest() {
        int size = map.size();
        assertEquals(0, size);
    }

    @Test
    void nullKeyTest(){
        map.put(key1, val1);
        map.put(null, val2);
        map.put(key3, val3);
        map.put(null, val4);

        Object result1 = map.get(key1);
        Object result2 = map.get(key3);
        Object result3 = map.get(null);

        assertEquals(val1, result1);
        assertEquals(val3, result2);
        assertEquals(val4, result3);
    }

    @Test
    void nullKeyTestWithGeneratedItems(){
        Random random = new Random();
        List<String> keyWords = new ArrayList<>();

        map.put(null, val1);
        map.put(key1, null);

        for (int i = 0; i < 100; i++) {
            byte[] bytes = new byte[20];
            random.nextBytes(bytes);

            String keyWord = Arrays.toString(bytes);
            keyWords.add(keyWord);

            map.put(keyWord, new Object());
        }

        assertTrue(map.containsKey(null));
        assertTrue(map.containsValue(null));
        assertEquals(val1, map.get(null));

        for (String s : keyWords){
            map.remove(s);
        }
        map.remove(null);
        map.remove(key1);

        for (int i = 0; i < 100; i++) {
            map.put(keyWords.get(0), new Object());
        }

        assertFalse(map.containsKey(null));
        assertFalse(map.containsValue(null));
    }

    @Test
    void oneElementSizeTest() {
        map.put("Key", new Object());
        int size = map.size();
        assertEquals(1, size);
    }

    @Test
    void randomCountElementsSizeTest() {
        Random random = new Random();
        int expectedCount = random.nextInt(300);
        int index = expectedCount;
        while (index > 0) {
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
    void isNotEmpty() {
        map.put("", new Object());
        boolean isEmpty = map.isEmpty();
        assertFalse(isEmpty);
    }

    @Test
    void containsKey() {
        map.put(key1, new Object());
        boolean containsKey = map.containsKey(key1);
        assertTrue(containsKey);
    }

    @Test
    void containsValue() {
        map.put(key1, val1);
        boolean containsValue = map.containsValue(val1);
        assertTrue(containsValue);
    }

    @Test
    void dontContainsValue() {
        boolean containsValue = map.containsValue(new Object());
        assertFalse(containsValue);
    }

    @Test
    void getNullValue() {
        assertNull(map.get(""));
    }

    @Test
    void putAndGet() {
        map.put(key1, val1);
        Object receivedValue = map.get(key1);
        assertEquals(val1, receivedValue);
    }

    @Test
    void put() {
        Object value = new Object();
        Object value2 = new Object();
        Object resultValue = map.put(key1, value);
        assertNull(resultValue);

        resultValue = map.put(key1, value2);
        assertEquals(value, resultValue);
    }

    @Test
    void remove() {
        map.put(key1, val1);
        map.remove(key1);
        assertFalse(map.containsKey(key1));
    }

    @Test
    void putAll() {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put(key1, val1);
        hashMap.put(key2, val2);
        hashMap.put(key3, val3);
        hashMap.put(key4, val4);
        hashMap.put(key5, val5);

        map.putAll(hashMap);

        assertEquals(val1, map.get(key1));
        assertEquals(val2, map.get(key2));
        assertEquals(val3, map.get(key3));
        assertEquals(val4, map.get(key4));
        assertEquals(val5, map.get(key5));
    }

    @Test
    void clear() {
        map.put("key1", new Object());
        map.put("key2", new Object());
        map.put("key3", new Object());
        map.put("key4", new Object());
        map.put("key5", new Object());

        int size = map.size();
        assertEquals(5, size);

        map.clear();
        size = map.size();
        assertEquals(0, size);

    }

    @Test
    void keySet() {
        map.put(key1, val1);
        map.put(key2, val2);
        map.put(key3, val3);
        map.put(key4, val4);

        Set<String> returnedKeySet = map.keySet();
        assertTrue(returnedKeySet.contains(key1));
        assertTrue(returnedKeySet.contains(key2));
        assertTrue(returnedKeySet.contains(key3));
        assertTrue(returnedKeySet.contains(key4));
    }

    @Test
    void values() {
        map.put(key1, val1);
        map.put(key2, val2);
        map.put(key3, val3);
        map.put(key4, val4);

        List<Object> returnedValues = (List) map.values();
        assertTrue(returnedValues.contains(val1));
        assertTrue(returnedValues.contains(val2));
        assertTrue(returnedValues.contains(val3));
        assertTrue(returnedValues.contains(val4));
        assertFalse(returnedValues.contains(val5));
    }

    @Test
    void entrySet() {
        map.put(key1, val1);
        map.put(key2, val2);
        map.put(key3, val3);
        map.put(key4, val4);
        map.put(key5, val5);


        Set<Map.Entry<String, Object>> set = map.entrySet();
        List<String> keyList = new ArrayList<>();
        List<Object> valList = new ArrayList<>();
        for (Map.Entry<String, Object> map : set) {
            keyList.add(map.getKey());
            valList.add(map.getValue());
        }

        assertTrue(keyList.contains(key1));
        assertTrue(keyList.contains(key2));
        assertTrue(keyList.contains(key3));
        assertTrue(keyList.contains(key4));
        assertTrue(keyList.contains(key5));

        assertTrue(valList.contains(val1));
        assertTrue(valList.contains(val2));
        assertTrue(valList.contains(val3));
        assertTrue(valList.contains(val4));
        assertTrue(valList.contains(val5));
    }
}