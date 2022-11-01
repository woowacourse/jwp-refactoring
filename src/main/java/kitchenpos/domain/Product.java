package kitchenpos.domain;

import java.math.BigDecimal;

public class Product {

    private final Long id;
    private final String name;
    private final Price price;

    /**
     * DB 에 저장되지 않은 객체
     */
    public Product(final String name, final BigDecimal price) {
        this(null, name, price);
    }

    /**
     * DB 에 저장된 객체
     */
    public Product(final Long id, final String name, final BigDecimal price) {
        this.id = id;
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
        return price.getPrice();
    }
}
