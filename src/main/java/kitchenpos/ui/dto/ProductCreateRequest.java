package kitchenpos.ui.dto;

import kitchenpos.domain.Product;

import java.math.BigDecimal;

public class ProductCreateRequest {

    private final String name;
    private final BigDecimal price;

    public ProductCreateRequest(final String name, final BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Product toEntity() {
        return new Product(name, price);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
