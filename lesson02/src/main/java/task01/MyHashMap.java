package task01;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MyHashMap<K, V> implements Map<K, V> {
    private final int CAPACITY = 16;
    private Node[] table = new Node[CAPACITY];

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public V get(Object key) {
        return null;
    }

    @Override
    public V put(K key, V value) {
        int index = key.hashCode() & (CAPACITY - 1);
        Node<K, V> oldNode = table[index];
        Node<K, V> newNode = new Node<>(key, value);

        if (oldNode == null) {
            table[index] = newNode;
        } else {
            do {
                K oldKey = oldNode.getKey();
                if (oldKey.hashCode() == key.hashCode()
                        && oldKey.equals(key)) {
                    V oldValue = oldNode.getValue();
                    oldNode.setValue(value);
                    return oldValue;
                }
                if (oldNode.nextNode == null) {
                    oldNode.setNextNode(newNode);
                    break;
                }
                oldNode = oldNode.nextNode();

            } while (true);
        }
        return null;
    }

    @Override
    public V remove(Object key) {
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<K> keySet() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }

    private int hash() {
        return 0;
    }

    private class Node<K, V> {
        private Node<K, V> nextNode;
        private K key;
        private V value;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public Node<K, V> nextNode() {
            return nextNode;
        }

        public void setNextNode(Node<K, V> nextNode) {
            this.nextNode = nextNode;
        }
    }
}
