package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public class MenuFixture {

    public static Menu createChickenSetMenuById(final Long id) {
        final Menu chickenSetMenu = new Menu(
                "치킨+콜라 세트",
                BigDecimal.valueOf(10000),
                1L,
                List.of(CHICKEN_MENU_PRODUCT)
        );
        chickenSetMenu.setId(id);
        return chickenSetMenu;
    }

    public static Menu CHICKEN_SET_MENU_NON_ID = new Menu(
            null,
            "치킨+콜라 세트",
            BigDecimal.valueOf(11000),
            1L,
            new ArrayList<>()
    );

    public static Menu CHICKEN_SET_MENU = new Menu(
            1L,
            "치킨+콜라 세트",
            BigDecimal.valueOf(11000),
            1L,
            new ArrayList<>()
    );

    public static MenuProduct CHICKEN_MENU_PRODUCT = new MenuProduct(
            1L,
            1L,
            1L,
            1
    );

    public static MenuProduct COKE_MENU_PRODUCT = new MenuProduct(
            2L,
            1L,
            2L,
            1
    );

    static {
        CHICKEN_SET_MENU.setMenuProducts(List.of(CHICKEN_MENU_PRODUCT, COKE_MENU_PRODUCT));
    }
}
