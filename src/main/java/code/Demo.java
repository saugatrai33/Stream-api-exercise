package code;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Demo {

    private static final Supplier<List<Product>> getProducts =
            () -> List.of(
                    new Product(0L, "iPhone 13 Pro Max", "Mobile", 999.0),
                    new Product(1L, "Samsung Galaxy Note 9", "Mobile", 899.0),
                    new Product(2L, "One Plus 10 Pro", "Mobile", 799.0),
                    new Product(3L, "Tesla Model Y", "Vehicle", 123456.99),
                    new Product(4L, "MacBook Pro M1 Max", "Laptop", 2999.0),
                    new Product(5L, "Beats Studio", "HeadPhone", 399.0),
                    new Product(6L, "iPad Pro M1 2021", "Tablet", 1399.0)
            );

    private static final Supplier<List<Customer>> getCustomers =
            () -> List.of(
                    new Customer(0L, "Ram", 1),
                    new Customer(1L, "Sita", 2),
                    new Customer(2L, "John", 3),
                    new Customer(3L, "Hari", 5),
                    new Customer(4L, "UnderTaker", 20),
                    new Customer(5L, "Deepak", 10),
                    new Customer(6L, "Nabin", 9),
                    new Customer(7L, "Raju", 3),
                    new Customer(8L, "Saugat", 2),
                    new Customer(9L, "Ram", 1)
            );

    private static final Supplier<List<Order>> getOrders =
            () -> List.of(
                    new Order(0L, "ONGOING", LocalDate.of(2022, 3, 5),
                            LocalDate.of(2022, 3, 17),
                            getProducts
                                    .get()
                                    .stream()
                                    .limit(3)
                                    .collect(Collectors.toList()),
                            getCustomers
                                    .get().get(3)
                    ),
                    new Order(1L, "DELIVERED", LocalDate.of(2022, 3, 9),
                            LocalDate.of(2022, 3, 17),
                            getProducts
                                    .get()
                                    .stream()
                                    .limit(2)
                                    .collect(Collectors.toList()),
                            getCustomers
                                    .get().get(0)
                    ),
                    new Order(2L, "PICKUP", LocalDate.of(2022, 2, 28),
                            LocalDate.of(2022, 3, 17),
                            getProducts
                                    .get()
                                    .stream()
                                    .limit(5)
                                    .collect(Collectors.toList()),
                            getCustomers
                                    .get().get(3)
                    ),
                    new Order(3L, "DELIVERED", LocalDate.now(),
                            LocalDate.of(2022, 3, 17),
                            getProducts
                                    .get()
                                    .stream()
                                    .limit(5)
                                    .collect(Collectors.toList()),
                            getCustomers
                                    .get().get(2)
                    ),
                    new Order(4L, "ONGOING", LocalDate.of(2022, 1, 1),
                            LocalDate.of(2022, 3, 17),
                            getProducts
                                    .get()
                                    .stream()
                                    .limit(4)
                                    .collect(Collectors.toList()),
                            getCustomers
                                    .get().get(1)
                    )
            );

    private static final Function<List<Order>, List<Order>> ordersByCat =
            orders -> orders
                    .stream()
                    .filter(order -> order.getProducts()
                            .stream()
                            .distinct()
                            .anyMatch(product -> product.getCategory().equalsIgnoreCase("Laptop")))
                    .collect(Collectors.toList());

    private static final Function<List<Product>, List<Product>> mobileCatPGT500 =
            products -> products
                    .stream()
                    .filter(product -> product.getCategory().equalsIgnoreCase("Mobile"))
                    .filter(product -> product.getPrice() > 500)
                    .collect(Collectors.toList());

    private static final Function<List<Product>, Map<String, List<String>>> productsByCat =
            products -> products
                    .stream()
                    .collect(
                            Collectors.groupingBy(
                                    Product::getCategory,
                                    Collectors.mapping(Product::getName, Collectors.toList())
                            )
                    );

    private static final Function<List<Product>, Map<String, Optional<Product>>> expensiveProductByCat =
            products -> products
                    .stream()
                    .collect(
                            Collectors.groupingBy(
                                    Product::getCategory,
                                    Collectors.maxBy(Comparator.comparing(Product::getPrice))
                            )
                    );

    private static final Function<List<Product>, List<Product>> mobilesWith10PercentDiscount =
            products -> products
                    .stream()
                    .filter(product -> product.getCategory().equalsIgnoreCase("Mobile"))
                    .map(product -> {
                        var netPrice = product.getPrice() - product.getPrice() * 10 / 100;
                        return new Product(product.getId(), product.getName(), product.getCategory(), netPrice);
                    })
                    .collect(Collectors.toList());

    private static final Function<List<Order>, List<Product>> exercise4 =
            orders -> orders.stream()
                    .filter(order -> order.getCustomer().getTier() == 2)
                    .filter(order -> order.getOrderDate().compareTo(LocalDate.of(2022, 2, 1)) >= 0)
                    .filter(order -> order.getOrderDate().compareTo(LocalDate.of(2022, 4, 1)) <= 0)
                    .flatMap(order -> order.getProducts().stream())
                    .distinct()
                    .collect(Collectors.toList());

    private static final Function<List<Product>, Optional<Product>> cheapestMobilePhone =
            products -> products.stream()
                    .filter(product -> product.getCategory().equalsIgnoreCase("Mobile"))
                    .min(Comparator.comparing(Product::getPrice));

    private static final Function<List<Order>, List<Order>> top3Orders =
            orders -> orders.stream()
                    .sorted(Comparator.comparing(Order::getOrderDate).reversed())
                    .limit(3)
                    .collect(Collectors.toList());

    private static final Function<List<Order>, Double> totalLump =
            orders -> orders.stream()
                    .filter(order -> order.getOrderDate().compareTo(LocalDate.of(2022, 2, 1)) >= 0)
                    .filter(order -> order.getOrderDate().compareTo(LocalDate.of(2022, 3, 1)) < 0)
                    .peek(System.out::println)
                    .flatMap(order -> order.getProducts().stream())
                    .mapToDouble(Product::getPrice)
                    .sum();

    private static final Function<List<Order>, OptionalDouble> avgPayments =
            orders -> orders.stream()
                    .filter(order -> order.getOrderDate().isEqual(LocalDate.of(2022, 3, 9)))
                    .peek(System.out::println)
                    .flatMap(order -> order.getProducts().stream())
                    .mapToDouble(Product::getPrice)
                    .average();

    private static final Function<List<Product>, DoubleSummaryStatistics> productStats =
            products -> products.stream()
                    .filter(product -> product.getCategory().equalsIgnoreCase("Mobile"))
                    .mapToDouble(Product::getPrice)
                    .summaryStatistics();

    private static Function<List<Order>, List<Product>> specificDayOrderProducts =
            orders -> orders.stream()
                    .filter(order -> order.getOrderDate().isEqual(LocalDate.of(2022, 1, 1)))
                    .peek(System.out::println)
                    .flatMap(order -> order.getProducts().stream())
                    .distinct()
                    .collect(Collectors.toList());

    private static Function<List<Order>, Map<Long, Integer>> exercise11 =
            orders -> orders.stream()
                    .collect(
                            Collectors.toMap(
                                    Order::getId,
                                    order -> order.getProducts().size()
                            )
                    );

    private static Function<List<Order>, Map<Customer, List<Order>>> ordersByCustomer =
            orders -> orders.stream()
                    .collect(
                            Collectors.groupingBy(
                                    Order::getCustomer
                            )
                    );

    private static Function<List<Order>, Map<Order, Integer>> ex13 =
            orders -> orders.stream()
                    .collect(
                            Collectors.toMap(
                                    Function.identity(),
                                    order -> order.getProducts().size()
                            )
                    );

    public static void main(String[] args) {

        // #1 list of mobile phone price greater than $500
//        mobileCatPGT500.apply(getProducts.get())
//                .forEach(System.out::println);

        // #2 orders list with cat laptop
//        ordersByCat.apply(getOrders.get())
//                .forEach(System.out::println);

        // #3 'Mobile' category products with 10% discount
//        mobilesWith10PercentDiscount.apply(getProducts.get())
//                .forEach(System.out::println);

        // #4 product list order by customer of tier 2 between 01-Feb-2022 and 01-Apr-2022
//        exercise4.apply(getOrders.get());

        // #5 cheapest mobile phone
//        cheapestMobilePhone.apply(getProducts.get())
//                .ifPresent(System.out::println);

        // #6 most recent placed order
//        top3Orders.apply(getOrders.get())
//                .forEach(System.out::println);

        // #7 orders list on 1-1-2022, log the records and return product list.
//        specificDayOrderProducts.apply(getOrders.get())
//                .forEach(System.out::println);

        // #8 Exercise 8 — Calculate total lump sum of all orders placed in Feb 2022
        // calculate total sum of prices of products that are placed in feb 2022
//        Double lump = totalLump.apply(getOrders.get());
//        System.out.println(lump);

        // #9 Calculate order average payment placed on 9-Mar-2022
//        Optional.ofNullable(avgPayments.apply(getOrders.get()))
//                .ifPresent(System.out::println);

        // Exercise 10 — Obtain a collection of statistic figures (i.e. sum, average, max, min, count) for all products of category “Mobile”
//        DoubleSummaryStatistics statistics = productStats.apply(getProducts.get());
//        System.out.printf("sum = %1$f, average = %2$f, max=%3$f, min = %4$f, count = %5$d%n",
//                statistics.getSum(), statistics.getAverage(), statistics.getMax(), statistics.getMin(), statistics.getCount());

        // Exercise 11 — Obtain a data map with order id and order’s product count
//        exercise11.apply(getOrders.get())
//                .forEach(
//                        (key, value) ->
//                                System.out.printf("id=%1$d, product_count=%2$d\n", key, value)
//                );

        // Exercise 12 — Produce a data map with order records grouped by customer
//        ordersByCustomer.apply(getOrders.get())
//                .forEach((key, value) -> {
//                            System.out.println("customer=" + key.getName());
//                            System.out.println("orders");
//                            value.forEach(System.out::println);
//                            System.out.println();
//                        }
//                );

        // Exercise 13 — Produce a data map with order record and product total sum
//        ex13.apply(getOrders.get())
//                .forEach((k, v) -> System.out.println("order=" + k.getId() + ", product_size=" + v));

        // Exercise 14 — Obtain a data map with list of product name by category
//        productsByCat.apply(getProducts.get())
//                .forEach((cat, products) -> {
//                    System.out.println(cat);
//                    products.forEach(System.out::println);
//                    System.out.println();
//                });

        // Exercise 15 — Get the most expensive product by category
//        expensiveProductByCat.apply(getProducts.get())
//                .forEach((cat, product) -> {
//                    System.out.println(cat);
//                    Optional.ofNullable(product).ifPresent(System.out::println);
//                    System.out.println();
//                });
    }
}
