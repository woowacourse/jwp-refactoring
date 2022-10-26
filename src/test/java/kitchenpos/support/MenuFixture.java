package kitchenpos.support;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public enum MenuFixture {

    MENU_PRICE_10000("메뉴1", 10000);

    private final String name;
    private final int price;

    MenuFixture(final String name, final int price) {
        this.name = name;
        this.price = price;
    }

    public Menu 생성(final long menuGroupId) {
        return new Menu(this.name, new BigDecimal(this.price), menuGroupId);
    }

    public Menu 생성(final long menuGroupId, final List<MenuProduct> menuProducts) {
        return new Menu(this.name, new BigDecimal(this.price), menuGroupId, menuProducts);
    }
}
