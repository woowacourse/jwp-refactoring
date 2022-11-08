package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.generic.Price;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;

public class MenuBuilder {

    private String name = "강정치킨";
    private BigDecimal price = BigDecimal.valueOf(14_000L);
    private Long menuGroupId;
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public MenuBuilder() {
    }

    public static MenuBuilder aMenu(Long menuGroupId) {
        return new MenuBuilder()
                .withMenuGroupId(menuGroupId);
    }

    public MenuBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public MenuBuilder withPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public MenuBuilder withMenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
        return this;
    }

    public Menu build() {
        return new Menu(name, new Price(price), menuGroupId, menuProducts);
    }

    private MenuBuilder withMenuGroupId(Long menuGroupId) {
        this.menuGroupId = menuGroupId;
        return this;
    }
}
