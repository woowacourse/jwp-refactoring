package kitchenpos.product.application.dto.response;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;

public record ProductResponse(Long id, String name, BigDecimal price) {

    public static ProductResponse from(final Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice());
    }
}
