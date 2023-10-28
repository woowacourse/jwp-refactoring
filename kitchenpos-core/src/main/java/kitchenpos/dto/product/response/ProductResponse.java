package kitchenpos.dto.product.response;

import java.math.BigDecimal;
import kitchenpos.domain.product.Product;

public class ProductResponse {
    private final Long id;
    private final String name;
    private final BigDecimal price;

    private ProductResponse(final Long id, final String name, final BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductResponse of(final Product product) {
        return new ProductResponse(
                product.id(),
                product.name(),
                product.price().price()
        );
    }

    public static ProductResponse of(final String name, final BigDecimal price) {
        return new ProductResponse(
                null,
                name,
                price
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
