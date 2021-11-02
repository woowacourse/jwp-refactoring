package kitchenpos.domain;

import javax.persistence.Embedded;
import java.math.BigDecimal;

public class Product {
    private Long id;
    private String name;
    @Embedded
    private Price price;

    public Product(String name, BigDecimal price) {
        this(0L, name, price);
    }

    public Product(Long id, String name, BigDecimal price) {
        this(id, name, new Price(price));
    }

    public Product(Long id, String name, Price price) {
        this.id = id;
        this.name = name;
        this.price = price;
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
        return price.getValue();
    }

    public void setPrice(final Price price) {
        this.price = price;
    }
}
