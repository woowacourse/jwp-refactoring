package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.domain.vo.MenuPrice;
import kitchenpos.menugroup.domain.MenuGroup;

public class MenuFixture {

    public static Menu 메뉴(final Long id, final String name, final BigDecimal price, final MenuGroup menuGroup,
                          final List<MenuProduct> menuProducts) {
        return new Menu(id, name, new MenuPrice(price), menuGroup.getId(), new MenuProducts(menuProducts));
    }
}
