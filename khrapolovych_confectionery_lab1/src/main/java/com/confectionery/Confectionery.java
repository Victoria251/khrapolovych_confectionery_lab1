package com.confectionery;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Клас, що вiдповiдає за управлiння кондитерською:
 * асортимент, клiєнти, замовлення, статистика продажiв.
 */
public class Confectionery {
    private List<Product> products = new ArrayList<>();     // Список усiх виробiв
    private List<Order> orders = new ArrayList<>();         // Список усiх замовлень
    private List<Customer> customers = new ArrayList<>();   // Список усiх замовникiв

    // === Управлiння замовниками (CRUD) ===

    /**
     * Додає нового замовника до списку.
     *
     * @param customer замовник
     */
    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    /**
     * Видаляє замовника зi списку.
     *
     * @param customer замовник
     */
    public void removeCustomer(Customer customer) {
        customers.remove(customer);
    }

    /**
     * Повертає список усiх зареєстрованих замовникiв.
     *
     * @return список замовникiв
     */
    public List<Customer> getCustomers() {
        return customers;
    }

    // === Управлiння виробами (CRUD) ===

    /**
     * Додає новий вирiб до асортименту.
     *
     * @param product вирiб
     */
    public void addProduct(Product product) {
        products.add(product);
    }

    /**
     * Видаляє вирiб з асортименту.
     *
     * @param product вирiб
     */
    public void removeProduct(Product product) {
        products.remove(product);
    }

    /**
     * Поповнює запас конкретного виробу на задану кiлькiсть.
     *
     * @param product  вирiб
     * @param quantity кiлькiсть для додавання
     */
    public void restockProduct(Product product, int quantity) {
        product.restock(quantity);
    }

    /**
     * Повертає список усiх доступних виробiв (якi ще не розпроданi).
     *
     * @return список доступних виробiв
     */
    public List<Product> getAvailableProducts() {
        return products.stream()
                .filter(p -> p.getAvailableQuantity() > 0)
                .collect(Collectors.toList());
    }

    /**
     * Повертає список виробiв, якi були проданi хоча б один раз.
     *
     * @return список проданих виробiв
     */
    public List<Product> getSoldProducts() {
        return products.stream()
                .filter(p -> p.getSoldQuantity() > 0)
                .collect(Collectors.toList());
    }

    /**
     * Повертає повний список виробiв.
     *
     * @return список усiх виробiв
     */
    public List<Product> getAllProducts() {
        return products;
    }

    // === Оформлення замовлень ===

    /**
     * Оформлює замовлення: зменшує кiлькiсть доступних виробiв, додає замовлення до iсторiї.
     *
     * @param customer   замовник
     * @param orderItems мапа "вирiб -> кiлькiсть"
     * @return об'єкт замовлення
     */
    public Order makeOrder(Customer customer, Map<Product, Integer> orderItems) {
        for (Map.Entry<Product, Integer> entry : orderItems.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            product.sell(quantity);
        }

        Order order = new Order(customer, orderItems);
        orders.add(order);
        return order;
    }

    /**
     * Повертає повну iсторiю замовлень.
     *
     * @return список замовлень
     */
    public List<Order> getOrders() {
        return orders;
    }

    /**
     * Обчислює загальну вартiсть замовлення.
     *
     * @param orderItems мапа "вирiб -> кiлькiсть"
     * @return загальна сума в гривнях
     */
    public double calculateOrderTotal(Map<Product, Integer> orderItems) {
        double total = 0;
        for (Map.Entry<Product, Integer> entry : orderItems.entrySet()) {
            total += entry.getKey().getPrice() * entry.getValue();
        }
        return total;
    }
}
