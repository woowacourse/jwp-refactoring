package kitchenpos.ui.dto;

import java.math.BigDecimal;
import kitchenpos.application.dto.request.CreateProductDto;

public class ProductRequestDto {

    private String name;
    private BigDecimal price;

    public ProductRequestDto() {
    }

    public CreateProductDto toCreateProductDto() {
        return new CreateProductDto(name, price);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
