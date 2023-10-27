package kitchenpos.fixture;

import kitchenpos.order.application.dto.OrderCreateRequest;
import kitchenpos.order.application.dto.OrderLineItemCreateRequest;
import kitchenpos.order.application.dto.OrderUpdateRequest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.ordertable.domain.OrderTable;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("NonAsciiCharacters")
public class OrderFixture {

    public static Order 주문_생성(final Long orderTableId,
                              final String orderStatus,
                              final List<OrderLineItem> orderLineItems) {
        return new Order(orderTableId, orderStatus, orderLineItems);
    }


    public static OrderCreateRequest 주문_생성_요청(final Order order) {
        List<OrderLineItemCreateRequest> orderLineItemCreateRequests = order.getOrderLineItems()
                .stream()
                .map(it -> new OrderLineItemCreateRequest(it.getMenuId(), it.getQuantity()))
                .collect(Collectors.toList());

        return new OrderCreateRequest(order.getOrderTableId(), orderLineItemCreateRequests);
    }

    public static OrderCreateRequest 주문_생성_요청(final OrderTable orderTable,
                                              final List<OrderLineItem> orderLineItems) {
        List<OrderLineItemCreateRequest> orderLineItemCreateRequests = orderLineItems.stream()
                .map(it -> new OrderLineItemCreateRequest(it.getMenuId(), it.getQuantity()))
                .collect(Collectors.toList());

        return new OrderCreateRequest(orderTable.getId(), orderLineItemCreateRequests);
    }

    public static OrderCreateRequest 주문_생성_요청_잘못된_주문_테이블(final OrderTable orderTable,
                                              final List<OrderLineItem> orderLineItems) {
        List<OrderLineItemCreateRequest> orderLineItemCreateRequests = orderLineItems.stream()
                .map(it -> new OrderLineItemCreateRequest(it.getMenuId(), it.getQuantity()))
                .collect(Collectors.toList());

        return new OrderCreateRequest(-1L, orderLineItemCreateRequests);
    }

    public static OrderUpdateRequest 주문_업데이트_요청(final Order order) {
        return new OrderUpdateRequest(order.getOrderStatus());
    }
}
