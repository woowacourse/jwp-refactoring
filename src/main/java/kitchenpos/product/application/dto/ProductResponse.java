package kitchenpos.product.application.dto;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;

public class ProductResponse {

    private long id;
    private String name;
    private BigDecimal price;

    private ProductResponse(final long id, final String name, final BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductResponse of(final Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice());
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
