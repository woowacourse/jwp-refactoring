package kitchenpos.ui.dto;

import kitchenpos.domain.Product;

import java.math.BigDecimal;

public class CreateProductRequest {

    private String name;
    private BigDecimal price;

    public CreateProductRequest() {
    }

    public CreateProductRequest(final String name, final BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Product toEntity() {
        return new Product(name, price);
    }
}
