package kitchenpos.application.dto.product;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductCreateResponse {

    private final Long id;
    private final String name;
    private final BigDecimal price;

    private ProductCreateResponse(final Long id, final String name, final BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductCreateResponse of(final Product product) {
        return new ProductCreateResponse(product.getId(), product.getName(), product.getPrice());
    }

    public Long getId() {
        return id;
    }
}
