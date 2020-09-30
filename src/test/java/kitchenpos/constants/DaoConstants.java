package kitchenpos.constants;

import java.math.BigDecimal;

public class DaoConstants {

    private DaoConstants() {
    }

    public static final String TEST_MENU_GROUP_NAME = "테스트그룹메뉴";

    public static final Long TEST_WRONG_MENU_ID = -1L;
    public static final String TEST_MENU_NAME = "테스트메뉴";
    public static final BigDecimal TEST_MENU_PRICE = BigDecimal.valueOf(1_000);

    public static final String TEST_PRODUCT_NAME = "테스트상품";
    public static final BigDecimal TEST_PRODUCT_PRICE = BigDecimal.valueOf(10_000);

}
