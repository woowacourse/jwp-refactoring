package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.List;

public class MenuFixture {

    public static Menu 메뉴_생성(final String name,
                             final BigDecimal price,
                             final Long menuGroupId,
                             final List<MenuProduct> menuProducts) {
        return new Menu(name, price, menuGroupId, menuProducts);
    }

    public static Menu 메뉴_생성(final Long menuGroupId,
                             final List<MenuProduct> menuProducts) {
        return new Menu("테스트 메뉴", BigDecimal.valueOf(10000), menuGroupId, menuProducts);
    }
}
