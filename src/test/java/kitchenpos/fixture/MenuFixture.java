package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.domain.Menu;

public class MenuFixture {
    public static Menu menu() {
        return new Menu(0L, "name", BigDecimal.ZERO, 0L, Arrays.asList(MenuProductFixture.menuProduct()));
    }
}
