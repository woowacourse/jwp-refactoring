package kitchenpos.fixture;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderStatusRequest;

public class OrderFixture {

    public static OrderRequest createOrderRequest(OrderTable orderTable, Menu... menus) {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (Menu menu : menus) {
            OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setMenuId(menu.getId());
            orderLineItem.setQuantity(1);
            orderLineItems.add(orderLineItem);
        }
        return new OrderRequest(orderTable.getId(), orderLineItems);
    }

    public static OrderStatusRequest updatedOrderStatusRequest(String status) {
        return new OrderStatusRequest(status);
    }
}
