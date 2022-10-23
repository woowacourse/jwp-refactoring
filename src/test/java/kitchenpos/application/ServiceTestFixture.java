package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.OrderLineItem;

public class ServiceTestFixture {
    public static final MenuGroup MENU_GROUP1 =  new MenuGroup(1L,  "한마리메뉴");

    public static final MenuProduct MENU_PRODUCT1 = new MenuProduct(1L, 1L);
    public static final MenuProduct MENU_PRODUCT2 = new MenuProduct( 2L, 1L);

    public static final List<MenuProduct> MENU_PRODUCTS = List.of(MENU_PRODUCT1, MENU_PRODUCT2);

    public static final OrderLineItem ORDER_LINE_ITEM1 = new OrderLineItem(1L, 1L, 1);
    public static final OrderLineItem ORDER_LINE_ITEM2 = new OrderLineItem(1L, 2L, 1);
    public static final OrderLineItem ORDER_LINE_ITEM_NOT_EXIST_MENU_ID = new OrderLineItem(1L, 100L, 1);

    public static final List<OrderLineItem> ORDER_LINE_ITEMS = List.of(ORDER_LINE_ITEM1, ORDER_LINE_ITEM2);

}
