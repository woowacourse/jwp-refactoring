package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;

public class MenuFixture {

    public static Menu getMenuRequest(final Long menuGroupId) {
        return getMenuRequest("후라이드+후라이드", 10000, menuGroupId, new LinkedList<>());
    }

    public static Menu getMenuRequest(final int price) {
        return getMenuRequest("후라이드+후라이드", price, 1L, new LinkedList<>());
    }

    public static Menu getMenuRequest(final int price, final List<MenuProduct> menuProducts) {
        return getMenuRequest("후라이드+후라이드", price, 1L, menuProducts);
    }

    public static Menu getMenuRequest(final String name,
                                      final int price,
                                      final Long menuGroupId,
                                      final List<MenuProduct> menuProducts) {
        return new Menu(name, BigDecimal.valueOf(price), menuGroupId, menuProducts);
    }

    public static Menu getMenu(final Long id) {
        return new Menu(id, "후라이드+후라이드", BigDecimal.valueOf(17000), null, new LinkedList<>());
    }

    public static MenuProduct getMenuProductRequest(final Long productId) {
        return getMenuProductRequest(productId, 1L, 1);
    }

    public static MenuProduct getMenuProductRequest(final Long productId, final Long menuId, final long quantity) {
        return new MenuProduct(menuId, productId, quantity);
    }

    public static MenuProduct getMenuProduct(final Long id) {
        return getMenuProduct(id, 1L, 1L, 1L);
    }

    public static MenuProduct getMenuProduct(final Long id,
                                             final Long productId,
                                             final Long menuId,
                                             final long quantity) {
        return new MenuProduct(id, menuId, productId, quantity);
    }

    public static MenuGroup getMenuGroupRequest(final Long id) {
        return new MenuGroup(id, "추천메뉴");
    }
}
