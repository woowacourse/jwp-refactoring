package kitchenpos.fixture;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.product.domain.Product;

import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.fixture.ProductFixture.PRODUCT_1;
import static kitchenpos.fixture.ProductFixture.PRODUCT_2;
import static kitchenpos.fixture.ProductFixture.PRODUCT_3;
import static kitchenpos.fixture.ProductFixture.PRODUCT_4;
import static kitchenpos.fixture.ProductFixture.PRODUCT_5;
import static kitchenpos.fixture.ProductFixture.PRODUCT_6;

public class MenuFixture {

    public static Menu MENU_1 =
            Menu.of("후라이드치킨", new BigDecimal("16000.00"), MenuGroupFixture.MENU_GROUP2,
                    new MenuProducts(List.of(new MenuProduct(null, null, PRODUCT_1, 1))));
    public static Menu MENU_2 = Menu.of("양념치킨", new BigDecimal("16000.00"), MenuGroupFixture.MENU_GROUP2,
            new MenuProducts(List.of(new MenuProduct(null, null, PRODUCT_2, 1))));
    public static Menu MENU_3 = Menu.of("반반치킨", new BigDecimal("16000.00"), MenuGroupFixture.MENU_GROUP2,
            new MenuProducts(List.of(new MenuProduct(null, null, PRODUCT_3, 1))));
    public static Menu MENU_4 = Menu.of("통구이", new BigDecimal("16000.00"), MenuGroupFixture.MENU_GROUP2,
            new MenuProducts(List.of(new MenuProduct(null, null, PRODUCT_4, 1))));
    public static Menu MENU_5 = Menu.of("간장치킨", new BigDecimal("17000.00"), MenuGroupFixture.MENU_GROUP2,
            new MenuProducts(List.of(new MenuProduct(null, null, PRODUCT_5, 1))));
    public static Menu MENU_6 = Menu.of("순살치킨", new BigDecimal("17000.00"), MenuGroupFixture.MENU_GROUP2,
            new MenuProducts(List.of(new MenuProduct(null, null, PRODUCT_6, 1))));

    private MenuFixture() {
    }

    public static Menu MENU_1(Product product) {
        MenuProducts menuProducts = new MenuProducts(List.of(new MenuProduct(null, null, product, 1)));
        Menu 후라이드치킨 = Menu.of("후라이드치킨", new BigDecimal("16000.00"), MenuGroupFixture.MENU_GROUP2, menuProducts);
        menuProducts.setMenu(후라이드치킨);
        return 후라이드치킨;
    }
}
