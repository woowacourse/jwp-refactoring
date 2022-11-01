package kitchenpos.domain;

import java.math.BigDecimal;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Embedded;

public class Product {

    @Id
    private final Long id;
    private final String name;

    @Embedded.Nullable
    private final Price price;

    protected Product(final Long id, final String name, final Price price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static Product of(final String name, final BigDecimal price) {
        return new Product(null, name, new Price(price));
    }

    public BigDecimal calculateAmount(final long quantity) {
        return price.multiply(quantity);
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
