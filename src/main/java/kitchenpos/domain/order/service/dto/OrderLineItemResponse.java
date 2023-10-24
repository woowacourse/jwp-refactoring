package kitchenpos.domain.order.service.dto;

import kitchenpos.domain.menu.service.dto.MenuResponse;
import kitchenpos.domain.order.OrderLineItem;

public class OrderLineItemResponse {
    private final Long seq;

    private final MenuResponse menu;

    private final long quantity;

    public OrderLineItemResponse(final Long seq, final MenuResponse menu, final long quantity) {
        this.seq = seq;
        this.menu = menu;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse toDto(final OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getSeq(), MenuResponse.toDto(orderLineItem.getMenu()), orderLineItem.getQuantity());
    }

    public Long getSeq() {
        return seq;
    }

    public MenuResponse getMenu() {
        return menu;
    }

    public long getQuantity() {
        return quantity;
    }
}
