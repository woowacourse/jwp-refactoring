package kitchenpos.generator;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public class MenuGenerator {

    public static Menu newInstance(String name, int price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return newInstance(null, name, price, menuGroupId, menuProducts);
    }

    public static Menu newInstance(String name, int price, Long menuGroupId) {
        return newInstance(null, name, price, menuGroupId, null);
    }

    public static Menu newInstance(Long id, String name, int price, Long menuGroupId) {
        return newInstance(id, name, price, menuGroupId, Collections.emptyList());
    }

    public static Menu newInstance(Long id, String name, int price, Long menuGroupId, List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setName(name);
        menu.setPrice(BigDecimal.valueOf(price));
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);
        menu.setMenuProducts(menuProducts);
        return menu;
    }

    public static MenuProduct newMenuProduct(Long productId, int quantity) {
        return newMenuProduct(null, productId, quantity);
    }

    public static MenuProduct newMenuProduct(Long menuId, Long productId, int quantity) {
        return newMenuProduct(null, menuId, productId, quantity);
    }

    public static MenuProduct newMenuProduct(Long seq, Long menuId, Long productId, int quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(seq);
        menuProduct.setMenuId(menuId);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }
}
