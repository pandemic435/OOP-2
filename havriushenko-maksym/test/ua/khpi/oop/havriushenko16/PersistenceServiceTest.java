package ua.khpi.oop.havriushenko16;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тести для сервісу збереження даних.
 * @author Гаврюшенко Максим
 */
class PersistenceServiceTest {

    @Test
    void testStandardSerialization() throws Exception {
        MyLinkedList<AddressBookRecord> list = new MyLinkedList<>();
        list.add(new AddressBookRecord("Тест", LocalDate.now(), "Адреса"));

        File tempFile = File.createTempFile("test_ser", ".ser");
        tempFile.deleteOnExit();

        PersistenceService.saveSerialized(list, tempFile.getAbsolutePath());
        MyLinkedList<AddressBookRecord> loaded = PersistenceService.loadSerialized(tempFile.getAbsolutePath());

        assertEquals(1, loaded.size());
        assertEquals("Тест", loaded.iterator().next().getFullName());
    }

    @Test
    void testCustomSerialization() throws Exception {
        MyLinkedList<AddressBookRecord> list = new MyLinkedList<>();
        list.add(new AddressBookRecord("Тест2", LocalDate.now(), "Адреса2"));

        File tempFile = File.createTempFile("test_custom", ".txt");
        tempFile.deleteOnExit();

        PersistenceService.saveCustom(list, tempFile.getAbsolutePath(), AddressBookRecord::toDataString);
        MyLinkedList<AddressBookRecord> loaded = PersistenceService.loadCustom(tempFile.getAbsolutePath(), AddressBookRecord::fromDataString);

        assertEquals(1, loaded.size());
        assertEquals("Тест2", loaded.iterator().next().getFullName());
    }
}