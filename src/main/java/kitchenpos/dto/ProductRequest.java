package kitchenpos.dto;

import java.math.BigDecimal;
import kitchenpos.domain.Price;
import kitchenpos.domain.product.Product;

public class ProductRequest {
    private final String name;
    private final BigDecimal price;

    public ProductRequest(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Product toProduct() {
        return new Product(name, new Price(price));
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
