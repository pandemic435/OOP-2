package ua.khpi.oop.havriushenko16;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * Тест для головного класу програми.
 * @author Гаврюшенко Максим
 */
class MainTest {

    @Test
    void testMainExecution() {
        // Просто запускаємо Main. Якщо він відпрацював без помилок — тест пройдено.
        assertDoesNotThrow(() -> {
            Main.main(new String[]{});
        });
    }
}