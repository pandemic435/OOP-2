package ua.khpi.oop.havriushenko16;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        try {
            // Забезпечуємо коректне відображення кирилиці в консолі
            System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        } catch (Exception e) {
            System.err.println("Не вдалося встановити UTF-8: " + e.getMessage());
        }

        System.out.println("--- 1. Демонстрація функціональності контейнера (Вимога 9.4) ---");

        // Створення контейнера
        MyLinkedList<AddressBookRecord> addressBook = new MyLinkedList<>();
        System.out.println("Створено порожній контейнер. Чи порожній? " + addressBook.isEmpty());

        // Створення domain-об'єктів (з кирилицею)
        AddressBookRecord rec1 = new AddressBookRecord(
                "Іваненко Іван Іванович",
                LocalDate.of(1990, 5, 15),
                "м. Київ, вул. Хрещатик, 1");
        rec1.addPhone("+380 (44) 123-45-67");
        rec1.addPhone("+380 (50) 111-22-33");

        AddressBookRecord rec2 = new AddressBookRecord(
                "Петренко Петро Петрович",
                LocalDate.of(1985, 10, 20),
                "м. Львів, пл. Ринок, 10");
        rec2.addPhone("+380 (32) 987-65-43");

        AddressBookRecord rec3 = new AddressBookRecord(
                "Сидоренко Сидір Сидорович",
                LocalDate.of(2000, 1, 30),
                "м. Одеса, вул. Дерибасівська, 5");

        // Додавання елементів
        addressBook.add(rec1);
        addressBook.add(rec2);
        addressBook.add(rec3);
        System.out.println("\nДодано 3 елементи. Розмір: " + addressBook.size());

        // Перетворення у рядок (toString)
        System.out.println("\nВміст контейнера (toString):\n" + addressBook);

        // Демонстрація Iterable (Вимога 9.2)
        System.out.println("\nПеревірка Iterable (for-each):");
        for (AddressBookRecord record : addressBook) {
            System.out.println("  - " + record.getFullName());
        }

        // Перевірка на наявність (contains)
        System.out.println("\nПеревірка наявності (contains):");
        System.out.println("  - Чи містить 'Петренко'? " + addressBook.contains(rec2));
        System.out.println("  - Чи містить null? " + addressBook.contains(null));

        // Видалення елементів
        System.out.println("\nВидалення елемента 'Петренко':");
        boolean removed = addressBook.remove(rec2);
        System.out.println("  - Елемент видалено: " + removed);
        System.out.println("  - Новий розмір: " + addressBook.size());
        System.out.println("  - Новий вміст:\n" + addressBook);

        // Перетворення у масив (toArray)
        System.out.println("\nПеретворення у масив (toArray):");
        Object[] array = addressBook.toArray();
        System.out.println("  - " + Arrays.toString(array));

        // Очищення контейнера
        System.out.println("\nОчищення контейнера (clear):");
        addressBook.clear();
        System.out.println("  - Розмір після очищення: " + addressBook.size());
        System.out.println("  - Чи порожній? " + addressBook.isEmpty());
        System.out.println("  - Вміст: " + addressBook);

        System.out.println("\n\n--- 2. Демонстрація збереження та відновлення (Вимога 9.3) ---");

        // Відновимо дані для збереження
        addressBook.add(rec1);
        addressBook.add(rec3);
        System.out.println("Відновлено 2 записи для тестування серіалізації:\n" + addressBook);

        // 9.3.a: Стандартна серіалізація
        String serializedFile = "addressbook.ser";
        try {
            System.out.println("\nЗбереження через стандартну серіалізацію в " + serializedFile + "...");
            PersistenceService.saveSerialized(addressBook, serializedFile);

            System.out.println("Відновлення зі " + serializedFile + "...");
            MyLinkedList<AddressBookRecord> loadedBook1 = PersistenceService.loadSerialized(serializedFile);
            System.out.println("Відновлено " + loadedBook1.size() + " записів:");
            System.out.println(loadedBook1);
        } catch (Exception e) {
            System.err.println("Помилка стандартної серіалізації: " + e.getMessage());
            e.printStackTrace();
        }

        // 9.3.b: Власна серіалізація (без протоколу)
        String customFile = "addressbook.txt";
        try {
            System.out.println("\nЗбереження через власний формат в " + customFile + "...");
            // Використовуємо посилання на метод як реалізацію Function<T, String>
            PersistenceService.saveCustom(addressBook, customFile, AddressBookRecord::toDataString);
            System.out.println("Файл " + customFile + " створено.");

            System.out.println("Відновлення з " + customFile + "...");
            // Використовуємо посилання на метод як реалізацію Function<String, T>
            MyLinkedList<AddressBookRecord> loadedBook2 = PersistenceService.loadCustom(customFile, AddressBookRecord::fromDataString);
            System.out.println("Відновлено " + loadedBook2.size() + " записів:");
            System.out.println(loadedBook2);

            // Перевірка, що дані ідентичні
            System.out.println("Чи ідентичні відновлені дані з оригіналом? " + loadedBook2.contains(rec1));
        } catch (Exception e) {
            System.err.println("Помилка власної серіалізації: " + e.getMessage());
            e.printStackTrace();
        }
    }
}