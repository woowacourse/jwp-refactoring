package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.application.dto.OrderChangeOrderStatusRequest;
import kitchenpos.application.dto.OrderCreateRequest;
import kitchenpos.application.dto.OrderLineItemCreateRequest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;

public class OrderFixture {
    public static Order createOrder(
        Long id,
        LocalDateTime orderedTime,
        OrderStatus orderStatus,
        Long orderTableId
    ) {
        return new Order(
            id,
            orderTableId,
            orderStatus.name(),
            orderedTime
        );
    }

    public static OrderCreateRequest createOrderRequest(
        Long orderTableId,
        List<OrderLineItemCreateRequest> orderLineItems
    ) {
        return new OrderCreateRequest(orderTableId, orderLineItems);
    }

    public static OrderChangeOrderStatusRequest createOrderRequestChangeOrderStatus(
        OrderStatus orderStatus
    ) {
        return new OrderChangeOrderStatusRequest(orderStatus);
    }
}
