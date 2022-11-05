package kitchenpos.product.presentation.dto;

import java.math.BigDecimal;
import kitchenpos.product.application.dto.request.ProductCommand;

public record ProductRequest(String name, BigDecimal price) {
    public ProductCommand toCommand() {
        return new ProductCommand(name, price);
    }
}
