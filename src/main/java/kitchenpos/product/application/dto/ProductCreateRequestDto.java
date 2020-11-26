package kitchenpos.product.application.dto;

import java.math.BigDecimal;

import kitchenpos.product.model.Product;

public class ProductCreateRequestDto {
    private String name;
    private BigDecimal price;

    private ProductCreateRequestDto() {
    }

    public ProductCreateRequestDto(String name, BigDecimal price) {
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
        return new Product(null, this.name, this.price);
    }
}
