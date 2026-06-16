package ua.khpi.oop.havriushenko16;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Function;

public class PersistenceService {

    // --- 9.3.a: Стандартна серіалізація ---

    /**
     * Зберігає колекцію за допомогою стандартної серіалізації.
     */
    public static <T extends Serializable> void saveSerialized(MyLinkedList<T> list, String filename)
            throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(list);
        }
    }

    /**
     * Відновлює колекцію за допомогою стандартної серіалізації.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Serializable> MyLinkedList<T> loadSerialized(String filename)
            throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (MyLinkedList<T>) ois.readObject();
        }
    }

    // --- 9.3.b: Не використовуючи протокол серіалізації ---

    /**
     * Зберігає колекцію, використовуючи власний текстовий формат.
     * @param serializer Функція, що перетворює T на рядок.
     */
    public static <T> void saveCustom(MyLinkedList<T> list, String filename, Function<T, String> serializer)
            throws IOException {
        // Використовуємо UTF-8 для коректної роботи з кирилицею
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filename), StandardCharsets.UTF_8)) {
            // Використовуємо наш ітератор
            for (T item : list) {
                writer.write(serializer.apply(item));
                writer.newLine();
            }
        }
    }

    /**
     * Відновлює колекцію з власного текстового формату.
     * @param deserializer Функція, що перетворює рядок на T.
     */
    public static <T> MyLinkedList<T> loadCustom(String filename, Function<String, T> deserializer)
            throws IOException {
        MyLinkedList<T> list = new MyLinkedList<>();
        // Використовуємо UTF-8 для коректної роботи з кирилицею
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filename), StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(deserializer.apply(line));
            }
        }
        return list;
    }
}