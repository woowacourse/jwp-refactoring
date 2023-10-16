package kitchenpos.dto.menu;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.MenuProduct;

public class MenuProductsResponse {

    private final List<MenuProductResponse> menuProducts;

    private MenuProductsResponse(final List<MenuProductResponse> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public static MenuProductsResponse from(final List<MenuProduct> menuProducts) {
        List<MenuProductResponse> menuProductResponses = menuProducts.stream()
                .map(MenuProductResponse::from)
                .collect(Collectors.toUnmodifiableList());
        return new MenuProductsResponse(menuProductResponses);
    }

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }
}
