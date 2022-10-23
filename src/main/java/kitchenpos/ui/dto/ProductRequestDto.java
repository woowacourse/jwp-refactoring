package kitchenpos.ui.dto;

import java.math.BigDecimal;
import kitchenpos.application.dto.CreateProductDto;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ProductRequestDto {

    private String name;
    private BigDecimal price;

    public CreateProductDto toCreateProductDto() {
        return new CreateProductDto(name, price);
    }
}
