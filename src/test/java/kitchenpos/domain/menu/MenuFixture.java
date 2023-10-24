package kitchenpos.domain.menu;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import kitchenpos.vo.Money;

@SuppressWarnings("NonAsciiCharacters")
public class MenuFixture {

    public static Menu 메뉴(Long menuGroupId) {
        return new Menu(
                "menuName",
                Money.valueOf(BigDecimal.valueOf(1).setScale(2, RoundingMode.HALF_UP)),
                menuGroupId,
                MenuProducts.from(List.of())
        );
    }

    public static Menu 메뉴(Long menuGroupId, List<MenuProduct> menuProducts) {
        return new Menu(
                "menuName",
                Money.valueOf(BigDecimal.valueOf(1).setScale(2, RoundingMode.HALF_UP)),
                menuGroupId,
                MenuProducts.from(menuProducts)
        );
    }

    public static Menu 메뉴(Long menuGroupId, BigDecimal price, List<MenuProduct> menuProducts) {
        return new Menu(
                "menuName",
                Money.valueOf(price),
                menuGroupId,
                MenuProducts.from(menuProducts)
        );
    }

    public static Menu 메뉴(Long menuId, Long menuGroupId, BigDecimal price, List<MenuProduct> menuProducts) {
        return new Menu(
                menuId,
                "menuName",
                Money.valueOf(price),
                menuGroupId,
                MenuProducts.from((menuProducts))
        );
    }
}
