package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.List;

public class MenuFixture {
    static Menu createMenuRequest(
            String name,
            BigDecimal price,
            Long menuGroupId,
            List<MenuProduct> menuProducts
    ) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);
        return menu;
    }

    static Menu createMenu(
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

    static MenuProduct createMenuProductRequest(Long productId, int quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }

    static MenuProduct createMenuProduct(Long seq, Long productId, int quantity, Long menuId) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(seq);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        menuProduct.setMenuId(menuId);
        return menuProduct;
    }
}
