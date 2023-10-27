package kitchenpos.domain;

import java.math.BigDecimal;
import kitchenpos.domain.product.Name;
import kitchenpos.domain.product.Price;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Embedded;

public class Product {
    @Id
    private Long id;

    @Embedded.Empty
    private Name name;

    @Embedded.Empty
    private Price price;

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
