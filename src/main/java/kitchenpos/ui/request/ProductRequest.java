package kitchenpos.ui.request;

import kitchenpos.domain.Product;

import java.math.BigDecimal;

public class ProductRequest {

    private final String name;
    private final BigDecimal price;

    public ProductRequest(final String name, final BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Product toProduct() {
        return new Product(name, price);
    }
}
