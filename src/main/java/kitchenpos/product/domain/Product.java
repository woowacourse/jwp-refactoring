package kitchenpos.product.domain;

import java.math.BigDecimal;
import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;

public class Product {
    private Long id;
    private Name name;
    private Price price;

    public Product(String name, BigDecimal price) {
        this(null, name, price);
    }
    public Product(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = new Name(name);
        this.price = new Price(price);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }
}
