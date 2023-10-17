package kitchenpos.ui.dto.product;

import java.math.BigDecimal;

import kitchenpos.domain.Product;

public class CreateProductRequest {

    private final String name;
    private final Integer price;

    public CreateProductRequest(final String name, final Integer price) {
        this.name = name;
        this.price = price;
    }

    public Product toDomain() {
        return new Product(name, BigDecimal.valueOf(price));
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }
}
