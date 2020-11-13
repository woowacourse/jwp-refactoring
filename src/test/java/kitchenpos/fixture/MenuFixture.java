package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.ui.dto.MenuCreateRequest;
import kitchenpos.ui.dto.MenuProductCreateRequest;

import java.math.BigDecimal;
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
        Menu menu = new Menu();
        menu.setId(id);
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);
        return menu;
    }

    public static MenuProductCreateRequest createMenuProductRequest(Long productId, int quantity) {
        return new MenuProductCreateRequest(productId, quantity);
    }

    public static MenuProduct createMenuProduct(Long seq, Long productId, int quantity, Long menuId) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(seq);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        menuProduct.setMenuId(menuId);
        return menuProduct;
    }
}
