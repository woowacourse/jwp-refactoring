package kitchenpos.support;

import java.math.BigDecimal;
import kitchenpos.domain.Menu;

public enum MenuFixture {

    MENU_1("메뉴1", 10000);

    private final String name;
    private final int price;

    MenuFixture(final String name, final int price) {
        this.name = name;
        this.price = price;
    }

    public Menu 생성(final long menuGroupId) {
        return new Menu(this.name, new BigDecimal(this.price), menuGroupId);
    }
}
