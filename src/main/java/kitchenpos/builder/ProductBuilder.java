package kitchenpos.builder;

import java.math.BigDecimal;

import kitchenpos.domain.Product;

public class ProductBuilder {
    private Long id;
    private String name;
    private BigDecimal price;

    public ProductBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public ProductBuilder name(String name) {
        this.name = name;
        return this;
    }

    public ProductBuilder price(BigDecimal price) {
        this.price = price;
        return this;
    }

    public Product build() {
        return new Product(id, name, price);
    }
}
