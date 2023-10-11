package kitchenpos.common;

import kitchenpos.domain.MenuProduct;

public class MenuProductFixtures {

    /**
     * MENU_ID
     */
    public static final Long MENU_PRODUCT1_MENU_ID = 1L;

    /**
     * PRODUCT_ID
     */
    public static final Long MENU_PRODUCT1_PRODUCT_ID = 1L;

    /**
     * QUANTITY
     */
    public static final long MENU_PRODUCT1_QUANTITY = 1L;

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
}
