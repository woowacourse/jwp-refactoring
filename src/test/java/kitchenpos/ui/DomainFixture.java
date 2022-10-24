package kitchenpos.ui;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class DomainFixture {

    public static Product getProduct() {
        final Product product = new Product();
        product.setId(1L);
        product.setName("productName");
        product.setPrice(BigDecimal.valueOf(1000L));
        return product;
    }

    public static MenuGroup getMenuGroup() {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(1L);
        menuGroup.setName("menuGroup");
        return menuGroup;
    }

    public static Menu getMenu() {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setId(1L);
        menuProduct.setQuantity(2);
        menuProduct.setProductId(1L);
        menuProduct.setMenuId(1L);
        final Menu menu = new Menu();
        menu.setId(1L);
        menu.setName("menu");
        menu.setPrice(BigDecimal.valueOf(1000L));
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(List.of(menuProduct));
        return menu;
    }

}
