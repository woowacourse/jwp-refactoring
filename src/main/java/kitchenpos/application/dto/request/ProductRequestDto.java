package kitchenpos.application.dto.request;

import java.math.BigDecimal;

public class ProductRequestDto {

    private String name;
    private BigDecimal price;

    private ProductRequestDto() {
    }

    public ProductRequestDto(String name, BigDecimal price) {
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
