package kitchenpos.fixture;

import kitchenpos.domain.OrderLineItem;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderLineItemResponse;

import java.util.ArrayList;
import java.util.List;

public class OrderLineItemFixture {
    private static final Long SEQ = 1L;
    private static final Long MENU_ID = 1L;
    private static final long QUANTITY = 1L;

    public static OrderLineItem createOrderLineItem(Long menuId) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(SEQ);
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(QUANTITY);
        return orderLineItem;
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
