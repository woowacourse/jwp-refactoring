package kitchenpos.product.ui.dto;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;

public class ProductRequest {

    private String name;
    private BigDecimal price;

    public ProductRequest(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    private ProductRequest() {
    }

    public Product toProduct() {
        return Product.of(name, price);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
