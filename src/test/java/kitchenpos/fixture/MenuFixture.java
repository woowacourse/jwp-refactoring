package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.fixture.FixtureUtil.listAllInDatabaseFrom;
import static kitchenpos.fixture.FixtureUtil.pushing;

@SuppressWarnings("NonAsciiCharacters")
public abstract class MenuFixture {

    @InDatabase
    public static Menu 후라이드치킨() {
        var menuProduct = pushing(new MenuProduct(), 1L, 1L, 1L, 1L);
        return pushing(new Menu(), 1L, "후라이드치킨", new BigDecimal("16000.00"), 2L, List.of(menuProduct));
    }

    @InDatabase
    public static Menu 양념치킨() {
        var menuProduct = pushing(new MenuProduct(), 2L, 2L, 2L, 1L);
        return pushing(new Menu(), 2L, "양념치킨", new BigDecimal("16000.00"), 2L, List.of(menuProduct));
    }

    @InDatabase
    public static Menu 반반치킨() {
        var menuProduct = pushing(new MenuProduct(), 3L, 3L, 3L, 1L);
        return pushing(new Menu(), 3L, "반반치킨", new BigDecimal("16000.00"), 2L, List.of(menuProduct));
    }

    @InDatabase
    public static Menu 통구이() {
        var menuProduct = pushing(new MenuProduct(), 4L, 4L, 4L, 1L);
        return pushing(new Menu(), 4L, "통구이", new BigDecimal("16000.00"), 2L, List.of(menuProduct));
    }

    @InDatabase
    public static Menu 간장치킨() {
        var menuProduct = pushing(new MenuProduct(), 5L, 5L, 5L, 1L);
        return pushing(new Menu(), 5L, "간장치킨", new BigDecimal("17000.00"), 2L, List.of(menuProduct));
    }

    @InDatabase
    public static Menu 순살치킨() {
        var menuProduct = pushing(new MenuProduct(), 6L, 6L, 6L, 1L);
        return pushing(new Menu(), 6L, "순살치킨", new BigDecimal("17000.00"), 2L, List.of(menuProduct));
    }

    public static Menu 후라이드_두마리() {
        var menuProduct = new MenuProduct();
        menuProduct.setSeq(7L);
        menuProduct.setMenuId(7L);
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(2L);
        return pushing(new Menu(), 7L, "후라이드_양념_두마리", new BigDecimal("32000.00"), 1L, List.of(menuProduct));
    }

    public static List<Menu> listAllInDatabase() {
        return listAllInDatabaseFrom(MenuFixture.class, Menu.class);
    }
}
