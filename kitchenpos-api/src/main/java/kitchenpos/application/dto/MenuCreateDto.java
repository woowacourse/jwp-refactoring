package kitchenpos.application.dto;

import java.math.BigDecimal;
import java.util.List;

public class MenuCreateDto {

    private final String name;
    private final Long menuGroupId;
    private final BigDecimal price;
    private final List<MenuProductCreateDto> menuProducts;

    public MenuCreateDto(final String name, final Long menuGroupId, final BigDecimal price,
        final List<MenuProductCreateDto> menuProducts) {
        this.name = name;
        this.menuGroupId = menuGroupId;
        this.price = price;
        this.menuProducts = menuProducts;
    }

    public String getName() {
        return name;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public List<MenuProductCreateDto> getMenuProducts() {
        return menuProducts;
    }
}
