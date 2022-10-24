package kitchenpos.domain;

import java.math.BigDecimal;

public class Product {
    private Long id;
    private String name;
    private Long price;

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

    public Long getPrice() {
        return price;
    }

    public void setPrice(final Long price) {
        this.price = price;
    }
}
