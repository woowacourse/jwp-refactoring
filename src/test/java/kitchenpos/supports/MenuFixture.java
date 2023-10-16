package kitchenpos.supports;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public class MenuFixture {

    private Long id = null;
    private String name = "기본 메뉴";
    private BigDecimal price = new BigDecimal(10_000);
    private Long menuGroupId = 1L;
    private List<MenuProduct> menuProducts;

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
        this.price = price;
        return this;
    }

    public MenuFixture price(int price) {
        return price(new BigDecimal(price));
    }

    public MenuFixture menuGroupId(Long menuGroupId) {
        this.menuGroupId = menuGroupId;
        return this;
    }

    public MenuFixture menuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
        return this;
    }

    public Menu build() {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);
        return menu;
    }
}
