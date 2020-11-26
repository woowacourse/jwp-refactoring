package kitchenpos.ui.dto;

import java.util.List;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.Table;

public class OrderAssembler {

    private OrderAssembler() {
    }

    public static Order assemble(OrderCreateRequest orderCreateRequest, Table table,
        List<Menu> menus) {
        List<OrderLineItem> orderLineItems = OrderLineItemAssembler
            .listAssemble(orderCreateRequest.getOrderLineItems(), menus);

        return Order.entityOf(table, OrderStatus.COOKING, orderLineItems);
    }
}
