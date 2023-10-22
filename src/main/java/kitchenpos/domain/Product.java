package kitchenpos.domain;

import java.math.BigDecimal;

public class Product {

    private Long id;
    private String name;
    private Money price;

    public Product(Long id, String name, Money price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Product(String name, Money price) {
        this(null, name, price);
    }

    public Product() {
    }

    public static Product of(String name, BigDecimal price) {
        return new Product(name, Money.valueOf(price));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPriceValue() {
        return price.getValue();
    }

    public Money getPrice() {
        return price;
    }
}
