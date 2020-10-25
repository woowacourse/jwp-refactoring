package kitchenpos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public class MenuFixture {
    public static Menu createMenuWithoutId() {
        final Menu menu = new Menu();
        final MenuProduct menuProduct = new MenuProduct();
        menu.setName("후라이드+후라이드");
        menu.setPrice(BigDecimal.valueOf(19000));
        menu.setMenuGroupId(1L);
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(2);
        menu.setMenuProducts(new ArrayList<>(Collections.singletonList(menuProduct)));

        return menu;
    }

    public static Menu createMenuWithId(final Long id) {
        final Menu menu = new Menu();
        final MenuProduct menuProduct = new MenuProduct();
        menu.setId(id);
        menu.setName("후라이드+후라이드");
        menu.setPrice(BigDecimal.valueOf(19000));
        menu.setMenuGroupId(1L);
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(2);
        menu.setMenuProducts(new ArrayList<>(Collections.singletonList(menuProduct)));

        return menu;
    }

    public static Menu createMinusPriceMenu() {
        final Menu menu = new Menu();
        final MenuProduct menuProduct = new MenuProduct();
        menu.setName("후라이드+후라이드");
        menu.setPrice(BigDecimal.valueOf(-10000));
        menu.setMenuGroupId(1L);
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(2);
        menu.setMenuProducts(new ArrayList<>(Collections.singletonList(menuProduct)));

        return menu;
    }

    public static Menu createNoPriceMenu() {
        final Menu menu = new Menu();
        final MenuProduct menuProduct = new MenuProduct();
        menu.setName("후라이드+후라이드");
        menu.setMenuGroupId(1L);
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(2);
        menu.setMenuProducts(new ArrayList<>(Collections.singletonList(menuProduct)));

        return menu;
    }

    public static MenuProduct createMenuProductWithProductId(final Long productId) {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(2);
        return menuProduct;
    }

    public static List<Menu> createMenus() {
        return Arrays.asList(createMenuWithId(1L), createMenuWithId(2L));
    }
}
