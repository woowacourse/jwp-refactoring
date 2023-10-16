package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Consumer;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public enum MenuFixture {

    LUNCH_SPECIAL(1L, "Lunch Special", BigDecimal.valueOf(30000L), 1L,
        List.of(MenuProductFixture.FRIED_CHICKEN_MENU_PRODUCT.toEntity()));

    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final List<MenuProduct> menuProducts;

    MenuFixture(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static Menu computeDefaultMenu(Consumer<Menu> consumer) {
        Menu menu = new Menu();
        menu.setId(1L);
        menu.setName("후라이드치킨");
        menu.setPrice(BigDecimal.valueOf(0L));
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(List.of(MenuProductFixture.FRIED_CHICKEN_MENU_PRODUCT.toEntity()));
        consumer.accept(menu);
        return menu;
    }

    public Menu toEntity() {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);
        return menu;
    }
}
