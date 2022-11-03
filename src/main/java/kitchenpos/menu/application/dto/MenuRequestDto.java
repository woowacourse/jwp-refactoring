package kitchenpos.menu.application.dto;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.domain.MenuProduct;

public class MenuRequestDto {

    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductDto> menuProducts;

    public MenuRequestDto(final String name,
                       final BigDecimal price,
                       final Long menuGroupId,
                       final List<MenuProductDto> menuProducts) {
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

    public List<MenuProductDto> getMenuProducts() {
        return menuProducts;
    }
}
