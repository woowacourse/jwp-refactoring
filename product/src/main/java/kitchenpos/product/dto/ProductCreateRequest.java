package kitchenpos.product.dto;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;

public class ProductCreateRequest {

    private String name;
    private BigDecimal price;

    public ProductCreateRequest() {
    }

    public ProductCreateRequest(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
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
