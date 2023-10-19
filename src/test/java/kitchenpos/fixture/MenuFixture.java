package kitchenpos.fixture;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;

import java.math.BigDecimal;
import java.util.List;

public class MenuFixture {

    public static Menu 아메리카노(Long menuGroupId, List<MenuProduct> menuProducts) {
        return new Menu("아메리카노", BigDecimal.valueOf(5600), menuGroupId, menuProducts);
    }
}
