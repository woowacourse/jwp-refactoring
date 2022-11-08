package kitchenpos.common.builder;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

public class MenuBuilder {

    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProduct> menuProducts;

    public MenuBuilder name(final String name) {
        this.name = name;
        return this;
    }

    public MenuBuilder price(final BigDecimal price) {
        this.price = price;
        return this;
    }

    public MenuBuilder menuGroupId(final Long menuGroupId) {
        this.menuGroupId = menuGroupId;
        return this;
    }

    public MenuBuilder menuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
        return this;
    }

    public Menu build() {
        return new Menu(name, price, menuGroupId, menuProducts);
    }
}
