package task01;

import java.util.*;

public class MyHashMap<K, V> implements Map<K, V> {
    private final int CAPACITY = 16;
    private int size;
    private Node<K, V>[] table = new Node[CAPACITY];

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        int index = hash(key);
        if (table[index] == null) {
            return false;
        }

        Node<K, V> node = table[index];
        while (node != null) {

            K currentKey = node.getKey();

            if (currentKey == null && key == null) {
                return true;
            } else if (currentKey == null || key == null) {
                node = node.getNextNode();
                continue;
            }
            if (node.getKey().equals(key)) {
                return true;
            }

            node = node.getNextNode();
        }
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        for (Node<K, V> node : table) {
            if (node != null) {
                while (node != null) {
                    V currentValue = node.getValue();
                    if (currentValue == null && value == null) {
                        return true;
                    } else if (currentValue == null || value == null) {
                        node = node.getNextNode();
                        continue;
                    }
                    if (currentValue.equals(value)) {
                        return true;
                    }
                    node = node.getNextNode();
                }
            }
        }
        return false;
    }

    @Override
    public V get(Object key) {
        int index = hash(key);
        if (table[index] == null) {
            return null;
        }

        Node<K, V> node = table[index];
        while (node != null) {
            K currentKey = node.getKey();

            if (currentKey == null && key == null) {
                return node.getValue();
            } else if (currentKey == null || key == null) {
                node = node.getNextNode();
                continue;
            }
            if ((currentKey.hashCode() == key.hashCode()) && currentKey.equals(key)) {
                return node.getValue();
            }

            node = node.getNextNode();
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        int index = hash(key);
        Node<K, V> oldNode = table[index];
        Node<K, V> newNode = new Node<>(key, value);
        size++;

        if (oldNode == null) {
            table[index] = newNode;
        } else {
            while (oldNode != null) {
                K oldKey = oldNode.getKey();
                if (oldKey == null && key == null) {
                    V oldValue = oldNode.getValue();
                    oldNode.setValue(value);
                    return oldValue;
                } else if (oldKey == null || key == null) {
                    if (oldNode.getNextNode() == null) {
                        oldNode.setNextNode(newNode);
                        break;
                    }
                    oldNode = oldNode.getNextNode();
                    continue;
                }
                if (oldKey.hashCode() == key.hashCode()
                        && oldKey.equals(key)) {
                    V oldValue = oldNode.getValue();
                    oldNode.setValue(value);
                    return oldValue;
                }
                if (oldNode.getNextNode() == null) {
                    oldNode.setNextNode(newNode);
                    break;
                }
                oldNode = oldNode.getNextNode();
            }
        }
        return null;
    }

    private V replaceNode(int index, Node<K, V> prevNode, Node<K, V> currentNode) {
        if (prevNode == null) {
            table[index] = currentNode.getNextNode();
        } else {
            prevNode.setNextNode(currentNode.getNextNode());
        }
        return currentNode.getValue();
    }

    @Override
    public V remove(Object key) {
        int index = hash(key);
        if (table[index] == null) {
            size--;
            return null;
        }

        Node<K, V> currentNode = table[index];
        Node<K, V> prevNode = null;

        while (currentNode != null) {
            K currentKey = currentNode.getKey();
            if (currentKey == null && key == null) {
                size--;
                return replaceNode(index, prevNode, currentNode);
            } else if (currentKey == null || key == null) {
                prevNode = currentNode;
                currentNode = currentNode.getNextNode();
                continue;
            }
            if (currentKey.hashCode() == key.hashCode() && currentKey.equals(key)) {
                size--;
                return replaceNode(index, prevNode, currentNode);
            }
            prevNode = currentNode;
            currentNode = currentNode.getNextNode();
        }
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        if (m.size() > 0) {
            for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
                K key = e.getKey();
                V value = e.getValue();
                put(key, value);
            }
            size += m.size();
        }
    }

    @Override
    public void clear() {
        table = new Node[CAPACITY];
        size = 0;
    }

    @Override
    public Set<K> keySet() {
        Set<K> result = new HashSet<>();

        for (Node<K, V> node : table) {
            if (node != null) {
                do {
                    result.add(node.getKey());
                    node = node.getNextNode();
                } while (node != null);
            }
        }
        return result;
    }

    @Override
    public Collection<V> values() {
        List<V> result = new ArrayList<>();

        for (Node<K, V> node : table) {
            if (node != null) {
                do {
                    result.add(node.getValue());
                    node = node.getNextNode();
                } while (node != null);
            }
        }
        return result;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        HashSet<Map.Entry<K, V>> result = new HashSet<>();

        for (Node<K, V> node : table) {
            if (node != null) {
                do {
                    result.add(new Entry<>(node.getKey(), node.getValue()));
                    node = node.getNextNode();
                } while (node != null);
            }
        }
        return result;
    }

    private int hash(Object key) {
        return (key == null) ? 0 : (key.hashCode() & (CAPACITY - 1));
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

        public Node<K, V> getNextNode() {
            return nextNode;
        }

        public void setNextNode(Node<K, V> nextNode) {
            this.nextNode = nextNode;
        }
    }

    private class Entry<K, V> implements Map.Entry<K, V> {
        private K key;
        private V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }
    }
}
