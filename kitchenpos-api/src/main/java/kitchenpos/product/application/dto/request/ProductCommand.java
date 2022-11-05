package kitchenpos.product.application.dto.request;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;

public record ProductCommand(String name, BigDecimal price) {

    public Product toEntity() {
        return new Product(name, price);
    }
}
