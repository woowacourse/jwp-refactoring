package kitchenpos.support;

import java.math.BigDecimal;
import kitchenpos.domain.Menu;

public enum MenuFixture {

    MENU_1("메뉴1", 10000);

    private String name;
    private int price;

    MenuFixture(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public Menu 생성(long menuGroupId) {
        return new Menu(this.name, new BigDecimal(this.price), menuGroupId);
    }
}
