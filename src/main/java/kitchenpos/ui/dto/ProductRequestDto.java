package kitchenpos.ui.dto;

import java.math.BigDecimal;
import kitchenpos.application.dto.CreateProductDto;

public class ProductRequestDto {

    private String name;
    private BigDecimal price;

    public ProductRequestDto() {
    }

    public CreateProductDto toCreateProductDto() {
        return new CreateProductDto(name, price);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
