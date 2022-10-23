package kitchenpos.application.dto;

import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;

@Getter
public class CreateMenuDto {

    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final List<CreateMenuProductDto> menuProducts;

    public CreateMenuDto(String name,
                         BigDecimal price,
                         Long menuGroupId,
                         List<CreateMenuProductDto> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }
}
