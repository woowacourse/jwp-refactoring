package kitchenpos.product.persistence.entity;

import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;

import java.math.BigDecimal;

public class ProductEntity {

    private Long id;
    private String name;
    private BigDecimal price;

    public ProductEntity(final Long id, final String name, final BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public ProductEntity() {
    }

    public static ProductEntity from(final Product product) {
        return new ProductEntity(product.getId(), product.getName(),
                product.getPrice().getValue());
    }

    public Product toProduct() {
        return new Product(id, name, new Price(price));
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
