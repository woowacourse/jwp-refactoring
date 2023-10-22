package kitchenpos.dto.request;

import kitchenpos.domain.Product;

import java.math.BigDecimal;

public class ProductCreateRequest {

    private final String name;
    private final BigDecimal price;

    public ProductCreateRequest(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Product toEntity() {
        return new Product(name, price);
    }

}
