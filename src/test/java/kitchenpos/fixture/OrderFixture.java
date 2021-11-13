package kitchenpos.fixture;

import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderLineItemResponse;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static kitchenpos.fixture.OrderLineItemFixture.createOrderLineItemResponses;

public class OrderFixture {

    private static final Long ID = 1L;
    private static final Long ORDER_TABLE_ID = 1L;
    private static final OrderStatus ORDER_STATUS = OrderStatus.COOKING;

    public static OrderRequest createOrderRequest(Long orderTableId, OrderStatus orderStatus, Long... menuIds) {
        List<OrderLineItemRequest> orderLineItemRequests = new ArrayList<>();
        long seq = 1L;
        for (Long id : menuIds) {
            OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(seq++, id, 1L);
            orderLineItemRequests.add(orderLineItemRequest);
        }
        return new OrderRequest(orderTableId, orderStatus, orderLineItemRequests);
    }

    public static OrderRequest createOrderRequest(Long orderTableId, Long... menuIds) {
        return createOrderRequest(orderTableId, ORDER_STATUS, menuIds);
    }

    public static OrderRequest createOrderRequest() {
        return createOrderRequest(ID, ORDER_STATUS, ORDER_TABLE_ID);
    }

    public static OrderRequest createOrderRequest(Long orderTableId, List<OrderLineItemRequest> datas) {
        return new OrderRequest(orderTableId, ORDER_STATUS, datas);
    }

    public static OrderResponse createOrderResponse(OrderRequest orderRequest) {
        List<OrderLineItemResponse> orderLineItemResponses = createOrderLineItemResponses(orderRequest.getOrderLineItemRequests());
        return new OrderResponse(ID, orderRequest.getOrderTableId(), orderRequest.getOrderStatus(), LocalDateTime.now(), orderLineItemResponses);
    }
}
