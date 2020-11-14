package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.ui.dto.MenuCreateRequest;
import kitchenpos.ui.dto.MenuProductCreateRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MenuFixture {
    public static MenuCreateRequest createMenuRequest(
            String name,
            BigDecimal price,
            Long menuGroupId,
            List<MenuProductCreateRequest> menuProducts
    ) {
        return new MenuCreateRequest(name, price, menuGroupId, menuProducts);
    }

    public static Menu createMenu(
            Long id,
            String name,
            BigDecimal price,
            Long menuGroupId,
            List<MenuProduct> menuProducts
    ) {
        Menu menu = Menu.from(id, name, price, menuGroupId);
        for (MenuProduct menuProduct : menuProducts) {
            menu.addMenuProduct(menuProduct);
        }
        return menu;
    }

    public static Menu createMenu(
            Long id,
            String name,
            BigDecimal price,
            Long menuGroupId
    ) {
        return Menu.from(id, name, price, menuGroupId);
    }

    public static MenuProductCreateRequest createMenuProductRequest(Long productId, int quantity) {
        return new MenuProductCreateRequest(productId, quantity);
    }

    public static MenuProduct createMenuProduct(Long seq, Long productId, int quantity, Long menuId) {
        return new MenuProduct(seq, menuId, productId, quantity);
    }
}
