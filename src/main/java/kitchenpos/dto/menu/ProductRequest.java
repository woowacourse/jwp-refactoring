package kitchenpos.dto.menu;

import kitchenpos.domain.menu.Price;
import kitchenpos.domain.menu.Product;

import java.math.BigDecimal;

public class ProductRequest {
    private String name;
    private BigDecimal price;

    private ProductRequest() {
    }

    public ProductRequest(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Product to() {
        return new Product(name, Price.of(price));
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
