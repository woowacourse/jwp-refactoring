package kitchenpos.product.dto;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;

public class ProductCreateRequest {

    private String name;
    private BigDecimal price;

    private ProductCreateRequest() {
    }

    public ProductCreateRequest(final String name, final BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Product toProduct() {
        return new Product(null, name, price);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
