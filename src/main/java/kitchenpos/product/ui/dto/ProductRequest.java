package kitchenpos.product.ui.dto;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;

public class ProductRequest {

    private String name;
    private Long price;

    private ProductRequest() {
    }

    public ProductRequest(final String name, final Long price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public Product toEntity() {
        return new Product(name, BigDecimal.valueOf(price));
    }
}
