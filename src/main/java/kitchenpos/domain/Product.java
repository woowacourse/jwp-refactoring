package kitchenpos.domain;

import java.math.BigDecimal;
import kitchenpos.domain.product.Price;

public class Product {
    private Long id;
    private String name;
    private Price price;

    public Product() {}

    public Product(final Long id, final String name, final BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = new Price(price);
    }

    public Product(final String name, final BigDecimal price) {
        this(null, name, price);
    }

    public Long getId() {
        return id;
    }

    @Deprecated
    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @Deprecated
    public void setName(final String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }

    @Deprecated
    public void setPrice(final BigDecimal price) {
        this.price = new Price(price);
    }
}
