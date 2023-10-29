package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProducts;

import java.util.List;
import java.util.stream.Collectors;

public class MenuProductsResponse {
    private final List<MenuProductResponse> menuProducts;

    private MenuProductsResponse(final List<MenuProductResponse> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public static MenuProductsResponse from(final MenuProducts menuProducts) {
        return new MenuProductsResponse(menuProducts.getItems().stream()
                .map(MenuProductResponse::from)
                .collect(Collectors.toList()));
    }

    public List<MenuProductResponse> getItems() {
        return menuProducts;
    }
}
