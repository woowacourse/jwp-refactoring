package kitchenpos.application.dto;

import java.math.BigDecimal;
import kitchenpos.domain.product.Product;

public class ReadProductDto {

    private Long id;
    private String name;
    private BigDecimal price;

    public ReadProductDto(final Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
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
