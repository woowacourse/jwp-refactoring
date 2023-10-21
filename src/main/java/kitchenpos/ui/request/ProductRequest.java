package kitchenpos.ui.request;

import kitchenpos.domain.Product;

import java.math.BigDecimal;

public class ProductRequest {

    private final Long id;
    private final String name;
    private final BigDecimal price;

    public ProductRequest(final Long id, final String name, final BigDecimal price) {
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

    public Product toProduct() {
        return new Product(id, name, price);
    }
}
