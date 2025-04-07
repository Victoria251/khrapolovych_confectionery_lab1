package com.confectionery;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class BusinessLogicTest {

    private Confectionery confectionery; // Екземпляр класу Confectionery для тестування
    private Product cake; // Продукт, що представляє торт
    private Product cookie; // Продукт, що представляє печиво
    private Customer customer; // Екземпляр клієнта для оформлення замовлень

    // Метод для ініціалізації об'єктів перед кожним тестом
    @BeforeEach
    public void setUp() {
        confectionery = new Confectionery(); // Створення нового екземпляра Confectionery
        cake = new Product("Торт", 200.0, 10); // Створення об'єкта торт
        cookie = new Product("Печиво", 50.0, 20); // Створення об'єкта печиво
        customer = new Customer("Марiя", "0971234567"); // Створення нового клієнта
    }

    // Тест для додавання та видалення клієнта
    @Test
    public void testAddAndRemoveCustomer() {
        confectionery.addCustomer(customer); // Додаємо клієнта до кондитерської
        assertTrue(confectionery.getCustomers().contains(customer)); // Перевірка, чи клієнт доданий

        confectionery.removeCustomer(customer); // Видаляємо клієнта
        assertFalse(confectionery.getCustomers().contains(customer)); // Перевірка, чи клієнт видалений
    }

    // Тест для додавання та видалення продукту
    @Test
    public void testAddAndRemoveProduct() {
        confectionery.addProduct(cake); // Додаємо торт до кондитерської
        assertTrue(confectionery.getAllProducts().contains(cake)); // Перевірка, чи торт доданий

        confectionery.removeProduct(cake); // Видаляємо торт
        assertFalse(confectionery.getAllProducts().contains(cake)); // Перевірка, чи торт видалений
    }

    // Тест для поповнення запасів продукту
    @Test
    public void testRestockProduct() {
        confectionery.addProduct(cake); // Додаємо торт до кондитерської
        confectionery.restockProduct(cake, 5); // Поповнюємо запас торта на 5 одиниць
        assertEquals(15, cake.getAvailableQuantity()); // Перевірка, чи кількість доступних одиниць правильна
    }

    // Тест для отримання доступних продуктів
    @Test
    public void testGetAvailableProducts() {
        confectionery.addProduct(cake); // Додаємо торт
        confectionery.addProduct(cookie); // Додаємо печиво
        cookie.sell(20); // Продаємо все печиво, щоб воно стало недоступним

        List<Product> available = confectionery.getAvailableProducts(); // Отримуємо доступні продукти
        assertTrue(available.contains(cake)); // Перевірка, чи торт доступний
        assertFalse(available.contains(cookie)); // Перевірка, чи печиво недоступне
    }

    // Тест для отримання проданих продуктів
    @Test
    public void testGetSoldProducts() {
        confectionery.addProduct(cake); // Додаємо торт
        confectionery.addProduct(cookie); // Додаємо печиво
        cookie.sell(5); // Продаємо 5 одиниць печива

        List<Product> sold = confectionery.getSoldProducts(); // Отримуємо продані продукти
        assertTrue(sold.contains(cookie)); // Перевірка, чи печиво є серед проданих
        assertFalse(sold.contains(cake)); // Перевірка, чи торт не продано
    }

    // Тест для створення замовлення
    @Test
    public void testMakeOrder() {
        confectionery.addCustomer(customer); // Додаємо клієнта
        confectionery.addProduct(cake); // Додаємо торт
        confectionery.addProduct(cookie); // Додаємо печиво

        Map<Product, Integer> items = new HashMap<>(); // Створення карти з товарами та їх кількостями
        items.put(cake, 2); // Додаємо 2 торти
        items.put(cookie, 3); // Додаємо 3 печива

        Order order = confectionery.makeOrder(customer, items); // Створення замовлення

        // Перевірка правильності зміни кількості товарів
        assertEquals(2, cake.getSoldQuantity()); // Перевірка кількості проданих тортів
        assertEquals(3, cookie.getSoldQuantity()); // Перевірка кількості проданих печив
        assertEquals(8, cake.getAvailableQuantity()); // Перевірка кількості доступних тортів
        assertEquals(17, cookie.getAvailableQuantity()); // Перевірка кількості доступних печив

        // Перевірка правильності збереження замовлення
        assertEquals(1, confectionery.getOrders().size()); // Перевірка, що в системі лише одне замовлення
        assertEquals(order, confectionery.getOrders().get(0)); // Перевірка, що замовлення додано в список
    }

    // Тест для обчислення загальної суми замовлення
    @Test
    public void testCalculateOrderTotal() {
        Map<Product, Integer> items = new HashMap<>(); // Створення карти з товарами та їх кількостями
        items.put(cake, 1); // Додаємо 1 торт
        items.put(cookie, 2); // Додаємо 2 печива
        double expectedTotal = 200.0 * 1 + 50.0 * 2; // Очікувана сума замовлення

        double total = confectionery.calculateOrderTotal(items); // Обчислюємо загальну суму
        assertEquals(expectedTotal, total); // Перевірка, чи обчислена сума відповідає очікуваній
    }
}
