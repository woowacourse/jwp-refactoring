package support.fixture;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu_group.MenuGroup;
import kitchenpos.domain.menu_product.MenuProduct;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class MenuBuilder {

    private static int sequence = 1;

    private String name;
    private BigDecimal price;
    private MenuGroup menuGroup;
    private List<MenuProduct> menuProducts;

    public MenuBuilder() {
        this.name = "메뉴" + sequence;
        this.price = BigDecimal.valueOf(1000 * sequence);
        this.menuGroup = null;
        this.menuProducts = Collections.emptyList();

        sequence++;
    }

    public MenuBuilder setName(final String name) {
        this.name = name;
        return this;
    }

    public MenuBuilder setPrice(final BigDecimal price) {
        this.price = price;
        return this;
    }

    public MenuBuilder setMenuGroup(final MenuGroup menuGroup) {
        this.menuGroup = menuGroup;
        return this;
    }

    public MenuBuilder setMenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
        return this;
    }

    public Menu build() {
        return new Menu(name, price, menuGroup, menuProducts);
    }
}
