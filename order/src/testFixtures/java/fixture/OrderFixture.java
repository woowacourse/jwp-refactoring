package fixture;

import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.dto.order.OrderCreateRequest;
import kitchenpos.dto.order.OrderLineItemRequest;

public class OrderFixture {

    public static Order 주문(OrderTable orderTable, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        return new Order(null, orderTable.getId(), orderStatus, LocalDateTime.now(), orderLineItems);
    }

    public static Order 주문(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        return new Order(orderTable.getId(), orderLineItems);
    }

    public static OrderCreateRequest 주문_생성_요청(Long orderTableId, List<OrderLineItem> orderLineItems) {
        List<OrderLineItemRequest> orderLineItemRequests = orderLineItems.stream()
                .map(orderLineItem -> new OrderLineItemRequest(orderLineItem.getMenuId(), orderLineItem.getQuantity()))
                .collect(toList());
        return new OrderCreateRequest(orderTableId, orderLineItemRequests);
    }
}
