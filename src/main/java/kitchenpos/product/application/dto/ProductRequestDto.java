package kitchenpos.product.application.dto;

import java.math.BigDecimal;

public class ProductRequestDto {

    private String name;
    private BigDecimal price;

    public ProductRequestDto(final String name, final BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
