package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.ArrayList;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Price;

public class MenuFixture {

    public static final Menu MENU_FIXTURE_1 = new Menu("소세트", new Price(BigDecimal.ONE), 1L, new ArrayList<>());
    public static final Menu MENU_FIXTURE_2 = new Menu("소국세트", new Price(BigDecimal.TEN), 2L, new ArrayList<>());
}
