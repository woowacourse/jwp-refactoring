package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Menu;

public class MenuFixture {

    public static final Menu MENU_FIXTURE_소고기 = new Menu();
    public static final Menu MENU_FIXTURE_소국 = new Menu();

    static {
        MENU_FIXTURE_소고기.setName("소고기");
        MENU_FIXTURE_소고기.setPrice(BigDecimal.ONE);
        MENU_FIXTURE_소고기.setMenuGroupId(1L);
        MENU_FIXTURE_소국.setName("소국");
        MENU_FIXTURE_소국.setPrice(BigDecimal.TEN);
        MENU_FIXTURE_소국.setMenuGroupId(2L);
    }
}
