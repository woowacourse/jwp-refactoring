package kitchenpos.dto.menu;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuPrice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MenuRequest {
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductDto> menuProductDtos;

    public MenuRequest() {
    }

    public MenuRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProductDto> menuProductDtos) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProductDtos = menuProductDtos;
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

    public List<MenuProductDto> getMenuProductDtos() {
        return menuProductDtos;
    }

    public Menu toMenu(MenuGroup menuGroup, BigDecimal productsPriceSum) {
        return new Menu(name, MenuPrice.of(price, productsPriceSum), menuGroup, new ArrayList<>());
    }
}
