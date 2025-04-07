package com.confectionery;

import java.io.*;
import java.util.*;

public class DataManager {

// Експорт списку виробів у форматі txt
public static void exportProducts(List<Product> products, String filename, Comparator<Product> comparator) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
        // Використовуємо англомовну локаль, щоб число форматувалося з крапкою
        Locale locale = Locale.US; // США використовують крапку для десяткових роздільників
        products.stream()
                .sorted(comparator)
                .forEach(product -> {
                    try {
                        writer.write(String.format(locale, "%s,%.2f,%d,%d%n", product.getName(), product.getPrice(),
                                product.getAvailableQuantity(), product.getSoldQuantity()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    } catch (IOException e) {
        e.printStackTrace();
    }
}


    // Експорт списку замовникiв у форматi txt
    public static void exportCustomers(List<Customer> customers, String filename, Comparator<Customer> comparator) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            customers.stream()
                    .sorted(comparator)
                    .forEach(customer -> {
                        try {
                            writer.write(String.format("%s,%s%n", customer.getName(), customer.getPhone()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Експорт списку замовлень у форматi txt
    public static void exportOrders(List<Order> orders, String filename, Comparator<Order> comparator) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            orders.stream()
                    .sorted(comparator)
                    .forEach(order -> {
                        try {
                            writer.write(String.format("Замовник: %s, Сума: %.2f%n", order.getCustomer().getName(),
                                    order.getTotalAmount()));
                            order.getItems().forEach((product, quantity) -> {
                                try {
                                    writer.write(String.format(" - %s × %d%n", product.getName(), quantity));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Iмпорт виробiв з файлу
    public static List<Product> importProducts(String filename) {
        List<Product> products = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                String name = data[0];
                double price = Double.parseDouble(data[1]);
                int availableQuantity = Integer.parseInt(data[2]);
                int soldQuantity = Integer.parseInt(data[3]);
                products.add(new Product(name, price, availableQuantity));
                products.get(products.size() - 1).setSoldQuantity(soldQuantity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return products;
    }

    // Iмпорт замовникiв з файлу
    public static List<Customer> importCustomers(String filename) {
        List<Customer> customers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                String name = data[0];
                String phone = data[1];
                customers.add(new Customer(name, phone));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return customers;
    }

    // Iмпорт замовлень з файлу
    public static List<Order> importOrders(String filename, List<Product> products, List<Customer> customers) {
        List<Order> orders = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            Customer customer = null;
            Map<Product, Integer> items = new HashMap<>();
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Замовник:")) {
                    if (customer != null && !items.isEmpty()) {
                        orders.add(new Order(customer, items));
                    }
                    String[] data = line.split(": ");
                    String customerName = data[1];
                    customer = customers.stream().filter(c -> c.getName().equals(customerName)).findFirst().orElse(null);
                    items = new HashMap<>();
                } else if (line.startsWith(" - ")) {
                    String[] data = line.substring(3).split(" × ");
                    String productName = data[0];
                    int quantity = Integer.parseInt(data[1]);
                    Product product = products.stream().filter(p -> p.getName().equals(productName)).findFirst().orElse(null);
                    if (product != null) {
                        items.put(product, quantity);
                    }
                }
            }
            if (customer != null && !items.isEmpty()) {
                orders.add(new Order(customer, items));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return orders;
    }
}
