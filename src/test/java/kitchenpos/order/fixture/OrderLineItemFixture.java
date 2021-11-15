package kitchenpos.order.fixture;

import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderLineItemResponse;

import java.util.ArrayList;
import java.util.List;

public class OrderLineItemFixture {
    private static final Long SEQ = 1L;
    private static final long QUANTITY = 1L;

    public static OrderLineItem createOrderLineItem(Long menuId) {
        return new OrderLineItem(SEQ, menuId, QUANTITY);
    }

    public static List<OrderLineItemResponse> createOrderLineItemResponses(List<OrderLineItemRequest> orderLineItemRequests) {
        List<OrderLineItemResponse> orderLineItemResponses = new ArrayList<>();
        long seq = 1L;
        for (OrderLineItemRequest request : orderLineItemRequests) {
            OrderLineItemResponse response = new OrderLineItemResponse(seq++, request.getSeq(), request.getMenuId(), request.getQuantity());
            orderLineItemResponses.add(response);
        }
        return orderLineItemResponses;
    }
}
