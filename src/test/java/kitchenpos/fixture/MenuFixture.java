package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.vo.Price;

public class MenuFixture {

    public static Menu 메뉴(final Long id, final String name, final BigDecimal price, final MenuGroup menuGroup,
                          final List<MenuProduct> menuProducts) {
        return new Menu(id, name, new Price(price), menuGroup, new MenuProducts(menuProducts));
    }
}
