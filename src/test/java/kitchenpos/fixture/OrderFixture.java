package kitchenpos.fixture;

import kitchenpos.order.application.dto.OrderCreateRequest;
import kitchenpos.order.application.dto.OrderLineItemCreateRequest;
import kitchenpos.order.application.dto.OrderUpdateRequest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.ordertable.domain.OrderTable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("NonAsciiCharacters")
public class OrderFixture {

    public static Order 주문_생성(final OrderTable orderTable,
                              final String orderStatus,
                              final LocalDateTime orderedTime,
                              final List<OrderLineItem> orderLineItems) {
        return new Order(null, orderTable, orderStatus, orderedTime, orderLineItems);
    }

    public static Order 주문_생성(final OrderTable orderTable,
                              final List<OrderLineItem> orderLineItems) {
        return new Order(orderTable, orderLineItems);
    }

    public static OrderCreateRequest 주문_생성_요청(final Order order) {
        List<OrderLineItemCreateRequest> orderLineItemCreateRequests = order.getOrderLineItems()
                .stream()
                .map(it -> new OrderLineItemCreateRequest(it.getMenu().getId(), it.getQuantity()))
                .collect(Collectors.toList());

        return new OrderCreateRequest(order.getOrderTable().getId(), orderLineItemCreateRequests);
    }

    public static OrderCreateRequest 주문_생성_요청(final OrderTable orderTable,
                                              final List<OrderLineItem> orderLineItems) {
        List<OrderLineItemCreateRequest> orderLineItemCreateRequests = orderLineItems.stream()
                .map(it -> new OrderLineItemCreateRequest(it.getMenu().getId(), it.getQuantity()))
                .collect(Collectors.toList());

        return new OrderCreateRequest(orderTable.getId(), orderLineItemCreateRequests);
    }

    public static OrderUpdateRequest 주문_업데이트_요청(final Order order) {
        return new OrderUpdateRequest(order.getOrderStatus());
    }
}
