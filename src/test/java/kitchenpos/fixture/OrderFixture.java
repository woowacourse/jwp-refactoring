package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.request.OrderLineItemRequest;

public class OrderFixture {

    public static OrderCreateRequest generateOrderCreateRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        return new OrderCreateRequest(orderTableId, orderLineItems);
    }

    public static Order generateOrder(LocalDateTime orderTime, Long orderTableId, String orderStatus) {
        Order order = new Order();
        order.setOrderedTime(orderTime);
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(orderStatus);
        return order;
    }
}
