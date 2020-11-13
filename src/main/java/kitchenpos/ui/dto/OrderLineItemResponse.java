package kitchenpos.ui.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import kitchenpos.domain.OrderLineItem;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderLineItemResponse {
    @NotNull
    private final Long seq;

    @NotNull
    private final Long orderId;

    @NotNull
    private final Long menuId;

    @NotNull
    @Positive
    private final long quantity;

    public static OrderLineItemResponse from(final OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getSeq(), orderLineItem.getOrderId(),
            orderLineItem.getMenuId(), orderLineItem.getQuantity());
    }
}
