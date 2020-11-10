package kitchenpos.fixture;

import kitchenpos.domain.MenuProduct;

public class MenuProductFixture {

    public static final MenuProduct MENU_PRODUCT_FIXTURE_1 = new MenuProduct();
    public static final MenuProduct MENU_PRODUCT_FIXTURE_2 = new MenuProduct();
    public static final MenuProduct MENU_PRODUCT_FIXTURE_3 = new MenuProduct();

    static {
        MENU_PRODUCT_FIXTURE_1.setMenuId(1L);
        MENU_PRODUCT_FIXTURE_1.setProductId(1L);
        MENU_PRODUCT_FIXTURE_1.setQuantity(6);
        MENU_PRODUCT_FIXTURE_2.setMenuId(2L);
        MENU_PRODUCT_FIXTURE_2.setProductId(2L);
        MENU_PRODUCT_FIXTURE_2.setQuantity(3);
        MENU_PRODUCT_FIXTURE_3.setMenuId(1L);
        MENU_PRODUCT_FIXTURE_3.setProductId(2L);
        MENU_PRODUCT_FIXTURE_3.setQuantity(9);
    }
}
