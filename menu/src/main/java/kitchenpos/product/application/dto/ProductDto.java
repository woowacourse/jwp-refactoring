package kitchenpos.product.application.dto;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;

public class ProductDto {

    private final Long id;
    private final String name;
    private final BigDecimal price;

    public ProductDto(final Long id, final String name, final BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductDto toDto(final Product product) {
        return new ProductDto(product.getId(), product.getName(), product.getPrice().getValue());
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
