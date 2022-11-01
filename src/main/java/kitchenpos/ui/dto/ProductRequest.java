package kitchenpos.ui.dto;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductRequest {

    private final String name;
    private final BigDecimal price;

    public ProductRequest(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Product toProduct() {
        return new Product(name, price);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
