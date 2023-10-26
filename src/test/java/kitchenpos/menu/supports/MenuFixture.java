package kitchenpos.menu.supports;

import java.math.BigDecimal;
import kitchenpos.global.Price;
import kitchenpos.menu.domain.model.Menu;
import kitchenpos.menu.domain.model.MenuGroup;

public class MenuFixture {

    private Long id = null;
    private String name = "기본 메뉴";
    private Price price = new Price(new BigDecimal(10_000));
    private MenuGroup menuGroup = MenuGroupFixture.fixture().build();

    private MenuFixture() {
    }

    public static MenuFixture fixture() {
        return new MenuFixture();
    }

    public MenuFixture id(Long id) {
        this.id = id;
        return this;
    }

    public MenuFixture name(String name) {
        this.name = name;
        return this;
    }

    public MenuFixture price(BigDecimal price) {
        this.price = new Price(price);
        return this;
    }

    public MenuFixture price(int price) {
        return price(new BigDecimal(price));
    }

    public MenuFixture menuGroup(MenuGroup menuGroup) {
        this.menuGroup = menuGroup;
        return this;
    }

    public Menu build() {
        return new Menu(id, name, price, menuGroup);
    }
}
