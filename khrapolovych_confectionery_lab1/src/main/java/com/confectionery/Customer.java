package com.confectionery;

import java.util.Objects;

/**
 * Клас, що представляє замовника у системi управлiння кондитерською.
 */
public class Customer {
    private String name;     // Iм'я замовника
    private String phone;    // Контактний номер телефону

    /**
     * Конструктор створення нового замовника.
     *
     * @param name  Iм'я замовника
     * @param phone Номер телефону
     */
    public Customer(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    // ===== Геттери та сеттери =====

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // ===== equals та hashCode для iдентифiкацiї клiєнта за iменем i телефоном =====

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer customer)) return false;
        return Objects.equals(name, customer.name) &&
               Objects.equals(phone, customer.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phone);
    }

    // ===== Зручний текстовий вигляд об'єкта =====

    @Override
    public String toString() {
        return String.format("Замовник: %s | Телефон: %s", name, phone);
    }
}
