package kitchenpos.builder;

import kitchenpos.product.domain.Product;

import java.math.BigDecimal;

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
        return new Product(this.id, this.name, this.price);
    }
}
