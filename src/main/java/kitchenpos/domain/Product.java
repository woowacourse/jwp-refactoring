package kitchenpos.domain;

import java.math.BigDecimal;

public class Product {

    private Long id;
    private String name;
    private Price price;

    public Product() {
    }

    public Product(final String name, final BigDecimal price) {
        this(null, name, price);
    }

    public Product(final Long id, final String name, final BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = new Price(price);
    }

    public BigDecimal calculateTotalPrice(final long quantity) {
        return price.getPrice().multiply(new BigDecimal(quantity));
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

    public BigDecimal getPrice() {
        return price.getPrice();
    }
}
