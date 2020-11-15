package kitchenpos.ui.dto;

import kitchenpos.domain.Product;

import java.math.BigDecimal;

public class ProductCreateRequest {
    private String name;
    private BigDecimal price;

    public ProductCreateRequest(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Product toEntity() {
        return new Product(null, name, price);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
