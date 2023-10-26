package kitchenpos.application.dto;

import java.math.BigDecimal;

public class ProductCreateDto {

    private String name;
    private BigDecimal price;

    public ProductCreateDto(final String name, final BigDecimal price) {
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
