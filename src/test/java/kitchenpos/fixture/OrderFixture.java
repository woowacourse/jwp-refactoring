package kitchenpos.fixture;

import kitchenpos.application.dto.OrderRequest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.application.dto.OrderRequest.OrderLineItemRequest;

public class OrderFixture {

    public static Order order(OrderTable orderTable, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        return new Order(null, orderTable, orderStatus, LocalDateTime.now(), orderLineItems);
    }

    public static Order order(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        return new Order(null, orderTable, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
    }

    public static OrderRequest orderRequest(Long orderTableId, List<OrderLineItem> orderLineItems) {
        List<OrderLineItemRequest> orderLineItemRequests = orderLineItems.stream()
                .map(it -> new OrderLineItemRequest(it.getMenu().getId(), it.getQuantity()))
                .collect(Collectors.toList());
        return new OrderRequest(orderTableId, orderLineItemRequests);
    }
}
