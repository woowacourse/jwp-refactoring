package kitchenpos.order.dto.response;

import kitchenpos.menu.dto.response.MenuResponse;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse {
    private final Long seq;
    private final MenuResponse menuResponse;
    private final long quantity;

    public OrderLineItemResponse(final Long seq, final MenuResponse menuResponse, final long quantity) {
        this.seq = seq;
        this.menuResponse = menuResponse;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse from(final OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(
                orderLineItem.seq(),
                MenuResponse.from(orderLineItem.menu()),
                orderLineItem.quantity()
        );
    }

    public Long seq() {
        return seq;
    }

    public MenuResponse menuResponse() {
        return menuResponse;
    }

    public long quantity() {
        return quantity;
    }
}
