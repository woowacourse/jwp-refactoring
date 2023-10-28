package kitchenpos.order.service.dto;

import kitchenpos.menu.service.dto.MenuResponse;
import kitchenpos.order.OrderLineItem;

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
        return new OrderLineItemResponse(orderLineItem.getSeq(), MenuResponse.toDto(orderLineItem.getOrderMenu()), orderLineItem.getQuantity());
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
