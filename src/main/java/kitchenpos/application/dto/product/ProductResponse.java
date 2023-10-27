package kitchenpos.application.dto.product;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductResponse {

    private Long id;
    private String name;
    private BigDecimal price;

    private ProductResponse(final Long id, final String name, final BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductResponse from(final Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPriceValue());
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