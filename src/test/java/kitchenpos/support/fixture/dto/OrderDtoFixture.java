package kitchenpos.support.fixture.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.application.dto.OrderLineItemResponse;
import kitchenpos.order.application.dto.OrderLineItemSaveRequest;
import kitchenpos.order.application.dto.OrderResponse;
import kitchenpos.order.application.dto.OrderSaveRequest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

public class OrderDtoFixture {

    public static OrderSaveRequest 주문_생성_요청(Order order, List<OrderLineItem> orderLineItems) {
        List<OrderLineItemSaveRequest> orderLineItemSaveRequests = orderLineItems.stream()
            .map(it -> new OrderLineItemSaveRequest(it.getMenuId(), it.getQuantity()))
            .collect(Collectors.toList());
        return new OrderSaveRequest(order.getOrderTableId(), orderLineItemSaveRequests);
    }

    public static OrderResponse 주문_생성_응답(Order order, List<OrderLineItem> orderLineItems) {
        List<OrderLineItemResponse> orderLineItemResponses = orderLineItems.stream()
            .map(OrderLineItemResponse::toResponse)
            .collect(Collectors.toList());
        return new OrderResponse(order.getId(), order.getOrderTableId(), order.getOrderStatus().name(),
            order.getOrderedTime(), orderLineItemResponses);
    }
}
