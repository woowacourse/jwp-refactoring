package kitchenpos.common.builder;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductBuilder {

    private String name;
    private BigDecimal price;

    public ProductBuilder name(final String name) {
        this.name = name;
        return this;
    }

    public ProductBuilder price(final BigDecimal price) {
        this.price = price;
        return this;
    }

    public Product build(){
        return new Product(name, price);
    }
}
