package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.domain.Menu;

public class MenuFixture {
    private static final Menu menu = new Menu(0L, "name", BigDecimal.ZERO, 0L,
            Arrays.asList(MenuProductFixture.menuProduct()));

    public static Menu menu() {
        return menu;
    }
}
