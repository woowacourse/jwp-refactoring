package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.common.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroup;

public class MenuFixture {

    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProduct> menuProducts = new ArrayList<>();

    private MenuFixture() {
    }

    public static MenuFixture builder() {
        return new MenuFixture();
    }

    public MenuFixture withId(Long id) {
        this.id = id;
        return this;
    }

    public MenuFixture withName(String name) {
        this.name = name;
        return this;
    }

    public MenuFixture withPrice(Long price) {
        this.price = BigDecimal.valueOf(price);
        return this;
    }

    public MenuFixture withMenuGroupId(Long menuGroupId) {
        this.menuGroupId = menuGroupId;
        return this;
    }

    public MenuFixture withMenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
        return this;
    }

    public Menu build() {
        return new Menu(
            id,
            name,
            new Price(price),
            new MenuGroup(menuGroupId, "menuGroup"),
            menuProducts
        );
    }
}
