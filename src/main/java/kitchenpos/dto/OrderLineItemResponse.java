package kitchenpos.dto;

import kitchenpos.order.domain.OrderLineItem;

import java.util.List;
import java.util.stream.Collectors;

public class OrderLineItemResponse {

    private long seq;
    private MenuResponse menu;
    private long quantity;

    public OrderLineItemResponse(final long seq, final MenuResponse menu, final long quantity) {
        this.seq = seq;
        this.menu = menu;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse from(final OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(
                orderLineItem.getSeq(),
                MenuResponse.from(orderLineItem.getMenu()),
                orderLineItem.getQuantity().getValue()
        );
    }

    public static List<OrderLineItemResponse> from(final List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(OrderLineItemResponse::from)
                .collect(Collectors.toList());
    }

    public long getSeq() {
        return seq;
    }

    public MenuResponse getMenu() {
        return menu;
    }

    public long getQuantity() {
        return quantity;
    }
}
