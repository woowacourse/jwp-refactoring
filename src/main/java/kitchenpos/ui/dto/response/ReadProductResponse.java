package kitchenpos.ui.dto.response;

import java.math.BigDecimal;
import kitchenpos.domain.product.Product;

public class ReadProductResponse {

    private final Long id;
    private final String name;
    private final BigDecimal price;

    public static ReadProductResponse from(final Product product) {
        return new ReadProductResponse(product.getId(), product.getName(), product.getPrice());
    }

    private ReadProductResponse(final Long id, final String name, final BigDecimal price) {
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
