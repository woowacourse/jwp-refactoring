package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Menu;

public class MenuFixture {

    public static final Menu MENU_FIXTURE_1 = new Menu();
    public static final Menu MENU_FIXTURE_2 = new Menu();

    static {
        MENU_FIXTURE_1.setName("소세트");
        MENU_FIXTURE_1.setPrice(BigDecimal.ONE);
        MENU_FIXTURE_1.setMenuGroupId(1L);
        MENU_FIXTURE_2.setName("소국세트");
        MENU_FIXTURE_2.setPrice(BigDecimal.TEN);
        MENU_FIXTURE_2.setMenuGroupId(2L);
    }
}
