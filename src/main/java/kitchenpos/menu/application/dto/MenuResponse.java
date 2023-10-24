package kitchenpos.menu.application.dto;

import java.math.BigDecimal;
import kitchenpos.menu.domain.Menu;

public class MenuResponse {

    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final MenuProductsResponse menuProducts;

    private MenuResponse(final Long id, final String name,
                        final BigDecimal price, final Long menuGroupId,
                        final MenuProductsResponse menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse from(final Menu menu) {
        final Long id = menu.getId();
        final String name = menu.getName();
        final BigDecimal price = menu.getPrice();
        final Long menuGroupId = menu.getMenuGroup().getId();
        final MenuProductsResponse menuProductsResponse = MenuProductsResponse.from(menu.getMenuProducts());
        return new MenuResponse(id, name, price, menuGroupId, menuProductsResponse);
    }

    public Long getId() {
        return id;
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

    public MenuProductsResponse getMenuProducts() {
        return menuProducts;
    }
}
