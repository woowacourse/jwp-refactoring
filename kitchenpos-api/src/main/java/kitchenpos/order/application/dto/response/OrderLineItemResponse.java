package kitchenpos.order.application.dto.response;

import kitchenpos.order.domain.OrderLineItem;

public record OrderLineItemResponse(Long seq, Long orderId, Long menuId, long quantity) {

    public static OrderLineItemResponse from(final OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getSeq(),
                orderLineItem.getOrderId(),
                orderLineItem.getMenuId(),
                orderLineItem.getQuantity());
    }
}
