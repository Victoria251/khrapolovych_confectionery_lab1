package com.confectionery;

import java.util.Objects;

/**
 * Клас, що представляє кондитерський вирiб у системi управлiння кондитерською.
 */
public class Product {
    private String name; // Назва виробу
    private double price; // Цiна за одиницю
    private int availableQuantity; // Кiлькiсть доступних виробiв
    private int soldQuantity; // Кiлькiсть проданих виробiв

    /**
     * Конструктор для створення нового виробу.
     *
     * @param name              Назва виробу
     * @param price             Цiна за одиницю
     * @param availableQuantity Початкова кiлькiсть виробiв на складi
     */
    public Product(String name, double price, int availableQuantity) {
        this.name = name;
        this.price = price;
        this.availableQuantity = availableQuantity;
        this.soldQuantity = 0;
    }

    /**
     * Продаж певної кiлькостi виробiв. Оновлює доступну та продану кiлькiсть.
     *
     * @param quantity Кiлькiсть до продажу
     * @return true — якщо продаж успiшний; false — якщо недостатньо товару
     */
    public boolean sell(int quantity) {
        if (quantity <= 0) return false;

        if (quantity <= availableQuantity) {
            availableQuantity -= quantity;
            soldQuantity += quantity;
            return true;
        }
        return false;
    }

    /**
     * Поповнення кiлькостi виробiв.
     *
     * @param quantity Кiлькiсть, яку потрiбно додати
     */
    public void restock(int quantity) {
        if (quantity > 0) {
            availableQuantity += quantity;
        }
    }

    // ===== Геттери та сеттери =====

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price >= 0) {
            this.price = price;
        }
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public int getSoldQuantity() {
        return soldQuantity;
    }

    public void setSoldQuantity(int soldQuantity) {
        this.soldQuantity = soldQuantity;
    }

    // ===== equals та hashCode для порiвняння об'єктiв та роботи в колекцiях =====

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    // ===== Для зручного виводу iнформацiї про вирiб =====

    @Override
    public String toString() {
        return String.format("Вирiб: %s | Цiна: %.2f грн | Доступно: %d | Продано: %d",
                name, price, availableQuantity, soldQuantity);
    }
    
}

