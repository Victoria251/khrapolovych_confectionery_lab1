package com.confectionery;

import java.util.*;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Confectionery confectionery = new Confectionery();

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            System.out.println("\n=== ГОЛОВНЕ МЕНЮ ===");
            System.out.println("1. Оформити замовлення");
            System.out.println("2. Виготовити вирiб");
            System.out.println("3. Переглянути iнформацiю");
            System.out.println("4. Управлiння замовниками");
            System.out.println("5. Управлiння виробами");
            System.out.println("6. Iмпорт даних");
            System.out.println("7. Експорт даних");
            System.out.println("0. Вихiд");
            System.out.print("Ваш вибiр: ");

            switch (scanner.nextLine()) {
                case "1" -> makeOrder();
                case "2" -> produceProduct();
                case "3" -> viewInformation();
                case "4" -> manageCustomers();
                case "5" -> manageProducts();
                case "6" -> importData();
                case "7" -> exportData();
                case "0" -> running = false;
                default -> System.out.println("Невiрний вибiр. Спробуйте ще раз.");
            }
        }
    }

    private static void makeOrder() {
        System.out.println("\n=== ОФОРМЛЕННЯ ЗАМОВЛЕННЯ ===");
        if (confectionery.getCustomers().isEmpty()) {
            System.out.println("Немає зареєстрованих замовникiв.");
            return;
        }
        if (confectionery.getAvailableProducts().isEmpty()) {
            System.out.println("Немає доступних виробiв для продажу.");
            return;
        }

        System.out.println("Список замовникiв:");
        for (int i = 0; i < confectionery.getCustomers().size(); i++) {
            System.out.println((i + 1) + ". " + confectionery.getCustomers().get(i));
        }
        System.out.print("Оберiть замовника: ");
        int customerIndex = Integer.parseInt(scanner.nextLine()) - 1;
        Customer customer = confectionery.getCustomers().get(customerIndex);

        Map<Product, Integer> orderItems = new HashMap<>();
        boolean adding = true;
        while (adding) {
            System.out.println("\nСписок доступних виробiв:");
            List<Product> available = confectionery.getAvailableProducts();
            for (int i = 0; i < available.size(); i++) {
                System.out.println((i + 1) + ". " + available.get(i));
            }
            System.out.print("Оберiть вирiб або 0 для завершення: ");
            int productIndex = Integer.parseInt(scanner.nextLine()) - 1;
            if (productIndex == -1) break;
            Product product = available.get(productIndex);
            System.out.print("Кiлькiсть: ");
            int quantity = Integer.parseInt(scanner.nextLine());
            orderItems.put(product, quantity);
        }

        double total = confectionery.calculateOrderTotal(orderItems);
        System.out.println("Загальна сума: " + total + " грн");
        Order order = confectionery.makeOrder(customer, orderItems);
        System.out.println("Замовлення створено: " + order);
    }

    private static void produceProduct() {
        System.out.println("\n=== ВИГОТОВЛЕННЯ ВИРОБУ ===");
        if (confectionery.getAllProducts().isEmpty()) {
            System.out.println("Немає жодного виробу для поповнення.");
            return;
        }
        for (int i = 0; i < confectionery.getAllProducts().size(); i++) {
            System.out.println((i + 1) + ". " + confectionery.getAllProducts().get(i));
        }
        System.out.print("Оберiть вирiб: ");
        int index = Integer.parseInt(scanner.nextLine()) - 1;
        Product product = confectionery.getAllProducts().get(index);
        System.out.print("Кiлькiсть для виготовлення: ");
        int quantity = Integer.parseInt(scanner.nextLine());
        confectionery.restockProduct(product, quantity);
        System.out.println("Запас оновлено.");
    }

    private static void viewInformation() {
        System.out.println("\n=== IНФОРМАЦIЯ ===");
        System.out.println("1. Доступнi вироби");
        System.out.println("2. Проданi вироби");
        System.out.println("3. Замовлення");
        System.out.print("Ваш вибiр: ");
        switch (scanner.nextLine()) {
            case "1" -> confectionery.getAvailableProducts().forEach(System.out::println);
            case "2" -> confectionery.getSoldProducts().forEach(System.out::println);
            case "3" -> confectionery.getOrders().forEach(System.out::println);
            default -> System.out.println("Невiрний вибiр.");
        }
    }

    private static void manageCustomers() {
        System.out.println("\n=== ЗАМОВНИКИ ===");
        System.out.println("1. Додати");
        System.out.println("2. Видалити");
        System.out.println("3. Переглянути всiх");
        System.out.print("Ваш вибiр: ");
        switch (scanner.nextLine()) {
            case "1" -> {
                System.out.print("Iм'я: ");
                String name = scanner.nextLine();
                System.out.print("Телефон: ");
                String phone = scanner.nextLine();
                confectionery.addCustomer(new Customer(name, phone));
                System.out.println("Додано.");
            }
            case "2" -> {
                List<Customer> customers = confectionery.getCustomers();
                for (int i = 0; i < customers.size(); i++) {
                    System.out.println((i + 1) + ". " + customers.get(i));
                }
                System.out.print("Оберiть номер для видалення: ");
                int index = Integer.parseInt(scanner.nextLine()) - 1;
                confectionery.removeCustomer(customers.get(index));
                System.out.println("Видалено.");
            }
            case "3" -> confectionery.getCustomers().forEach(System.out::println);
            default -> System.out.println("Невiрний вибiр.");
        }
    }

    private static void manageProducts() {
        System.out.println("\n=== ВИРОБИ ===");
        System.out.println("1. Додати");
        System.out.println("2. Видалити");
        System.out.println("3. Переглянути всi");
        System.out.print("Ваш вибiр: ");
        switch (scanner.nextLine()) {
            case "1" -> {
                System.out.print("Назва: ");
                String name = scanner.nextLine();
                System.out.print("Цiна: ");
                double price = Double.parseDouble(scanner.nextLine());
                System.out.print("Кiлькiсть: ");
                int qty = Integer.parseInt(scanner.nextLine());
                confectionery.addProduct(new Product(name, price, qty));
                System.out.println("Додано.");
            }
            case "2" -> {
                List<Product> products = confectionery.getAllProducts();
                for (int i = 0; i < products.size(); i++) {
                    System.out.println((i + 1) + ". " + products.get(i));
                }
                System.out.print("Оберiть номер для видалення: ");
                int index = Integer.parseInt(scanner.nextLine()) - 1;
                confectionery.removeProduct(products.get(index));
                System.out.println("Видалено.");
            }
            case "3" -> confectionery.getAllProducts().forEach(System.out::println);
            default -> System.out.println("Невiрний вибiр.");
        }
    }

    private static void importData() {
        System.out.println("\n=== IМПОРТ ДАНИХ ===");
        System.out.println("1. Вироби");
        System.out.println("2. Замовники");
        System.out.println("3. Замовлення");
        System.out.println("4. Все");
        System.out.print("Ваш вибiр: ");
        String path;
        switch (scanner.nextLine()) {
            case "1" -> {
                System.out.print("Файл: ");
                path = scanner.nextLine();
                DataManager.importProducts(path).forEach(confectionery::addProduct);
            }
            case "2" -> {
                System.out.print("Файл: ");
                path = scanner.nextLine();
                DataManager.importCustomers(path).forEach(confectionery::addCustomer);
            }
            case "3" -> {
                System.out.print("Файл замовлень: ");
                path = scanner.nextLine();
                List<Order> importedOrders = DataManager.importOrders(path, confectionery.getAllProducts(), confectionery.getCustomers());
                importedOrders.forEach(order -> confectionery.getOrders().add(order));
            }
            case "4" -> {
                System.out.print("Файл виробiв: ");
                String file1 = scanner.nextLine();
                System.out.print("Файл замовникiв: ");
                String file2 = scanner.nextLine();
                System.out.print("Файл замовлень: ");
                String file3 = scanner.nextLine();
                DataManager.importProducts(file1).forEach(confectionery::addProduct);
                DataManager.importCustomers(file2).forEach(confectionery::addCustomer);
                List<Order> imported = DataManager.importOrders(file3, confectionery.getAllProducts(), confectionery.getCustomers());
                imported.forEach(order -> confectionery.getOrders().add(order));
            }
            default -> System.out.println("Невiрний вибiр");
        }
    }

    private static void exportData() {
        System.out.println("\n=== ЕКСПОРТ ДАНИХ ===");
        System.out.println("1. Вироби");
        System.out.println("2. Замовники");
        System.out.println("3. Замовлення");
        System.out.println("4. Все");
        System.out.print("Ваш вибiр: ");
        String path;
        Comparator<Product> pcomp = null;
        Comparator<Customer> ccomp = null;
        Comparator<Order> ocomp = null;
    
        switch (scanner.nextLine()) {
            case "1" -> {
                System.out.println("\nОберiть критерiй сортування для виробiв:");
                System.out.println("1. За назвою");
                System.out.println("2. За цiною");
                System.out.println("3. За кiлькiстю проданих одиниць");
                String sortChoice = scanner.nextLine();
    
                // Вибiр критерiю сортування для виробiв
                switch (sortChoice) {
                    case "1" -> pcomp = Comparator.comparing(Product::getName);
                    case "2" -> pcomp = Comparator.comparing(Product::getPrice);
                    case "3" -> pcomp = Comparator.comparing(Product::getSoldQuantity);
                    default -> System.out.println("Невiрний вибiр сортування.");
                }
    
                System.out.print("Файл: ");
                path = scanner.nextLine();
                if (pcomp != null) {
                    DataManager.exportProducts(confectionery.getAllProducts(), path, pcomp);
                }
            }
            case "2" -> {
                System.out.println("\nОберiть критерiй сортування для замовникiв:");
                System.out.println("1. За iменем");
                String sortChoice = scanner.nextLine();
    
                // Вибiр критерiю сортування для замовникiв
                if (sortChoice.equals("1")) {
                    ccomp = Comparator.comparing(Customer::getName);
                } else {
                    System.out.println("Невiрний вибiр сортування.");
                }
    
                System.out.print("Файл: ");
                path = scanner.nextLine();
                if (ccomp != null) {
                    DataManager.exportCustomers(confectionery.getCustomers(), path, ccomp);
                }
            }
            case "3" -> {
                System.out.println("\nОберiть критерiй сортування для замовлень:");
                System.out.println("1. За сумою замовлення");
                String sortChoice = scanner.nextLine();
    
                // Вибiр критерiю сортування для замовлень
                if (sortChoice.equals("1")) {
                    ocomp = Comparator.comparing(Order::getTotalAmount);
                } else {
                    System.out.println("Невiрний вибiр сортування.");
                }
    
                System.out.print("Файл: ");
                path = scanner.nextLine();
                if (ocomp != null) {
                    DataManager.exportOrders(confectionery.getOrders(), path, ocomp);
                }
            }
            case "4" -> {
                System.out.println("\nОберiть критерiй сортування для виробiв:");
                System.out.println("1. За назвою");
                System.out.println("2. За цiною");
                System.out.println("3. За кiлькiстю проданих одиниць");
                String productSortChoice = scanner.nextLine();
                // Вибiр критерiю сортування для виробiв
                switch (productSortChoice) {
                    case "1" -> pcomp = Comparator.comparing(Product::getName);
                    case "2" -> pcomp = Comparator.comparing(Product::getPrice);
                    case "3" -> pcomp = Comparator.comparing(Product::getSoldQuantity);
                    default -> System.out.println("Невiрний вибiр сортування.");
                }
    
                System.out.println("\nОберiть критерiй сортування для замовникiв:");
                System.out.println("1. За iменем");
                String customerSortChoice = scanner.nextLine();
                // Вибiр критерiю сортування для замовникiв
                if (customerSortChoice.equals("1")) {
                    ccomp = Comparator.comparing(Customer::getName);
                } else {
                    System.out.println("Невiрний вибiр сортування.");
                }
    
                System.out.println("\nОберiть критерiй сортування для замовлень:");
                System.out.println("1. За сумою замовлення");
                String orderSortChoice = scanner.nextLine();
                // Вибiр критерiю сортування для замовлень
                if (orderSortChoice.equals("1")) {
                    ocomp = Comparator.comparing(Order::getTotalAmount);
                } else {
                    System.out.println("Невiрний вибiр сортування.");
                }
    
                // Вибiр файлiв для експорту
                System.out.print("Файл виробiв: ");
                String file1 = scanner.nextLine();
                System.out.print("Файл замовникiв: ");
                String file2 = scanner.nextLine();
                System.out.print("Файл замовлень: ");
                String file3 = scanner.nextLine();
    
                // Експорт усiх даних з вiдповiдними критерiями сортування
                if (pcomp != null) {
                    DataManager.exportProducts(confectionery.getAllProducts(), file1, pcomp);
                }
                if (ccomp != null) {
                    DataManager.exportCustomers(confectionery.getCustomers(), file2, ccomp);
                }
                if (ocomp != null) {
                    DataManager.exportOrders(confectionery.getOrders(), file3, ocomp);
                }
            }
            default -> System.out.println("Невiрний вибiр");
        }
    }
} 
