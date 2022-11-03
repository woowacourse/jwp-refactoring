package kitchenpos.application.dto;

import java.math.BigDecimal;
import java.util.List;

public class CreateMenuDto {

    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<CreateMenuProductDto> menuProducts;

    private CreateMenuDto() {
    }

    public CreateMenuDto(final String name,
                         final BigDecimal price,
                         final Long menuGroupId,
                         final List<CreateMenuProductDto> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<CreateMenuProductDto> getMenuProducts() {
        return menuProducts;
    }
}
