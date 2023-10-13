package kitchenpos.fixture;

import kitchenpos.application.order.dto.OrderCreateRequest;
import kitchenpos.application.order.dto.OrderLineItemCreateRequest;
import kitchenpos.application.order.dto.OrderUpdateRequest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("NonAsciiCharacters")
public class OrderFixture {

    public static Order 주문_생성(final Long orderTableId,
                              final String orderStatus,
                              final LocalDateTime orderedTime,
                              final List<OrderLineItem> orderLineItems) {
        return new Order(null, orderTableId, orderStatus, orderedTime, orderLineItems);
    }

    public static OrderCreateRequest 주문_생성_요청(final Order order) {
        List<OrderLineItemCreateRequest> orderLineItemCreateRequests = order.getOrderLineItems()
                .stream()
                .map(it -> new OrderLineItemCreateRequest(it.getMenuId(), it.getQuantity()))
                .collect(Collectors.toList());

        return new OrderCreateRequest(order.getOrderTableId(), orderLineItemCreateRequests);
    }

    public static OrderUpdateRequest 주문_업데이트_요청(final Order order) {
        return new OrderUpdateRequest(order.getOrderStatus());
    }
}
