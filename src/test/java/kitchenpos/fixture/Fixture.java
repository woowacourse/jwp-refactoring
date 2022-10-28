package kitchenpos.fixture;

import static kitchenpos.domain.order.OrderStatus.COOKING;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuPrice;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductPrice;
import kitchenpos.domain.tablegroup.TableGroup;

public class Fixture {

    public static final MenuGroup 한마리메뉴 = new MenuGroup(1L, "한마리메뉴");
    public static final MenuGroup 두마리메뉴 = new MenuGroup(2L, "두마리메뉴");
    public static final Menu MENU_후라이드치킨 = new Menu(1L, "후라이드치킨", new MenuPrice(BigDecimal.valueOf(16000)), 1L, null);
    public static final Menu MENU_양념치킨 = new Menu(2L, "양념치킨", new MenuPrice(BigDecimal.valueOf(17000)), 1L, null);

    public static final Product PRODUCT_후라이드 = new Product(1L, "후라이드",
            new ProductPrice(BigDecimal.valueOf(16000)));
    public static final Product PRODUCT_양념치킨 = new Product(2L, "양념치킨",
            new ProductPrice(BigDecimal.valueOf(17000)));
    public static final Order ORDER_첫번째_주문 = new Order(1L, 1L, COOKING, LocalDateTime.now(), null);
    public static final OrderTable ORDER_TABLE_삼인용_테이블 = new OrderTable(1L, null, 5, true);
    public static final TableGroup TABLE_GROUP_1번 = new TableGroup(1L, LocalDateTime.now());

}
