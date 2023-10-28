package kitchenpos.dto.menu;

import kitchenpos.domain.menu.Menu;

public class MenuResponse {
    private final Long id;
    private final String name;
    private final long price;
    private final Long menuGroupId;
    private final MenuProductsResponse menuProducts;

    private MenuResponse(final Long id, final String name, final long price, final Long menuGroupId, final MenuProductsResponse menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse from(final Menu menu) {
        return new MenuResponse(
                menu.getId(),
                menu.getName().getName(),
                menu.getPrice().getPrice().longValue(),
                menu.getMenuGroupId(),
                MenuProductsResponse.from(menu.getMenuProducts())
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public MenuProductsResponse getMenuProducts() {
        return menuProducts;
    }
}
