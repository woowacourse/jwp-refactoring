package kitchenpos.dto.product;

import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductPrice;

import javax.validation.constraints.NotNull;

import java.math.BigDecimal;

public class ProductRequest {
    @NotNull
    private String name;

    @NotNull
    private BigDecimal price;

    public ProductRequest() {
    }

    public ProductRequest(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Product toProduct() {
        return new Product(name, ProductPrice.of(price));
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
