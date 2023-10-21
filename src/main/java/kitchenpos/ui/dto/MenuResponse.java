package kitchenpos.ui.dto;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Price;

import java.util.List;
import java.util.stream.Collectors;

public class MenuResponse {

    private final Long id;
    private final String name;
    private final Price price;
    private final Long menuGroupId;
    private final List<MenuProductResponse> menuProducts;

    public MenuResponse(final Long id, final String name, final Price price, final Long menuGroupId,
                        final List<MenuProductResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse from(final Menu menu) {
        return new MenuResponse(
                menu.getId(), menu.getName(), menu.getPrice(), menu.getMenuGroupId(),
                menu.getMenuProducts().stream()
                        .map(MenuProductResponse::from)
                        .collect(Collectors.toList()));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }
}
