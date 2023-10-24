package kitchenpos.common.fixtures;

import static kitchenpos.common.fixtures.ProductFixtures.PRODUCT1;
import static kitchenpos.common.fixtures.ProductFixtures.PRODUCT2;

import kitchenpos.menu.application.dto.MenuProductRequest;
import kitchenpos.menu.domain.MenuProduct;

public class MenuProductFixtures {

    /**
     * MENU_ID
     */
    public static final Long MENU_PRODUCT1_MENU_ID = 1L;
    public static final Long MENU_PRODUCT2_MENU_ID = 1L;
    public static final Long MENU_PRODUCT3_MENU_ID = 2L;

    /**
     * PRODUCT_ID
     */
    public static final Long MENU_PRODUCT1_PRODUCT_ID = 1L;
    public static final Long MENU_PRODUCT2_PRODUCT_ID = 2L;
    public static final Long MENU_PRODUCT3_PRODUCT_ID = 2L;

    /**
     * QUANTITY
     */
    public static final long MENU_PRODUCT1_QUANTITY = 2L;
    public static final long MENU_PRODUCT2_QUANTITY = 1L;
    public static final long MENU_PRODUCT3_QUANTITY = 1L;

    /**
     * REQUEST
     */
    public static MenuProductRequest MENU_PRODUCT1_REQUEST() {
        return new MenuProductRequest(MENU_PRODUCT1_PRODUCT_ID, MENU_PRODUCT1_QUANTITY);
    }

    public static MenuProductRequest MENU_PRODUCT2_REQUEST() {
        return new MenuProductRequest(MENU_PRODUCT2_PRODUCT_ID, MENU_PRODUCT2_QUANTITY);
    }

    public static MenuProductRequest MENU_PRODUCT3_REQUEST() {
        return new MenuProductRequest(MENU_PRODUCT3_PRODUCT_ID, MENU_PRODUCT3_QUANTITY);
    }

    /**
     * ENTITY
     */
    public static MenuProduct MENU_PRODUCT1() {
        return new MenuProduct(PRODUCT1(), MENU_PRODUCT1_QUANTITY);
    }

    public static MenuProduct MENU_PRODUCT2() {
        return new MenuProduct(PRODUCT2(), MENU_PRODUCT2_QUANTITY);
    }
}
