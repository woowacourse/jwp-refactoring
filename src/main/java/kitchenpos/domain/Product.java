package kitchenpos.domain;

import java.math.BigDecimal;

public class Product {
    private final Long id;
    private final String name;
    private final Price price;

    public Product(String name, BigDecimal price) {
        this(null, name, new Price(price));
    }

    public Product(Long id, String name, Price price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public BigDecimal multiplyPrice(long count) {
        return price.multiply(count);
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
