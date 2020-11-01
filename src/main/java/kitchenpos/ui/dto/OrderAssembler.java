package kitchenpos.ui.dto;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.Table;

public class OrderAssembler {

    private OrderAssembler() {
    }

    public static Order assemble(OrderCreateRequest orderCreateRequest, Table table,
        List<Menu> menus) {
        return Order.entityOf(table, OrderStatus.COOKING.name(), LocalDateTime.now(),
            OrderLineItemAssembler.listAssemble(orderCreateRequest.getOrderLineItems(), menus));
    }
}
