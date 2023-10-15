package kitchenpos.fixture;

import kitchenpos.application.dto.OrderRequest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

import static kitchenpos.application.dto.OrderRequest.OrderLineItemRequest;

public class OrderFixture {
    public static Order order(Long orderTableId, OrderStatus orderStatus) {
        return new Order(orderTableId, orderStatus, LocalDateTime.now());
    }

    public static OrderRequest orderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItemRequests) {
        return new OrderRequest(orderTableId, orderLineItemRequests);
    }
}
