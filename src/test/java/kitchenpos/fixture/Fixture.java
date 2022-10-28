package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuPrice;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductPrice;

public class Fixture {

    public static final MenuGroup 한마리메뉴 = new MenuGroup(1L, "한마리메뉴");
    public static final MenuGroup 두마리메뉴 = new MenuGroup(2L, "두마리메뉴");
    public static final Menu MENU_후라이드치킨 = new Menu(1L, "후라이드치킨", new MenuPrice(BigDecimal.valueOf(16000)), 1L, null);
    public static final Menu MENU_양념치킨 = new Menu(2L, "양념치킨", new MenuPrice(BigDecimal.valueOf(17000)), 1L, null);

    public static final Product PRODUCT_후라이드 = new Product(1L, "후라이드",
            new ProductPrice(BigDecimal.valueOf(16000)));
    public static final Product PRODUCT_양념치킨 = new Product(2L, "양념치킨",
            new ProductPrice(BigDecimal.valueOf(17000)));
}
