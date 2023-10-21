package kitchenpos.application.dto.domain;

import java.math.BigDecimal;
import kitchenpos.domain.product.Product;

public class ProductDto {
    private Long id;
    private String name;
    private BigDecimal price;

    public static ProductDto from(final Product product) {
        return new ProductDto(product.getId(), product.getName(), product.getPrice());
    }

    public ProductDto(final Long id, final String name, final BigDecimal price) {
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
