package com.confectionery;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * Клас, що представляє замовлення у системi.
 * Мiстить iнформацiю про замовника, перелiк виробiв i загальну суму.
 */
public class Order {
    private static int idCounter = 0;  // Лiчильник для генерацiї унiкальних iдентифiкаторiв
    private final int id;              // Унiкальний iдентифiкатор замовлення
    private final Customer customer;   // Хто зробив замовлення
    private final Map<Product, Integer> items;  // Що було замовлено
    private final double totalAmount; // Загальна сума замовлення

    /**
     * Конструктор створення нового замовлення.
     *
     * @param customer замовник
     * @param items    мапа вирiб -> кiлькiсть
     */
    public Order(Customer customer, Map<Product, Integer> items) {
        this.id = ++idCounter; // Присвоєння унiкального iдентифiкатора
        this.customer = customer;
        this.items = Map.copyOf(items); // Невимiрювана копiя
        this.totalAmount = calculateTotal();
    }

    /**
     * Розрахунок загальної вартостi замовлення.
     *
     * @return сума в гривнях
     */
    private double calculateTotal() {
        return items.entrySet().stream()
                .mapToDouble(e -> e.getKey().getPrice() * e.getValue())
                .sum();
    }

    // ===== Геттери =====

    public int getId() {
        return id; // Повертає унiкальний iдентифiкатор замовлення
    }

    public Customer getCustomer() {
        return customer;
    }

    public Map<Product, Integer> getItems() {
        return Collections.unmodifiableMap(items);
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    // ===== equals та hashCode для порiвняння замовлень =====

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order order)) return false;
        return Double.compare(order.totalAmount, totalAmount) == 0 &&
                Objects.equals(customer, order.customer) &&
                Objects.equals(items, order.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customer, items, totalAmount);
    }

    // ===== Вивiд замовлення у зручному виглядi =====

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Замовлення вiд " + customer.getName() + " (ID: " + id + "):\n");
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            sb.append("- ").append(entry.getKey().getName())
              .append(" × ").append(entry.getValue())
              .append(" шт.\n");
        }
        sb.append("Сума до сплати: ").append(String.format("%.2f грн", totalAmount));
        return sb.toString();
    }
}
