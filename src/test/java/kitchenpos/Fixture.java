package kitchenpos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;

public class Fixture {

    public static final OrderLineItem ORDER_LINE_ITEM = new OrderLineItem(1L, 1L);
    public static final Order ORDER =  new Order(1L, OrderStatus.COOKING.name(),
            LocalDateTime.now(), List.of(ORDER_LINE_ITEM));

    public static final MenuProduct MENU_PRODUCT = new MenuProduct(1L, 1L,
            1L, 2L);
    public static final Menu MENU =  new Menu(1L, "후라이드+후라이드",
            BigDecimal.valueOf(19000), 1L, List.of(MENU_PRODUCT));

    public static final Product PRODUCT = new Product(1L, "강정치킨",
            BigDecimal.valueOf(17000));

    public static final MenuGroup MENU_GROUP = new MenuGroup(1L, "추천메뉴");

    public static final OrderTable ORDER_TABLE1 = new OrderTable(1L,  0, true);
    public static final OrderTable ORDER_TABLE2 = new OrderTable(2L, 1, true);

    public static final TableGroup TABLE_GROUP = new TableGroup(1L, LocalDateTime.now(), List.of(ORDER_TABLE1, ORDER_TABLE2));
}
