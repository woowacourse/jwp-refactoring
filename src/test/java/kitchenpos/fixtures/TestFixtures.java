package kitchenpos.fixtures;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import kitchenpos.ui.dto.MenuCreateRequest;
import kitchenpos.ui.dto.MenuGroupCreateRequest;
import kitchenpos.ui.dto.MenuProductCreateRequest;

@SuppressWarnings("NonAsciiCharacters")
public class TestFixtures {

    private TestFixtures() {
    }

    public static Product 상품_생성(final String name, final BigDecimal price) {
        return new Product(name, price);
    }

    public static MenuGroup 메뉴_그룹_생성(final String name) {
        return new MenuGroup(name);
    }

    public static MenuGroupCreateRequest 메뉴_그룹_생성_요청(final String name) {
        return new MenuGroupCreateRequest(name);
    }

    public static Menu 메뉴_생성(final String name, final BigDecimal price,
                             final Long menuGroupId, final List<MenuProduct> menuProducts) {
        return new Menu(name, price, null, menuProducts);
    }

    public static MenuCreateRequest 메뉴_생성_요청(final String name,
                                             final BigDecimal price,
                                             final Long menuGroupId,
                                             final List<MenuProductCreateRequest> menuProductCreateRequests) {
        return new MenuCreateRequest(name, price, menuGroupId, menuProductCreateRequests);
    }

    public static MenuProduct 메뉴_상품_생성(final Long menuId, final Long productId, final long quantity) {
        return new MenuProduct(null, null, quantity);
    }

    public static MenuProductCreateRequest 메뉴_상품_생성_요청(final Long menuId,
                                                       final Long productId,
                                                       final long quantity) {
        return new MenuProductCreateRequest(1L, quantity);
    }

    public static Order 주문_생성(final Long orderTableId, final String orderStatus,
                              final LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(orderStatus);
        order.setOrderedTime(orderedTime);
        order.setOrderLineItems(orderLineItems);
        return order;
    }

    public static OrderLineItem 주문_항목_생성(final Long orderId, final Long menuId, final long quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(orderId);
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }

    public static OrderTable 주문_테이블_생성(final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        return orderTable;
    }

    public static TableGroup 단체_지정_생성(final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(createdDate);
        tableGroup.setOrderTables(orderTables);
        return tableGroup;
    }
}
