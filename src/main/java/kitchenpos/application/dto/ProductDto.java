package kitchenpos.application.dto;

import java.math.BigDecimal;
import kitchenpos.domain.Product;
import lombok.Getter;

@Getter
public class ProductDto {

    private final Long id;
    private final String name;
    private final BigDecimal price;

    private ProductDto(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductDto of(Product product) {
        return new ProductDto(product.getId(), product.getName(), product.getPrice());
    }
}
