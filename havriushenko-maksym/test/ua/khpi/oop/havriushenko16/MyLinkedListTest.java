package ua.khpi.oop.havriushenko16;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Тести для перевірки роботи власного списку MyLinkedList.
 * Покривають основні методи за завданням лабораторної №16.
 * * @author Гаврюшенко Максим
 */
class MyLinkedListTest {

    private MyLinkedList<String> list;

    @BeforeEach
    void setUp() {
        // Створюємо новий пустий список перед кожним тестом
        list = new MyLinkedList<>();
    }

    /**
     * Перевірка додавання елементів та розміру списку
     */
    @Test
    void testAdd() {
        assertTrue(list.isEmpty());
        list.add("test1");
        list.add("test2");
        
        assertFalse(list.isEmpty());
        assertEquals(2, list.size());
    }

    /**
     * Перевірка видалення існуючого та неіснуючого елемента
     */
    @Test
    void testRemove() {
        list.add("A");
        list.add("B");
        list.add("C");

        boolean isRemoved = list.remove("B");
        assertTrue(isRemoved);
        assertEquals(2, list.size());
        assertFalse(list.contains("B"));

        assertFalse(list.remove("X"));
    }

    /**
     * Перевірка роботи з null
     */
    @Test
    void testNullElements() {
        list.add(null);
        list.add("A");
        
        assertTrue(list.contains(null));
        list.remove(null);
        assertFalse(list.contains(null));
        assertEquals(1, list.size());
    }

    /**
     * Перевірка методів clear та contains
     */
    @Test
    void testClearAndContains() {
        list.add("Hello");
        assertTrue(list.contains("Hello"));
        assertFalse(list.contains("World"));
        
        list.clear();
        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
    }

    /**
     * Перевірка конвертації у масив і рядок
     */
    @Test
    void testToArrayAndToString() {
        list.add("1");
        list.add("2");
        
        Object[] arr = list.toArray();
        assertEquals(2, arr.length);
        assertEquals("1", arr[0]);
        
        assertEquals("[1, 2]", list.toString());
        
        list.clear();
        assertEquals("[]", list.toString());
    }

    /**
     * Перевірка ітератора
     */
    @Test
    void testIterator() {
        list.add("first");
        list.add("second");

        Iterator<String> it = list.iterator();
        
        assertTrue(it.hasNext());
        assertEquals("first", it.next());
        assertEquals("second", it.next());
        assertFalse(it.hasNext());

        assertThrows(NoSuchElementException.class, () -> {
            it.next();
        });
    }
}