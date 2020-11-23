package kitchenpos.dto.product;

import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductPrice;

import java.math.BigDecimal;

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
        return Product.of(this.name, ProductPrice.from(this.price));
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
