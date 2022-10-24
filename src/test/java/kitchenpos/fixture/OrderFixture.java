package kitchenpos.fixture;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;

public class OrderFixture {

    public static Order createOrder(OrderTable orderTable, Menu... menus) {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (Menu menu : menus) {
            OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setMenuId(menu.getId());
            orderLineItem.setQuantity(1);
            orderLineItems.add(orderLineItem);
        }

        Order order = new Order();
        order.setOrderLineItems(orderLineItems);
        order.setOrderTableId(orderTable.getId());
        return order;
    }

    public static Order updatedOrder(String status) {
        Order order = new Order();
        order.setOrderStatus(status);
        return order;
    }
}
