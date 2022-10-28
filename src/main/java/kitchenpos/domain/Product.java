package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class Product {

    private final Long id;
    private final String name;
    private final Price price;

    public Product(final Long id, final String name, final BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = new Price(price);
    }

    public static Product ofNew(final String name, final BigDecimal price) {
        return new Product(null, name, price);
    }

    public static Price calculateTotalPrice(final List<Product> products) {
        final List<Price> prices = products.stream()
                .map(product -> product.price)
                .collect(Collectors.toList());

        return Price.sum(prices);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }
}
