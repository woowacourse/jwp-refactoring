package kitchenpos.application.dto.convertor;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.application.dto.request.MenuProductRequest;
import kitchenpos.application.dto.request.MenuRequest;
import kitchenpos.application.dto.response.MenuProductResponse;
import kitchenpos.application.dto.response.MenuResponse;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public class MenuConvertor {

    private MenuConvertor() {
    }

    public static Menu toMenu(final MenuRequest request) {
        final Menu menu = new Menu();
        menu.setName(request.getName());
        menu.setPrice(request.getPrice());
        menu.setMenuGroupId(request.getMenuGroupId());
        menu.setMenuProducts(toMenuProducts(request.getMenuProducts()));
        return menu;
    }

    public static MenuResponse toMenuResponse(final Menu menu) {
        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(), menu.getMenuGroupId(), toMenuProductResponses(menu.getMenuProducts()));
    }

    public static List<MenuResponse> toMenuResponses(final List<Menu> menus) {
        return menus.stream()
            .map(menu -> new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(), menu.getMenuGroupId(), toMenuProductResponses(menu.getMenuProducts())))
            .collect(Collectors.toUnmodifiableList());
    }

    public static List<MenuProduct> toMenuProducts(final List<MenuProductRequest> requests) {
        return requests.stream()
            .map(MenuConvertor::toMenuProduct)
            .collect(Collectors.toUnmodifiableList());
    }

    public static MenuProduct toMenuProduct(final MenuProductRequest request) {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(request.getProductId());
        menuProduct.setQuantity(request.getQuantity());
        return menuProduct;
    }

    public static List<MenuProductResponse> toMenuProductResponses(final List<MenuProduct> menuProducts) {
        return menuProducts.stream()
            .map(menuProduct -> new MenuProductResponse(menuProduct.getSeq(), menuProduct.getMenuId(), menuProduct.getProductId(), menuProduct.getQuantity()))
            .collect(Collectors.toUnmodifiableList());
    }
}
