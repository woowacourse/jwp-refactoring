package kitchenpos.domain;

import java.math.BigDecimal;

public class Product {

    private final Long id;
    private final String name;
    private final Price price;

    public Product(String name, Price price) {
        this(null, name, price);
    }

    public Product(Long id, String name, Price price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public BigDecimal price() {
        return price.value();
    }
}
