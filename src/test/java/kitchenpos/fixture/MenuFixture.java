package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public class MenuFixture {

    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProduct> menuProducts;

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
        Menu menu = new Menu(
            id,
            name,
            price,
            menuGroupId,
            menuProducts
        );
        return menu;
    }
}
