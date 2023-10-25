package kitchenpos.common.fixtures;

import static kitchenpos.common.fixtures.ProductFixtures.PRODUCT1_PRICE;
import static kitchenpos.common.fixtures.ProductFixtures.PRODUCT2_PRICE;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.application.dto.MenuCreateRequest;
import kitchenpos.menu.application.dto.MenuProductRequest;

public class MenuFixtures {

    /**
     * NAME
     */
    public static final String MENU1_NAME = "후라이드치킨 & 양념치킨";
    public static final String MENU2_NAME = "양념치킨";

    /**
     * PRICE
     */
    public static final BigDecimal MENU1_PRICE = PRODUCT1_PRICE.multiply(
                    BigDecimal.valueOf(MENU1_MENU_PRODUCT1_REQUEST().getQuantity()))
            .add(PRODUCT2_PRICE.multiply(BigDecimal.valueOf(MENU1_MENU_PRODUCT2_REQUEST().getQuantity())));

    public static final BigDecimal MENU2_PRICE = PRODUCT1_PRICE.multiply(
            BigDecimal.valueOf(MENU2_MENU_PRODUCT1_REQUEST().getQuantity()));

    /**
     * MENU_GROUP_ID
     */
    public static final Long MENU1_MENU_GROUP_ID = 1L;
    public static final Long MENU2_MENU_GROUP_ID = 2L;

    /**
     * MENU_PRODUCT
     */
    public static MenuProductRequest MENU1_MENU_PRODUCT1_REQUEST() {
        return new MenuProductRequest(1L, 2L);
    }

    public static MenuProductRequest MENU1_MENU_PRODUCT2_REQUEST() {
        return new MenuProductRequest(2L, 1L);
    }

    public static MenuProductRequest MENU2_MENU_PRODUCT1_REQUEST() {
        return new MenuProductRequest(2L, 1L);
    }

    /**
     * REQUEST
     */
    public static MenuCreateRequest MENU1_REQUEST() {
        return new MenuCreateRequest(MENU1_NAME, MENU1_PRICE, MENU1_MENU_GROUP_ID,
                List.of(MENU1_MENU_PRODUCT1_REQUEST(), MENU1_MENU_PRODUCT1_REQUEST()));
    }

    public static MenuCreateRequest MENU2_REQUEST() {
        return new MenuCreateRequest(MENU1_NAME, MENU1_PRICE, MENU1_MENU_GROUP_ID,
                List.of(MENU2_MENU_PRODUCT1_REQUEST()));
    }
}
