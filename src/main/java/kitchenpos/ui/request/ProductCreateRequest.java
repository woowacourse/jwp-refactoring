package kitchenpos.ui.request;

import kitchenpos.domain.product.Product;

import java.math.BigDecimal;

public class ProductCreateRequest {

    private final String name;
    private final BigDecimal price;

    public ProductCreateRequest(final String name, final BigDecimal price) {
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
