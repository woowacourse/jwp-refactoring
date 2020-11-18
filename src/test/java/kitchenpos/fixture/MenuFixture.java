package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Price;

import java.math.BigDecimal;

public class MenuFixture {

    private static final String MENU_NAME = "후라이드 치킨";

    private static Menu createMenu(Long id, Price price, MenuGroup menuGroup) {
        return new Menu(id, MENU_NAME, price, menuGroup);
    }

    public static Menu createMenuWithId(Long id) {
        return createMenu(id, null, null);
    }

    public static Menu createMenuWithPrice(BigDecimal price) {
        return createMenu(null, new Price(price), null);
    }

    public static Menu createMenuWithMenuGroup(MenuGroup menuGroup) {
        return createMenu(null, new Price(BigDecimal.ONE), menuGroup);
    }

}
