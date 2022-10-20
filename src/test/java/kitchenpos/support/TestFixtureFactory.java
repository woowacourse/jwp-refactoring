package kitchenpos.support;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class TestFixtureFactory {

    private TestFixtureFactory() {
    }

    public static Product 상품을_생성한다(final String name, final BigDecimal price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        return product;
    }

    public static MenuGroup 메뉴_그룹을_생성한다(final String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return menuGroup;
    }

    public static Menu 메뉴를_생성한다(final String name, final BigDecimal price, final Long menuGroupId,
                                final List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);
        return menu;
    }

    public static MenuProduct 메뉴_상품을_생성한다(final Long menuId, final Long productId, final long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(menuId);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }
}
