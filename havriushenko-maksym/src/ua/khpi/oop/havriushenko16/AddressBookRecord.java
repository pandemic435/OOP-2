package ua.khpi.oop.havriushenko16;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

// Реалізуємо Serializable для стандартної серіалізації (9.3.a)
public class AddressBookRecord implements Serializable {

    // Необхідно для серіалізації
    private static final long serialVersionUID = 1L;

    private String fullName; // П.І.Б.
    private LocalDate dateOfBirth; // Дата народження
    private MyLinkedList<String> phones; // Телефони (використовуємо наш контейнер)
    private String address; // Адреса
    private LocalDateTime editTimestamp; // Дата і час редагування

    // Форматери для дат (для власної серіалізації)
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final String SEPARATOR = "|"; // Роздільник для полів
    private static final String PHONE_SEPARATOR = ";"; // Роздільник для телефонів

    public AddressBookRecord(String fullName, LocalDate dateOfBirth, String address) {
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.phones = new MyLinkedList<>();
        updateTimestamp(); // Встановлюємо час створення
    }

    // Приватний конструктор для методу fromDataString
    private AddressBookRecord() {
        this.phones = new MyLinkedList<>();
    }

    private void updateTimestamp() {
        this.editTimestamp = LocalDateTime.now();
    }

    // --- Методи для управління ---
    public void addPhone(String phone) {
        this.phones.add(phone);
        updateTimestamp();
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
        updateTimestamp();
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        updateTimestamp();
    }

    public MyLinkedList<String> getPhones() {
        return phones;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        updateTimestamp();
    }

    public LocalDateTime getEditTimestamp() {
        return editTimestamp;
    }

    // --- Перевизначені методи ---

    @Override
    public String toString() {
        // Забезпечуємо коректне відображення кирилиці
        return "Запис{" +
                "ПІБ='" + fullName + '\'' +
                ", Дата народження=" + dateOfBirth +
                ", Телефони=" + phones + // Використає toString() нашого списку
                ", Адреса='" + address + '\'' +
                ", Редаговано=" + editTimestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddressBookRecord record = (AddressBookRecord) o;
        return Objects.equals(fullName, record.fullName) &&
                Objects.equals(dateOfBirth, record.dateOfBirth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullName, dateOfBirth);
    }

    // --- Методи для власної серіалізації (Вимога 9.3.b) ---

    /**
     * Перетворює об'єкт на рядок для збереження у файл.
     */
    public String toDataString() {
        StringBuilder phonesStr = new StringBuilder();
        // Використовуємо наш ітератор (через for-each) для збору телефонів
        for (String phone : phones) {
            phonesStr.append(phone).append(PHONE_SEPARATOR);
        }
        // Видаляємо останній PHONE_SEPARATOR, якщо він є
        if (phonesStr.length() > 0) {
            phonesStr.setLength(phonesStr.length() - PHONE_SEPARATOR.length());
        }

        return String.join(SEPARATOR,
                fullName,
                dateOfBirth.format(DATE_FORMATTER),
                address,
                editTimestamp.format(DATETIME_FORMATTER),
                phonesStr.toString()
        );
    }

    /**
     * Відновлює об'єкт з рядка.
     */
    public static AddressBookRecord fromDataString(String data) {
        String[] parts = data.split("\\" + SEPARATOR);
        if (parts.length < 4) {
            throw new IllegalArgumentException("Некоректний формат даних: " + data);
        }

        AddressBookRecord record = new AddressBookRecord();
        record.fullName = parts[0];
        record.dateOfBirth = LocalDate.parse(parts[1], DATE_FORMATTER);
        record.address = parts[2];
        record.editTimestamp = LocalDateTime.parse(parts[3], DATETIME_FORMATTER);

        // Обробка телефонів (частина 4)
        if (parts.length == 5 && !parts[4].isEmpty()) {
            String[] phoneParts = parts[4].split(PHONE_SEPARATOR);
            for (String phone : phoneParts) {
                record.phones.add(phone);
            }
        }
        return record;
    }
}