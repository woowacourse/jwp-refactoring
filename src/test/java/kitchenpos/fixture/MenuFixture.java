package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.menu.domain.Menu;

public class MenuFixture {

    public static Menu of(Long menuGroupId, String name, BigDecimal price) {
        return new Menu(menuGroupId, name, price);
    }
}
