package kitchenpos;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class Fixture {

    public static BigDecimal MENU_PRODUCT_1_PRICE = BigDecimal.valueOf(1000);
    public static BigDecimal MENU_PRODUCT_2_PRICE = BigDecimal.valueOf(5000);
    public static int MENU_PRODUCT_1_QUANTITY = 10;
    public static int MENU_PRODUCT_2_QUANTITY = 3;

    public static MenuProduct getMenuProduct1() {
        return new MenuProduct(
                new Menu(),
                Product.of("wuga", MENU_PRODUCT_1_PRICE),
                MENU_PRODUCT_1_QUANTITY);
    }
    public static MenuProduct getMenuProduct2() {
        return new MenuProduct(
                new Menu(),
                Product.of("wuga", MENU_PRODUCT_2_PRICE),
                MENU_PRODUCT_2_QUANTITY);
    }

    public static Menu getMenu() {
        return Menu.of("wugas", BigDecimal.valueOf(25000), getMenuGroup(), List.of(getMenuProduct1(),
                getMenuProduct2()));
    }

    public static MenuGroup getMenuGroup() {
        return new MenuGroup("wugawuga");
    }
}
