package kitchenpos.order.application.dto;

import kitchenpos.order.domain.OrderLineItem;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderLineItemResponse {
    private final Long seq;
    private final Long orderId;
    private final Long menuId;
    private final long quantity;

    public static OrderLineItemResponse from(final OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getSeq(), orderLineItem.getOrderId(),
            orderLineItem.getMenuId(), orderLineItem.getQuantity());
    }
}
