package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;

import java.math.BigDecimal;

public class MenuFixture {

    private static final Long ID = 1L;
    private static final String NAME = "이달의치킨";
    private static final BigDecimal PRICE = BigDecimal.valueOf(20_000);
    private static final MenuGroup MENU_GROUP = MenuGroupFixture.create();

    public static Menu create() {
        return create(ID, NAME, PRICE, MENU_GROUP);
    }

    public static Menu create(Long id, String name, BigDecimal price, MenuGroup menuGroup) {
        Menu menu = new Menu(id, name, price, menuGroup);

        return menu;
    }
}
