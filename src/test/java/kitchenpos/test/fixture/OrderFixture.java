package kitchenpos.test.fixture;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.order.application.OrderLineItemMapper;
import kitchenpos.order.application.OrderValidator;
import kitchenpos.order.application.dto.OrderLineItemQuantityDto;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.vo.OrderStatus;

public class OrderFixture {

    public static Order 주문(Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime) {
        return new Order(orderTableId, orderStatus, orderedTime);
    }

    public static Order 주문(
            Long orderTableId,
            OrderStatus orderStatus,
            LocalDateTime orderedTime,
            OrderValidator orderValidator
    ) {
        return new Order(
                orderTableId,
                orderStatus,
                orderedTime,
                orderValidator
        );
    }

    public static Order 주문(
            Long orderTableId,
            OrderStatus orderStatus,
            LocalDateTime orderedTime,
            List<OrderLineItemQuantityDto> orderLineItemQuantities,
            OrderLineItemMapper orderLineItemMapper,
            OrderValidator orderValidator
    ) {
        return new Order(
                orderTableId,
                orderStatus,
                orderedTime,
                orderLineItemQuantities,
                orderLineItemMapper,
                orderValidator
        );
    }
}
