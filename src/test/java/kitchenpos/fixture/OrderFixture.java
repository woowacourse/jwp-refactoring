package kitchenpos.fixture;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderStatusRequest;

public class OrderFixture {

    public static OrderRequest createOrderRequest(OrderTable orderTable, Menu... menus) {
        final List<OrderLineItemRequest> orderLineItems = new ArrayList<>();
        for (final Menu menu : menus) {
            orderLineItems.add(new OrderLineItemRequest(menu.getId(), 1));
        }
        return new OrderRequest(orderTable.getId(), orderLineItems);
    }

    public static OrderStatusRequest updatedOrderStatusRequest(String status) {
        return new OrderStatusRequest(status);
    }
}
