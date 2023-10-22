package kitchenpos.dto;

import java.math.BigDecimal;
import java.util.List;

public class MenuCreateDto {

    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final List<MenuProductCreateDto> menuProducts;

    public MenuCreateDto(final String name, final BigDecimal price, final Long menuGroupId,
                         final List<MenuProductCreateDto> menuProducts) {
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

    public List<MenuProductCreateDto> getMenuProducts() {
        return menuProducts;
    }
}
