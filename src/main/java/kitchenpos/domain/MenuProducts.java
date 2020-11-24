package kitchenpos.domain;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.dto.menu.MenuProductRequest;

public class MenuProducts {

    private List<MenuProduct> menuProducts;

    private MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public static MenuProducts from(List<MenuProductRequest> menuProductRequests, Menu menu) {
        validateMenu(menu);

        List<MenuProduct> menuProducts = menuProductRequests.stream()
            .map(menuProductRequest -> new MenuProduct(
                menu.getId(), menuProductRequest.getProductId(), menuProductRequest.getQuantity()))
            .collect(Collectors.toList());

        return new MenuProducts(menuProducts);
    }

    private static void validateMenu(Menu menu) {
        if (Objects.isNull(menu.getId())) {
            throw new IllegalArgumentException("menu id must exist.");
        }
    }

    public List<MenuProduct> getMenuProducts() {
        return Collections.unmodifiableList(menuProducts);
    }
}
