package kitchenpos.fixture;

import kitchenpos.application.dto.OrderRequest;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.application.dto.OrderRequest.OrderLineItemRequest;

public class OrderFixture {

    public static Order order(Long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        return new Order(null, orderTableId, orderStatus, LocalDateTime.now(), orderLineItems);
    }

    public static Order order(Long orderTableId, List<OrderLineItem> orderLineItems) {
        return new Order(null, orderTableId, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
    }

    public static OrderRequest orderRequest(Long orderTableId, List<OrderLineItem> orderLineItems) {
        List<OrderLineItemRequest> orderLineItemRequests = orderLineItems.stream()
                .map(it -> new OrderLineItemRequest(it.getMenuId(), it.getQuantity()))
                .collect(Collectors.toList());
        return new OrderRequest(orderTableId, orderLineItemRequests);
    }
}
