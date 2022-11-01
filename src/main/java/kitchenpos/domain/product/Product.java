package kitchenpos.domain.product;

import java.math.BigDecimal;
import kitchenpos.domain.vo.Price;

public class Product {

    private Long id;
    private String name;
    private Price price;

    public Product(final Long id, final String name, final BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = Price.valueOf(price);
    }

    public static Product create(final String name, final BigDecimal price) {
        return new Product(null, name, price);
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
