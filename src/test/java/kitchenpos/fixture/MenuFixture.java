package kitchenpos.fixture;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.product.Price;

import java.math.BigDecimal;

import static kitchenpos.fixture.MenuGroupFixture.createMenuGroupWitId;

public class MenuFixture {

    private static final String MENU_NAME = "후라이드 치킨";

    private static Menu createMenu(Long id, Price price, MenuGroup menuGroup) {
        return new Menu(id, MENU_NAME, price, menuGroup);
    }

    public static Menu createMenuWithId(Long id) {
        return createMenu(id, new Price(BigDecimal.ONE), createMenuGroupWitId(1L));
    }

    public static Menu createMenuWithoutId() {
        return createMenu(null, null, null);
    }

    public static Menu createMenuWithPrice(BigDecimal price) {
        return createMenu(null, new Price(price), null);
    }

    public static Menu createMenuWithMenuGroup(MenuGroup menuGroup) {
        return createMenu(null, new Price(BigDecimal.ONE), menuGroup);
    }

}
