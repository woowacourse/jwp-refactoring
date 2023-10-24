package kitchenpos.product.dto.response;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;

public class ProductResponse {
    private final Long id;
    private final String name;
    private final BigDecimal price;

    private ProductResponse(final Long id, final String name, final BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductResponse from(final Product product) {
        return new ProductResponse(
                product.id(),
                product.name(),
                product.price().price()
        );
    }

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public BigDecimal price() {
        return price;
    }
}
