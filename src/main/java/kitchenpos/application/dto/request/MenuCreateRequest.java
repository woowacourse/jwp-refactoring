package kitchenpos.application.dto.request;

import kitchenpos.application.dto.MenuProductDto;

import java.math.BigDecimal;
import java.util.List;

public class MenuCreateRequest {

    final String name;
    final BigDecimal price;
    final Long menuGroupId;
    final List<MenuProductDto> menuProducts;

    public MenuCreateRequest(final String name, final BigDecimal price, final Long menuGroupId, final List<MenuProductDto> menuProducts) {
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
