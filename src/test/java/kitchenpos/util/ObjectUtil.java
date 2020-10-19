package kitchenpos.util;

import java.math.BigDecimal;
import java.util.List;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class ObjectUtil {
    public static Menu createMenu(Long id, String name, Integer price, Long menuGroupId,
        List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setName(name);
        if (price != null) {
            menu.setPrice(BigDecimal.valueOf(price));
        }
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);
        return menu;
    }

    public static MenuProduct createMenuProduct(Long seq, Long menuId, Long productId, long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(seq);
        menuProduct.setMenuId(menuId);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }

    public static Product createProduct(Long id, String name, Integer price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        if (price != null) {
            product.setPrice(BigDecimal.valueOf(price));
        }
        return product;
    }
}
