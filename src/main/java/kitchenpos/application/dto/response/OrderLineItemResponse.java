package kitchenpos.application.dto.response;

import kitchenpos.domain.OrderLineItem;

public record OrderLineItemResponse(Long seq, Long orderId, Long menuId, long quantity) {

    public static OrderLineItemResponse from(final OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getSeq(),
                orderLineItem.getOrderId(),
                orderLineItem.getMenuId(),
                orderLineItem.getQuantity());
    }
}
