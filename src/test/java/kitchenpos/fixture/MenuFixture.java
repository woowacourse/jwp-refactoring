package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.dto.MenuRequest;

public class MenuFixture {

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

    public static Menu CHICKEN_SET_MENU_NON_ID = new Menu(
            null,
            "치킨+콜라 세트",
            BigDecimal.valueOf(11000),
            1L,
            List.of(CHICKEN_MENU_PRODUCT, COKE_MENU_PRODUCT)
    );

    public static Menu CHICKEN_SET_MENU = new Menu(
            1L,
            "치킨+콜라 세트",
            BigDecimal.valueOf(11000),
            1L,
            List.of(CHICKEN_MENU_PRODUCT, COKE_MENU_PRODUCT)
    );

    public static Menu createChickenSetMenuById(final Long id) {
        final Menu chickenSetMenu = new Menu(
                "치킨 세트",
                BigDecimal.valueOf(10000),
                1L,
                List.of(CHICKEN_MENU_PRODUCT)
        );
        chickenSetMenu.setId(id);
        return chickenSetMenu;
    }

    public static MenuRequest CHICKEN_SET_MENU_REQUEST = new MenuRequest(
            "치킨 세트",
            BigDecimal.valueOf(10000),
            1L,
            List.of(CHICKEN_MENU_PRODUCT)
    );
}
