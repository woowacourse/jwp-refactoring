package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Price;

import java.math.BigDecimal;
import java.util.List;

public class MenuFixture {

    public static Menu MENU(String name, Long price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        return new Menu(name,
                new Price(BigDecimal.valueOf(price)),
                menuGroup,
                menuProducts);
    }
}
