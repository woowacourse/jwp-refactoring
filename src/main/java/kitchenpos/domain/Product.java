package kitchenpos.domain;

import java.math.BigDecimal;

public class Product {
    private Long id;
    private String name;
    private Price price;

    public Product(
            final Long id,
            final String name,
            final BigDecimal price
    ) {
        this.id = id;
        this.name = name;
        this.price = Price.from(price);
    }

    public Product(
            final String name,
            final BigDecimal price
    ) {
        this.name = name;
        this.price = Price.from(price);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getPrice();
    }
}
