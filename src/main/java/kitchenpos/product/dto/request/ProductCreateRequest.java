package kitchenpos.product.dto.request;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;

public class ProductCreateRequest {
    private String name;
    private BigDecimal price;

    public ProductCreateRequest(final String name, final BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Product toEntity() {
        return new Product(name, price);
    }

    public String name() {
        return name;
    }

    public BigDecimal price() {
        return price;
    }
}
