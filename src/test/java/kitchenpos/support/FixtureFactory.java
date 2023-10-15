package kitchenpos.support;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

import java.math.BigDecimal;
import java.util.List;

public class FixtureFactory {

    public static Product forSaveProduct(final String name, final BigDecimal price) {
        final Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        return product;
    }

    public static Product savedProduct(final Long id, final String name, final BigDecimal price) {
        final Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        return product;
    }

    public static MenuGroup forSaveMenuGroup(final String groupName) {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(groupName);
        return menuGroup;
    }

    public static MenuGroup savedMenuGroup(final Long id, final String groupName) {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(id);
        menuGroup.setName(groupName);
        return menuGroup;
    }

    public static Menu forSaveMenu(final String name, final BigDecimal price, final Long menuGroupId, final List<MenuProduct> menuProducts) {
        final Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);
        return menu;
    }

    public static Menu savedMenu(final Long id, final String name, final BigDecimal price, final Long menuGroupId, final List<MenuProduct> menuProducts) {
        final Menu menu = new Menu();
        menu.setId(id);
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);
        return menu;
    }

    public static MenuProduct savedMenuProduct(final Long seq, final Long menuId, final Long productId, final long quantity) {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(seq);
        menuProduct.setMenuId(menuId);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }
}
