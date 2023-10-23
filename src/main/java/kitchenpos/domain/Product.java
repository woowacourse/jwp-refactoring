package kitchenpos.domain;

import java.math.BigDecimal;

public class Product {
    private Long id;
    private String name;
    private Price price;

    public Product() {
    }

    public Product(
            final String name,
            final BigDecimal price
    ) {
        this.name = name;
        this.price = new Price(price);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price.getPrice();
    }

    public void setPrice(final BigDecimal price) {
        this.price = new Price(price);
    }
}
