package kitchenpos.order.dto.response;

import kitchenpos.menu.domain.Menu;
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

    public static OrderLineItemResponse of(final OrderLineItem orderLineItem, final Menu menu) {
        return new OrderLineItemResponse(
                orderLineItem.seq(),
                MenuResponse.from(menu),
                orderLineItem.quantity()
        );
    }

    public Long getSeq() {
        return seq;
    }

    public MenuResponse getMenuResponse() {
        return menuResponse;
    }

    public long getQuantity() {
        return quantity;
    }
}
