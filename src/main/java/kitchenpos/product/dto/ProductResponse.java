package kitchenpos.product.dto;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;

public class ProductResponse {
    private final Long id;
    private final String name;
    private final BigDecimal price;

    private ProductResponse(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductResponse from(Product savedProduct) {
        return new ProductResponse(
                savedProduct.getId(),
                savedProduct.getName(),
                savedProduct.getPrice()
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
