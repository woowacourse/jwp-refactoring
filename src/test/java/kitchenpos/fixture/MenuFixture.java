package kitchenpos.fixture;

import static kitchenpos.fixture.MenuProductFixture.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import kitchenpos.common.TestObjectUtils;
import kitchenpos.domain.Menu;

public class MenuFixture {
    public static final Menu MENU1 = TestObjectUtils.createMenu(1L, "후라이드치킨",
            BigDecimal.valueOf(16000), 2L, Collections.singletonList(MENU_PRODUCT1));

    public static final Menu MENU2 = TestObjectUtils.createMenu(2L, "양념치킨",
            BigDecimal.valueOf(16000), 2L, Collections.singletonList(MENU_PRODUCT2));

    public static final List<Menu> MENUS = Arrays.asList(MENU1, MENU2);

    public static final Menu MENU_PRICE_NULL = TestObjectUtils.createMenu(1L, "후라이드치킨",
            null, 2L, Collections.singletonList(MENU_PRODUCT1));

    public static final Menu MENU_PRICE_NEGATIVE = TestObjectUtils.createMenu(1L, "후라이드치킨",
            BigDecimal.valueOf(-1), 2L, Collections.singletonList(MENU_PRODUCT1));
}
