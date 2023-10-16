package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.List;

public class MenuFixture {

    public static Menu MENU_1 =
            new Menu("후라이드치킨", new BigDecimal("16000.00"), 2L,
                    List.of(new MenuProduct(null, null, 1L, 1)));
    public static Menu MENU_2 = new Menu("양념치킨", new BigDecimal("16000.00"), 2L,
            List.of(new MenuProduct(null, null, 2L, 1)));
    public static Menu MENU_3 = new Menu("반반치킨", new BigDecimal("16000.00"), 2L,
            List.of(new MenuProduct(null, null, 3L, 1)));
    public static Menu MENU_4 = new Menu("통구이", new BigDecimal("16000.00"), 2L,
            List.of(new MenuProduct(null, null, 4L, 1)));
    public static Menu MENU_5 = new Menu("간장치킨", new BigDecimal("17000.00"), 2L,
            List.of(new MenuProduct(null, null, 5L, 1)));
    public static Menu MENU_6 = new Menu("순살치킨", new BigDecimal("17000.00"), 2L,
            List.of(new MenuProduct(null, null, 6L, 1)));

    private MenuFixture() {
    }
}
