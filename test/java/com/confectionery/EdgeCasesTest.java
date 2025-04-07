package com.confectionery;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class EdgeCasesTest {

    private Path tempFile;

    @AfterEach
    void cleanup() throws IOException {
        if (tempFile != null && Files.exists(tempFile)) {
            Files.delete(tempFile);
        }
    }

    @Test
    void testImportEmptyFile() throws IOException {
        // Створення порожнього файлу
        tempFile = Files.createTempFile("empty_file", ".txt");

        List<Product> products = DataManager.importProducts(tempFile.toString());
        assertTrue(products.isEmpty(), "Список продуктів має бути порожнім");

        List<Customer> customers = DataManager.importCustomers(tempFile.toString());
        assertTrue(customers.isEmpty(), "Список клієнтів має бути порожнім");

        List<Order> orders = DataManager.importOrders(tempFile.toString(), new ArrayList<>(), new ArrayList<>());
        assertTrue(orders.isEmpty(), "Список замовлень має бути порожнім");
    }

    @Test
    void testImportInvalidProductData() {
    // Створюємо тимчасовий файл з некоректними даними
    String invalidData = "ProductName,abc,10,5\nProduct2,20.5,15,10";
    Path path = Paths.get("invalid_products.txt");
    try {
        Files.write(path, invalidData.getBytes());

        // Перевірка: очікуємо, що з'явиться виключення
        assertThrows(NumberFormatException.class, () -> DataManager.importProducts("invalid_products.txt"));

    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        try {
            Files.delete(path); // Видаляємо тимчасовий файл після тесту
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

    @Test
    void testImportInvalidOrderData() throws IOException {
        // Створення файлу з некоректними даними для замовлень
        String invalidOrderData = "Замовник: John, Сума: abc\n - Cake × 2\n";
        tempFile = Files.createTempFile("invalid_order_data", ".txt");
        Files.write(tempFile, invalidOrderData.getBytes());

        List<Product> products = List.of(new Product("Cake", 10.0, 5)); // Мок продукту
        List<Customer> customers = List.of(new Customer("John", "+380111111111")); // Мок клієнта
        List<Order> orders = DataManager.importOrders(tempFile.toString(), products, customers);

        // Перевірка, що замовлення не були імпортовані через некоректну суму
        assertTrue(orders.isEmpty(), "Список замовлень має бути порожнім через некоректну суму");
    }

    @Test
    void testImportInvalidCustomerData() {
    // Створюємо тимчасовий файл з некоректними даними
    String invalidData = "John Doe,12345\nInvalid Customer,abc123\n";
    Path path = Paths.get("invalid_customers.txt");
    try {
        Files.write(path, invalidData.getBytes());

        // Перевірка: тут можна перевіряти різні варіанти некоректних даних
        List<Customer> customers = DataManager.importCustomers("invalid_customers.txt");
        assertNotNull(customers);
        assertEquals(2, customers.size()); // Має бути два клієнти, навіть якщо телефон некоректний, але без помилок

    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        try {
            Files.delete(path); // Видаляємо тимчасовий файл після тесту
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

        @Test
        void testImportValidProductData() throws IOException {
        // Створення файлу з коректними даними для продуктів
        String validData = "Cake,20.5,100,50\nPie,15.0,30,15\n";
        tempFile = Files.createTempFile("valid_product_data", ".txt");
        Files.write(tempFile, validData.getBytes());

        List<Product> products = DataManager.importProducts(tempFile.toString());

        // Перевірка, що продукти імпортувались коректно
        assertEquals(2, products.size(), "Список продуктів має містити два елементи");

        Product cake = products.get(0);
        assertEquals("Cake", cake.getName(), "Назва першого продукту має бути 'Cake'");
        assertEquals(20.5, cake.getPrice(), 0.01, "Ціна першого продукту має бути 20.5");
        assertEquals(100, cake.getAvailableQuantity(), "Кількість першого продукту на складі має бути 100");
        assertEquals(50, cake.getSoldQuantity(), "Кількість проданих одиниць першого продукту має бути 50");

        Product pie = products.get(1);
        assertEquals("Pie", pie.getName(), "Назва другого продукту має бути 'Pie'");
        assertEquals(15.0, pie.getPrice(), 0.01, "Ціна другого продукту має бути 15.0");
        assertEquals(30, pie.getAvailableQuantity(), "Кількість другого продукту на складі має бути 30");
        assertEquals(15, pie.getSoldQuantity(), "Кількість проданих одиниць другого продукту має бути 15");
    }

    // Тест для коректних даних клієнтів
    @Test
    void testImportValidCustomerData() throws IOException {
        // Створення файлу з коректними даними для клієнтів
        String validData = "John Doe,+123456789\nJane Smith,+987654321\n";
        tempFile = Files.createTempFile("valid_customer_data", ".txt");
        Files.write(tempFile, validData.getBytes());

        List<Customer> customers = DataManager.importCustomers(tempFile.toString());

        // Перевірка, що клієнти імпортувались коректно
        assertEquals(2, customers.size(), "Список клієнтів має містити два елементи");

        Customer john = customers.get(0);
        assertEquals("John Doe", john.getName(), "Ім'я першого клієнта має бути 'John Doe'");
        assertEquals("+123456789", john.getPhone(), "Номер телефону першого клієнта має бути '+123456789'");

        Customer jane = customers.get(1);
        assertEquals("Jane Smith", jane.getName(), "Ім'я другого клієнта має бути 'Jane Smith'");
        assertEquals("+987654321", jane.getPhone(), "Номер телефону другого клієнта має бути '+987654321'");
    }

    @Test
    void testExportEmptyData() throws IOException {
        // Перевірка експорту порожніх списків
        List<Product> emptyProducts = new ArrayList<>();
        List<Customer> emptyCustomers = new ArrayList<>();
        List<Order> emptyOrders = new ArrayList<>();

        tempFile = Files.createTempFile("empty_export", ".txt");

        DataManager.exportProducts(emptyProducts, tempFile.toString(), Comparator.comparing(Product::getName));
        DataManager.exportCustomers(emptyCustomers, tempFile.toString(), Comparator.comparing(Customer::getName));
        DataManager.exportOrders(emptyOrders, tempFile.toString(), Comparator.comparing(o -> o.getCustomer().getName()));

        // Перевірка, що файли порожні
        List<String> productLines = Files.readAllLines(tempFile);
        assertTrue(productLines.isEmpty(), "Файл з продуктами має бути порожнім");

        List<String> customerLines = Files.readAllLines(tempFile);
        assertTrue(customerLines.isEmpty(), "Файл з клієнтами має бути порожнім");

        List<String> orderLines = Files.readAllLines(tempFile);
        assertTrue(orderLines.isEmpty(), "Файл з замовленнями має бути порожнім");
    }
}
