package kitchenpos.domain.product;

import java.math.BigDecimal;

public class Product {
    private Long id;
    private String name;
    private ProductPrice price;

    public Product() {
    }

    public Product(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = new ProductPrice(price);
    }

    public BigDecimal multiplyWithQuantity(long quantity) {
        return price.multiplyWithQuantity(quantity);
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

    public void setPrice(final BigDecimal price) {
        this.price = new ProductPrice(price);
    }
}
