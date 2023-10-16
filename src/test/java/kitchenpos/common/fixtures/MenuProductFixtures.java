package kitchenpos.common.fixtures;

import kitchenpos.domain.MenuProduct;

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
    public static MenuProduct MENU_PRODUCT1_REQUEST() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(MENU_PRODUCT1_MENU_ID);
        menuProduct.setProductId(MENU_PRODUCT1_PRODUCT_ID);
        menuProduct.setQuantity(MENU_PRODUCT1_QUANTITY);
        return menuProduct;
    }

    public static MenuProduct MENU_PRODUCT2_REQUEST() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(MENU_PRODUCT2_MENU_ID);
        menuProduct.setProductId(MENU_PRODUCT2_PRODUCT_ID);
        menuProduct.setQuantity(MENU_PRODUCT2_QUANTITY);
        return menuProduct;
    }

    public static MenuProduct MENU_PRODUCT3_REQUEST() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(MENU_PRODUCT3_MENU_ID);
        menuProduct.setProductId(MENU_PRODUCT3_PRODUCT_ID);
        menuProduct.setQuantity(MENU_PRODUCT3_QUANTITY);
        return menuProduct;
    }
}
