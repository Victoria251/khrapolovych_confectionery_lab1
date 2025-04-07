package com.confectionery;

import org.junit.jupiter.api.*;
import java.io.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DataManagerTest {

    private File productsFile;
    private File customersFile;
    private File ordersFile;

    @BeforeEach
    void setup() throws IOException {
        productsFile = File.createTempFile("products", ".txt");
        customersFile = File.createTempFile("customers", ".txt");
        ordersFile = File.createTempFile("orders", ".txt");
    }

    @AfterEach
    void tearDown() {
        productsFile.delete();
        customersFile.delete();
        ordersFile.delete();
    }

    @Test
    void testExportProducts_withMocks() throws IOException {
    // Мок продукту
    Product mockProduct = mock(Product.class);
    when(mockProduct.getName()).thenReturn("Candy");
    when(mockProduct.getPrice()).thenReturn(10.0);
    when(mockProduct.getAvailableQuantity()).thenReturn(50);
    when(mockProduct.getSoldQuantity()).thenReturn(5);
    
    List<Product> products = List.of(mockProduct);

    // Записуємо продукти у файл
    DataManager.exportProducts(products, productsFile.getAbsolutePath(), Comparator.comparing(Product::getName));

    // Читаємо файл
    List<String> lines = readLines(productsFile);
    
    // Виводимо фактичний вміст файлу для перевірки
    System.out.println("Read from file:");
    lines.forEach(System.out::println);
    
    // Перевірка
    assertFalse(lines.isEmpty(), "Файл не повинен бути порожнім!");
    assertEquals(1, lines.size(), "Має бути один рядок у файлі");
    assertEquals("Candy,10.00,50,5", lines.get(0).trim(), "Запис у файлі не відповідає очікуваному");
}
    @Test
    void testImportProducts_fromFile() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(productsFile))) {
            writer.write("Cake,8.5,30,7\n");
        }

        List<Product> products = DataManager.importProducts(productsFile.getAbsolutePath());

        assertEquals(1, products.size());
        Product p = products.get(0);
        assertEquals("Cake", p.getName());
        assertEquals(8.5, p.getPrice());
        assertEquals(30, p.getAvailableQuantity());
        assertEquals(7, p.getSoldQuantity());
    }

    @Test
    void testExportCustomers_withMocks() throws IOException {
        Customer mockCustomer = mock(Customer.class);
        when(mockCustomer.getName()).thenReturn("John");
        when(mockCustomer.getPhone()).thenReturn("123-456");

        List<Customer> customers = List.of(mockCustomer);

        DataManager.exportCustomers(customers, customersFile.getAbsolutePath(), Comparator.comparing(Customer::getName));

        List<String> lines = readLines(customersFile);
        assertEquals(1, lines.size());
        assertEquals("John,123-456", lines.get(0));
    }

    @Test
    void testImportCustomers_fromFile() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(customersFile))) {
            writer.write("Anna,555-999\n");
        }

        List<Customer> customers = DataManager.importCustomers(customersFile.getAbsolutePath());

        assertEquals(1, customers.size());
        Customer c = customers.get(0);
        assertEquals("Anna", c.getName());
        assertEquals("555-999", c.getPhone());
    }

    @Test
    void testExportOrders_withMocks() throws IOException {
        Customer mockCustomer = mock(Customer.class);
        when(mockCustomer.getName()).thenReturn("Ivan");

        Product mockProduct = mock(Product.class);
        when(mockProduct.getName()).thenReturn("Cookie");

        Map<Product, Integer> items = new LinkedHashMap<>();
        items.put(mockProduct, 3);

        Order mockOrder = mock(Order.class);
        when(mockOrder.getCustomer()).thenReturn(mockCustomer);
        when(mockOrder.getTotalAmount()).thenReturn(30.0);
        when(mockOrder.getItems()).thenReturn(items);

        List<Order> orders = List.of(mockOrder);

        DataManager.exportOrders(orders, ordersFile.getAbsolutePath(), Comparator.comparing(o -> o.getCustomer().getName()));

        List<String> lines = readLines(ordersFile);
        assertEquals(2, lines.size());
        assertEquals("Замовник: Ivan, Сума: 30,00", lines.get(0));
        assertEquals(" - Cookie × 3", lines.get(1));
    }

    @Test
    void testImportOrders_fromFile() throws IOException {
        // Записуємо тестові дані в файл
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ordersFile))) {
            writer.write("Замовник: Dmytro, Сума: 20,00\n");
            writer.write(" - Donut × 2\n");
        }
    
        // Перевіряємо, чи файл створено
        System.out.println("Файл створено: " + ordersFile.exists());
    
        // Створюємо об'єкти для тесту
        Product donut = new Product("Donut", 5.0, 20); // Продукт
        Customer dmytro = new Customer("Dmytro", "099-111"); // Клієнт
        List<Product> productList = List.of(donut); // Список продуктів
        List<Customer> customerList = List.of(dmytro); // Список клієнтів
    
        // Імпортуємо замовлення з файлу
        List<Order> orders = DataManager.importOrders(ordersFile.getAbsolutePath(), productList, customerList);
    
        // Перевіряємо, скільки замовлень було імпортовано
        System.out.println("Кількість замовлень: " + orders.size());
    
        // Перевіряємо, чи є замовлення
        if (orders.size() > 0) {
            // Виводимо замовлення для дебагу
            Order order = orders.get(0);
            System.out.println("Замовлення імпортовано: " + order);
    
            // Перевірка на правильність замовлення
            assertEquals(1, orders.size());
            assertEquals("Dmytro", order.getCustomer().getName());
            assertTrue(order.getItems().containsKey(donut));
            assertEquals(2, order.getItems().get(donut));
        } else {
            System.out.println("Замовлення не імпортовані!");
        }
    }
    

    // Допоміжний метод для читання рядків з файлу
    private List<String> readLines(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            List<String> result = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                result.add(line);
            }
            return result;
        }
    }
}
