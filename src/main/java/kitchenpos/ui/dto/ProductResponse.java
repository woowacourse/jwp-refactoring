package kitchenpos.ui.dto;

import kitchenpos.domain.Product;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductResponse {
    private final Long id;
    private final String name;
    private final BigDecimal price;

    public static ProductResponse from(final Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice());
    }
}
