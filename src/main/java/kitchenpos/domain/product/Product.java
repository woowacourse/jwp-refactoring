package kitchenpos.domain.product;

import java.math.BigDecimal;

public class Product {
    private final Long id;
    private final Name name;
    private final Price price;

    public static Product from(final String name, final BigDecimal price) {
        return new Product(Name.from(name), Price.from(price));
    }

    private Product(final Name name, final Price price) {
        this(null, name, price);
    }

    public Product(final Long id, final Name name, final Price price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }
}
