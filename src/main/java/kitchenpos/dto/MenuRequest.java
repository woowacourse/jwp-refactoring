package kitchenpos.dto;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public class MenuRequest {

    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final List<MenuProductInfo> menuProductInfos;

    public MenuRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProductInfo> menuProductInfos) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProductInfos = menuProductInfos;
    }

    public Menu toEntity() {
        return new Menu(name, price, menuGroupId, menuProducts);
    }

    public Menu toEntity(Long id) {
        Menu menu = new Menu(name, price, menuGroupId, menuProducts);
        menu.setId(id);
        return menu;
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

    public List<MenuProductInfo> getMenuProductInfos() {
        return menuProductInfos;
    }
}
