package kitchenpos.dto;

import java.math.BigDecimal;

import kitchenpos.domain.Product;

public class ProductRequest {

    private String name;
    private BigDecimal price;

    public ProductRequest() {}

    public ProductRequest(String name, double price) {
        this.name = name;
        this.price = BigDecimal.valueOf(price);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Product toEntity() {
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격은 음수가 될 수 없습니다.");
        }

        return new Product(null, name, price);
    }
}
