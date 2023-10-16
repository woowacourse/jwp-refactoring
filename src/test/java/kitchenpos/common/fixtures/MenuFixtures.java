package kitchenpos.common.fixtures;

import static kitchenpos.common.fixtures.ProductFixtures.PRODUCT1_PRICE;
import static kitchenpos.common.fixtures.ProductFixtures.PRODUCT2_PRICE;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public class MenuFixtures {

    /**
     * NAME
     */
    public static final String MENU1_NAME = "후라이드치킨 & 양념치킨";
    public static final String MENU2_NAME = "양념치킨";

    /**
     * PRICE
     */
    public static final BigDecimal MENU1_PRICE =
            PRODUCT1_PRICE.multiply(BigDecimal.valueOf(MENU1_MENU_PRODUCT1().getQuantity()))
                    .add(PRODUCT2_PRICE.multiply(BigDecimal.valueOf(MENU1_MENU_PRODUCT2().getQuantity())));

    public static final BigDecimal MENU2_PRICE =
            PRODUCT1_PRICE.multiply(BigDecimal.valueOf(MENU2_MENU_PRODUCT1().getQuantity()));

    /**
     * MENU_GROUP_ID
     */
    public static final Long MENU1_MENU_GROUP_ID = 2L;
    public static final Long MENU2_MENU_GROUP_ID = 2L;

    /**
     * MENU_PRODUCT
     */
    public static MenuProduct MENU1_MENU_PRODUCT1() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(2L);
        return menuProduct;
    }

    public static MenuProduct MENU1_MENU_PRODUCT2() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(2L);
        menuProduct.setQuantity(1);
        return menuProduct;
    }

    public static MenuProduct MENU2_MENU_PRODUCT1() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(2L);
        menuProduct.setQuantity(1);
        return menuProduct;
    }

    /**
     * REQUEST
     */
    public static Menu MENU1_REQUEST() {
        Menu menu = new Menu();
        menu.setName(MENU1_NAME);
        menu.setPrice(MENU1_PRICE);
        menu.setMenuGroupId(MENU1_MENU_GROUP_ID);
        menu.setMenuProducts(List.of(MENU1_MENU_PRODUCT1(), MENU1_MENU_PRODUCT2()));
        return menu;
    }

    public static Menu MENU2_REQUEST() {
        Menu menu = new Menu();
        menu.setName(MENU2_NAME);
        menu.setPrice(MENU2_PRICE);
        menu.setMenuGroupId(MENU2_MENU_GROUP_ID);
        menu.setMenuProducts(List.of(MENU2_MENU_PRODUCT1()));
        return menu;
    }
}
