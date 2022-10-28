package kitchenpos.application.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import kitchenpos.domain.OrderLineItem;

public class OrderLineItemResponse {

    private Long menuId;
    private long quantity;

    @JsonCreator
    public OrderLineItemResponse(final Long menuId, final long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItemResponse(final OrderLineItem orderLineItem) {
        this(orderLineItem.getMenu().getId(), orderLineItem.getQuantity());
    }

    public long getQuantity() {
        return quantity;
    }
}
