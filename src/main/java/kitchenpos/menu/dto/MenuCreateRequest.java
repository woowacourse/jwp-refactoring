package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

public class MenuCreateRequest {
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProduct> menuProducts;

    private MenuCreateRequest() {
    }

    public MenuCreateRequest(final String name, final BigDecimal price, final Long menuGroupId,
                             final List<MenuProduct> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuCreateRequest from(final Menu menu) {
        return new MenuCreateRequest(menu.getName(), menu.getPrice(), menu.getMenuGroupId(), menu.getAllMenuProduct());
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

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
