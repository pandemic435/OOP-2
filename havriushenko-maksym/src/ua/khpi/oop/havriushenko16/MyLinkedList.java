package ua.khpi.oop.havriushenko16;
import java.io.Serializable;
import java.util.Objects;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Власний клас-контейнер на основі зв'язного списку.
 * @param <T> Тип елементів, що зберігаються
 */
public class MyLinkedList<T> implements Iterable<T>, Serializable {

    private static final long serialVersionUID = 2L;

    /**
     * Внутрішній клас, що представляє вузол списку.
     * Він також Serializable.
     */
    private class Node implements Serializable {
        private static final long serialVersionUID = 3L;
        T data;
        Node next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node head; // Початок списку
    private Node tail; // Кінець списку (для швидкого додавання)
    private int size = 0;

    public MyLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * Додає елемент в кінець списку.
     */
    public void add(T item) {
        Node newNode = new Node(item);
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    /**
     * Видаляє перше входження вказаного елемента.
     * @return true, якщо елемент було знайдено та видалено.
     */
    public boolean remove(T item) {
        if (isEmpty()) {
            return false;
        }

        Node current = head;
        Node prev = null;

        while (current != null) {
            // Обробка як null, так і non-null елементів
            if (Objects.equals(current.data, item)) {
                // Елемент знайдено
                if (prev == null) {
                    // Це був перший елемент (head)
                    head = current.next;
                } else {
                    // Це елемент всередині або в кінці
                    prev.next = current.next;
                }

                // Якщо це був останній елемент (tail)
                if (current.next == null) {
                    tail = prev;
                }

                size--;
                return true;
            }
            // Рухаємось далі
            prev = current;
            current = current.next;
        }
        return false; // Елемент не знайдено
    }

    /**
     * Очищує контейнер.
     */
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * Перетворює список у масив.
     */
    public Object[] toArray() {
        Object[] array = new Object[size];
        int index = 0;
        // Використовуємо ітератор (неявно)
        for (T item : this) {
            array[index++] = item;
        }
        return array;
    }

    /**
     * Перевіряє, чи містить список вказаний елемент.
     */
    public boolean contains(T item) {
        for (T data : this) {
            if (Objects.equals(data, item)) {
                return true;
            }
        }
        return false;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Повертає рядкове представлення списку.
     */
    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Node current = head;
        while (current != null) {
            sb.append(current.data);
            if (current.next != null) {
                sb.append(", ");
            }
            current = current.next;
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Реалізація методу iterator() з інтерфейсу Iterable (Вимога 9.2).
     * Це дозволяє використовувати список у циклі for-each.
     */
    @Override
    public Iterator<T> iterator() {
        return new MyIterator();
    }

    /**
     * Внутрішній клас-ітератор.
     */
    private class MyIterator implements Iterator<T> {
        private Node current = head;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T data = current.data;
            current = current.next;
            return data;
        }
    }
}