package kitchenpos.ui.dto;

import kitchenpos.domain.Product;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequest {
    private final Long id;
    private final String name;
    private final BigDecimal price;

    public Product convert() {
        return new Product(id, name, price);
    }
}
