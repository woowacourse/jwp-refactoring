package kitchenpos.common;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public class MenuFixtures {

    /**
     * NAME
     */
    public static final String MENU1_NAME = "후라이드치킨";

    /**
     * PRICE
     */
    public static final BigDecimal MENU1_PRICE = BigDecimal.valueOf(16000).setScale(0, RoundingMode.UNNECESSARY);

    /**
     * MENU_GROUP_ID
     */
    public static final Long MENU1_MENU_GROUP_ID = 2L;

    /**
     * MENU_PRODUCT
     */
    public static MenuProduct MENU1_MENU_PRODUCT() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(2L);
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
        menu.setMenuProducts(List.of(MENU1_MENU_PRODUCT()));
        return menu;
    }
}
