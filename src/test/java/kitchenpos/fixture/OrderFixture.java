package kitchenpos.fixture;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderStatusRequest;

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
