package kitchenpos.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

import kitchenpos.domain.*;

public class Fixtures {
    public static final MenuGroup MENU_GROUP = new MenuGroup(null, "메뉴 그룹");
    public static final Product PRODUCT = new Product(null, "당수육", BigDecimal.valueOf(10000));
    public static final MenuProduct MENU_PRODUCT = new MenuProduct(null, 1L, 1L, 1L);
    public static final Menu MENU = new Menu(null, "메뉴", BigDecimal.valueOf(10000), 1L, Collections.singletonList(MENU_PRODUCT));

    public static final OrderTable FIRST_ORDER_TABLE = new OrderTable(null, null, 1, true);
    public static final OrderTable SECOND_ORDER_TABLE = new OrderTable(null, null, 1, true);
    public static final TableGroup TABLE_GROUP = new TableGroup(null, LocalDateTime.now(), new ArrayList<>());

    public static final OrderLineItem FIRST_ORDER_LINE_ITEM = new OrderLineItem(null, 1L, 1L, 1L);
    public static final Order COOKING_ORDER = new Order(null, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), Collections.singletonList(FIRST_ORDER_LINE_ITEM));

    public static final OrderLineItem SECOND_ORDER_LINE_ITEM = new OrderLineItem(null, 2L, 1L, 1L);
    public static final Order COMPLETION_ORDER = new Order(null, 1L, OrderStatus.COMPLETION.name(), LocalDateTime.now(), Collections.singletonList(SECOND_ORDER_LINE_ITEM));
}
