package kitchenpos.application.dto.convertor;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.application.dto.response.MenuProductResponse;
import kitchenpos.application.dto.response.MenuResponse;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public class MenuConvertor {

    private MenuConvertor() {
    }

    public static MenuResponse toMenuResponse(final Menu menu) {
        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(), menu.getMenuGroupId(), toMenuProductResponses(menu.getMenuProducts()));
    }

    public static List<MenuResponse> toMenuResponses(final List<Menu> menus) {
        return menus.stream()
            .map(menu -> new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(), menu.getMenuGroupId(), toMenuProductResponses(menu.getMenuProducts())))
            .collect(Collectors.toUnmodifiableList());
    }

    public static List<MenuProductResponse> toMenuProductResponses(final List<MenuProduct> menuProducts) {
        return menuProducts.stream()
            .map(menuProduct -> new MenuProductResponse(menuProduct.getSeq(), menuProduct.getMenuId(), menuProduct.getProductId(), menuProduct.getQuantity()))
            .collect(Collectors.toUnmodifiableList());
    }
}
