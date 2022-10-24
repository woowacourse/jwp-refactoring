package kitchenpos.support.fixture.domain;

import java.math.BigDecimal;
import kitchenpos.domain.Menu;

public enum MenuFixture {

    CHICKEN_1000("치킨", new BigDecimal(1000)),
    ;

    private final String name;
    private final BigDecimal price;

    MenuFixture(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Menu getMenu(Long menuGroupId) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        return menu;
    }

    public Menu getMenu(Long id, Long menuGroupId) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        return menu;
    }
}
