package kitchenpos.dto;

import java.math.BigDecimal;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;

public class ProductRequest {

    private String name;
    private BigDecimal price;

    public ProductRequest(final String name, final BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Product toEntity() {
        return new Product(name, Price.valueOf(price));
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
