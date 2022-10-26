package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.MenuProductRequest;

public class ServiceTestFixture {
    public static final MenuGroup MENU_GROUP1 =  new MenuGroup(1L,  "한마리메뉴");

    public static final MenuProduct MENU_PRODUCT1 = new MenuProduct(1L, 1L);
    public static final MenuProduct MENU_PRODUCT2 = new MenuProduct( 2L, 1L);

    public static final MenuProductRequest MENU_PRODUCT_REQUEST1 = new MenuProductRequest(1L, 1L);
    public static final MenuProductRequest MENU_PRODUCT_REQUEST2 = new MenuProductRequest( 2L, 1L);

    public static final List<MenuProduct> MENU_PRODUCTS = List.of(MENU_PRODUCT1, MENU_PRODUCT2);
    public static final List<MenuProductRequest> MENU_PRODUCT_REQUESTS = List.of(MENU_PRODUCT_REQUEST1, MENU_PRODUCT_REQUEST2);

    public static final OrderLineItem ORDER_LINE_ITEM1 = new OrderLineItem(1L, 1L, 1);
    public static final OrderLineItem ORDER_LINE_ITEM2 = new OrderLineItem(1L, 2L, 1);
    public static final OrderLineItem ORDER_LINE_ITEM_NOT_EXIST_MENU_ID = new OrderLineItem(1L, 100L, 1);

    public static final List<OrderLineItem> ORDER_LINE_ITEMS = List.of(ORDER_LINE_ITEM1, ORDER_LINE_ITEM2);

    public static final OrderTable ORDER_TABLE1 = new OrderTable(1L,   0, true);
    public static final OrderTable ORDER_TABLE2 = new OrderTable(2L,   0, true);
    public static final OrderTable ORDER_TABLE3 = new OrderTable(3L,  0, true);
    public static final OrderTable INVALID_ORDER_TABLE = new OrderTable(100L, 1L, 0, true);
    public static final OrderTable NOT_EMPTY_ORDER_TABLE = new OrderTable(100L, 1L, 0, false);
    public static final OrderTable INVALID_GROUPID_ORDER_TABLE = new OrderTable(100L, 100L, 0, false);

    public static final List<OrderTable> ORDER_TABLES = List.of(ORDER_TABLE1, ORDER_TABLE2, ORDER_TABLE3);
}
