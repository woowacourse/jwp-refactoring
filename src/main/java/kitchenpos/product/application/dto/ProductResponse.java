package kitchenpos.product.application.dto;

import kitchenpos.product.domain.Product;

import java.math.BigDecimal;

public final class ProductResponse {

    private final Long id;
    private final String name;
    private final BigDecimal price;

    public static ProductResponse from(final Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice());
    }

    private ProductResponse(final Long id, final String name, final BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
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
