package kitchenpos.application.dto.response;

import java.math.BigDecimal;

public class ProductResponseDto {

    private Long id;
    private String name;
    private BigDecimal price;

    private ProductResponseDto() {
    }

    public ProductResponseDto(Long id, String name, BigDecimal price) {
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
