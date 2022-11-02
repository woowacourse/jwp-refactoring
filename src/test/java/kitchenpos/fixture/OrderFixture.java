package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.request.OrderLineItemRequest;

public class OrderFixture {

    public static OrderCreateRequest generateOrderCreateRequest(Long orderTableId,
                                                                List<OrderLineItemRequest> orderLineItems) {
        return new OrderCreateRequest(orderTableId, orderLineItems);
    }

    public static Order generateOrder(LocalDateTime orderTime, Long orderTableId, String orderStatus) {
        return new Order(orderTableId, orderStatus, orderTime);
    }
}
