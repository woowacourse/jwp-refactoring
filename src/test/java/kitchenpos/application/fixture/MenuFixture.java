package kitchenpos.application.fixture;

import static kitchenpos.application.fixture.MenuGroupFixture.MENU_GROUP_ID;
import static kitchenpos.application.fixture.ProductFixture.INVALID_PRODUCT_ID;
import static kitchenpos.application.fixture.ProductFixture.PRODUCT_ID_ONE;
import static kitchenpos.application.fixture.ProductFixture.PRODUCT_ID_THREE;
import static kitchenpos.application.fixture.ProductFixture.PRODUCT_ID_TWO;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public class MenuFixture {

    public static final Long MENU_FIRST_ID = 1L;
    public static final Long MENU_SECOND_ID = 2L;
    public static final Long MENU_THIRD_ID = 3L;
    public static final long MENU_PRICE = 9900L;
    public static final long MENU_INVALID_PRICE = -10L;
    public static final long MENU_EXPENSIVE_PRICE = 11001L;
    public static final String MENU_NAME = "빵";
    public static final List<MenuProduct> MENU_PRODUCTS = new ArrayList<>();
    public static final List<MenuProduct> INVALID_MENU_PRODUCTS = new ArrayList<>();
    public static final Menu UNSAVED_MENU = new Menu(MENU_NAME, MENU_PRICE, MENU_GROUP_ID, MENU_PRODUCTS);

    static {
        MenuProduct menuProduct1 = new MenuProduct(PRODUCT_ID_ONE, 2);
        MENU_PRODUCTS.add(menuProduct1);

        MenuProduct menuProduct2 = new MenuProduct(PRODUCT_ID_TWO, 3);
        MENU_PRODUCTS.add(menuProduct2);

        MenuProduct menuProduct3 = new MenuProduct(PRODUCT_ID_THREE, 1);
        MENU_PRODUCTS.add(menuProduct3);

        MenuProduct invalidMenuProduct = new MenuProduct(INVALID_PRODUCT_ID, 1);
        INVALID_MENU_PRODUCTS.add(invalidMenuProduct);
    }
}
