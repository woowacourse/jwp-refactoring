package kitchenpos.dto;

import kitchenpos.domain.Price;
import kitchenpos.domain.Product;

import java.math.BigDecimal;

public class ProductCreateRequest {

    private String name;
    private BigDecimal price;

    protected ProductCreateRequest() {
    }

    public ProductCreateRequest(final String name, final BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Product toEntity() {
        return new Product(name, new Price(price));
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
