package kitchenpos.application.dto;

import java.math.BigDecimal;

public class CreateProductDto {

    private String name;
    private BigDecimal price;

    public CreateProductDto(String name, BigDecimal price) {
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
