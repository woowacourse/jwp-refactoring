package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import kitchenpos.menu.vo.Price;

@SuppressWarnings("NonAsciiCharacters")
public class MenuFixture {

    private static final Price PRICE = Price.valueOf(BigDecimal.valueOf(1).setScale(2, RoundingMode.HALF_UP));

    public static Menu 메뉴(Long menuGroupId, List<MenuProduct> menuProducts) {
        return new Menu("menuName", PRICE, menuGroupId, MenuProducts.from(menuProducts));
    }

    public static Menu 메뉴(Long menuGroupId, BigDecimal price, List<MenuProduct> menuProducts) {
        return new Menu("menuName", Price.valueOf(price), menuGroupId, MenuProducts.from(menuProducts));
    }

    public static Menu 메뉴(Long menuId, Long menuGroupId, BigDecimal price, List<MenuProduct> menuProducts) {
        return new Menu(menuId, "menuName", Price.valueOf(price), menuGroupId, MenuProducts.from((menuProducts)));
    }
}
