package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public class MenuFixture {

    public static Menu 메뉴(final Long id, final String name, final BigDecimal price, final Long menuGroupId,
                          final List<MenuProduct> menuProducts) {
        return new Menu(id, name, price, menuGroupId, menuProducts);
    }
}
