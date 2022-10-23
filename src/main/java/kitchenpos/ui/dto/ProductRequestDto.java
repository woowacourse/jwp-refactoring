package kitchenpos.ui.dto;

import java.math.BigDecimal;
import kitchenpos.application.dto.CreateProductDto;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
public class ProductRequestDto {

    private String name;
    private BigDecimal price;

    public CreateProductDto toCreateProductDto() {
        return new CreateProductDto(name, price);
    }
}
