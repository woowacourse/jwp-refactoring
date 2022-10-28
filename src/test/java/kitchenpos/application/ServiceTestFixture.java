package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderTableRequest;

public class ServiceTestFixture {
    public static final MenuGroup MENU_GROUP1 = new MenuGroup(1L, "한마리메뉴");

    public static final MenuProduct MENU_PRODUCT1 = new MenuProduct(1L, 1L);
    public static final MenuProduct MENU_PRODUCT2 = new MenuProduct(2L, 1L);

    public static final MenuProductRequest MENU_PRODUCT_REQUEST1 = new MenuProductRequest(1L, 1L);
    public static final MenuProductRequest MENU_PRODUCT_REQUEST2 = new MenuProductRequest(2L, 1L);

    public static final List<MenuProduct> MENU_PRODUCTS = List.of(MENU_PRODUCT1, MENU_PRODUCT2);
    public static final List<MenuProductRequest> MENU_PRODUCT_REQUESTS = List.of(MENU_PRODUCT_REQUEST1,
            MENU_PRODUCT_REQUEST2);

    public static final OrderLineItemRequest ORDER_LINE_ITEM_REQUEST1 = new OrderLineItemRequest(1L, 1L, 1L, 1);
    public static final OrderLineItemRequest ORDER_LINE_ITEM_REQUEST2 = new OrderLineItemRequest(2L, 1L, 2L, 1);

    public static final OrderLineItemRequest ORDER_LINE_ITEM_REQUEST_NOT_EXIST_MENU_ID = new OrderLineItemRequest(3L,
            1L, 100L, 1);

    public static final List<OrderLineItemRequest> ORDER_LINE_ITEM_REQUESTS = List.of(ORDER_LINE_ITEM_REQUEST1,
            ORDER_LINE_ITEM_REQUEST2);

    public static final OrderLineItem ORDER_LINE_ITEM1 = new OrderLineItem(1L, 1L, 1);
    public static final OrderLineItem ORDER_LINE_ITEM2 = new OrderLineItem(1L, 2L, 1);
    public static final OrderLineItem ORDER_LINE_ITEM_NOT_EXIST_MENU_ID = new OrderLineItem(1L, 100L, 1);

    public static final List<OrderLineItem> ORDER_LINE_ITEMS = List.of(ORDER_LINE_ITEM1, ORDER_LINE_ITEM2);

    public static final OrderTable ORDER_TABLE1 = new OrderTable(1L, 0, true);
    public static final OrderTable ORDER_TABLE2 = new OrderTable(2L, 0, true);
    public static final OrderTable ORDER_TABLE3 = new OrderTable(3L, 0, true);
    public static final OrderTable INVALID_ORDER_TABLE = new OrderTable(100L, 1L, 0, true);
    public static final OrderTable NOT_EMPTY_ORDER_TABLE = new OrderTable(100L, 1L, 0, false);
    public static final OrderTable INVALID_GROUPID_ORDER_TABLE = new OrderTable(100L, 100L, 0, false);

    public static final OrderTableRequest ORDER_TABLE_REQUEST1 = new OrderTableRequest(1L);
    public static final OrderTableRequest ORDER_TABLE_REQUEST2 = new OrderTableRequest(2L);
    public static final OrderTableRequest ORDER_TABLE_REQUEST3 = new OrderTableRequest(3L);
    public static final OrderTableRequest INVALID_ORDER_TABLE_REQUEST = new OrderTableRequest(100L);
    public static final OrderTableRequest INVALID_GROUPID_ORDER_TABLE_REQUEST = new OrderTableRequest(100L);

    public static final List<OrderTableRequest> ORDER_TABLE_REQUESTS = List.of(ORDER_TABLE_REQUEST1,
            ORDER_TABLE_REQUEST2, ORDER_TABLE_REQUEST3);

    public static final List<OrderTable> ORDER_TABLES = List.of(ORDER_TABLE1, ORDER_TABLE2, ORDER_TABLE3);
}
