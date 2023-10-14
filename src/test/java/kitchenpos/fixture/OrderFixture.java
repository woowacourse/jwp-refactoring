package kitchenpos.fixture;

import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.OrderLineItemRequest;

public class OrderFixture {

    public static Order 주문(Long orderTableId, OrderStatus orderStatus) {
        return new Order(orderTableId, orderStatus.name(), LocalDateTime.now());
    }

    public static Order 주문(Long orderTableId, List<OrderLineItem> orderLineItems) {
        Order order = new Order(orderTableId);
        order.changeOrderLineItems(orderLineItems);
        return order;
    }

    public static OrderCreateRequest 주문_생성_요청(Long orderTableId, List<OrderLineItem> orderLineItems) {
        List<OrderLineItemRequest> orderLineItemRequests = orderLineItems.stream()
                .map(orderLineItem -> new OrderLineItemRequest(orderLineItem.getMenuId(), orderLineItem.getQuantity()))
                .collect(toList());
        return new OrderCreateRequest(orderTableId, orderLineItemRequests);
    }
}
