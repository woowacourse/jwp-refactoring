package kitchenpos.domain;

import java.math.BigDecimal;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;

public class Product {

    @Id
    private final Long id;
    private final String name;
    private final BigDecimal price;

    public Product(final String name, final BigDecimal price) {
        this(null, name, price);
    }

    @PersistenceCreator
    private Product(final Long id, final String name, final BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
