package core.basesyntax;

import java.util.List;

public class MyLinkedList<T> implements MyLinkedListInterface<T> {
    private Node<T> first;
    private Node<T> last;
    private int size;

    @Override
    public void add(T value) {
        final Node<T> newNode = new Node<>(value);

        if (isEmpty()) {
            first = newNode;
        } else {
            last.next = newNode;
            newNode.prev = last;
        }

        last = newNode;
        size++;
    }

    @Override
    public void add(T value, int index) {
        checkIndexExclusive(index);

        final Node<T> newNode = new Node<>(value);
        final Node<T> nodeAtIndex = getNodeByIndex(index);

        if (isEmpty()) {
            first = newNode;
            last = newNode;
        } else if (index == 0) {
            first.prev = newNode;
            first = newNode;
            newNode.next = first;
        } else if (index == size) {
            last.next = newNode;
            newNode.prev = last;
            last = newNode;
        } else {
            newNode.prev = nodeAtIndex.prev;
            nodeAtIndex.prev.next = newNode;
            nodeAtIndex.prev = newNode;
            newNode.next = nodeAtIndex;
        }

        size++;
    }

    @Override
    public void addAll(List<T> list) {
        if (list == null) {
            return;
        }

        for (T object : list) {
            add(object);
        }
    }

    @Override
    public T get(int index) {
        checkIndex(index);
        final Node<T> node = getNodeByIndex(index);

        return node.value;
    }

    @Override
    public T set(T value, int index) {
        checkIndex(index);

        final Node<T> node = getNodeByIndex(index);
        final T oldValue = node.value;
        node.value = value;

        return oldValue;
    }

    @Override
    public T remove(int index) {
        checkIndex(index);

        final Node<T> node = getNodeByIndex(index);
        unlink(node);
        size--;

        return node.value;
    }

    @Override
    public boolean remove(T object) {
        boolean removed = false;
        Node<T> node = first;

        while (node != null) {
            if (objectsEquals(object, node.value)) {
                unlink(node);
                size--;
                removed = true;
                break;
            }

            node = node.next;
        }

        return removed;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    private void unlink(Node<T> node) {
        if (node.prev != null) {
            node.prev.next = node.next;
        }

        if (node.next != null) {
            node.next.prev = node.prev;
        }

        if (size == 1) {
            last = null;
            first = null;
        } else if (first == node || first != null && first.equals(node)) {
            first.next.prev = null;
            first = first.next;
        } else if (last == node || last != null && last.equals(node)) {
            last.prev.next = null;
            last = last.prev;
        }
    }

    private Node<T> getNodeByIndex(int index) {
        final int middle = index / 2;
        Node<T> node;

        if (index <= middle) {
            node = first;
            for (int i = 0; i < index; i++) {
                node = node.next;
            }
        } else {
            node = last;
            for (int i = size - 1; i > index; i--) {
                node = node.prev;
            }
        }

        return node;
    }

    private boolean objectsEquals(T a, T b) {
        return a == b || a != null && a.equals(b);
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index " + index
                + " out of bounds for size " + size);
        }
    }

    private void checkIndexExclusive(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index " + index
                + " out of bounds for size " + size);
        }
    }

    private static class Node<T> {
        private T value;
        private Node<T> next;
        private Node<T> prev;

        private Node(T value) {
            this.value = value;
        }
    }
}
