package kitchenpos.product.domain;

import java.math.BigDecimal;

public class Product {

    private Long id;
    private String name;
    private Price price;

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(BigDecimal price) {
        this.price = new Price(price);
    }

    public Product() {
    }

    public Product(final String name, final BigDecimal price) {
        this.name = name;
        this.price = new Price(price);
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
