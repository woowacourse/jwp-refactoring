package kitchenpos.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.OrderLineItem;

import java.util.ArrayList;
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

    public static OrderLineItemResponse from(final OrderLineItem orderLineItem, final Menu menu) {
        return new OrderLineItemResponse(
                orderLineItem.getSeq(),
                MenuResponse.from(menu),
                orderLineItem.getQuantity().getValue()
        );
    }

    public static OrderLineItemResponse from(final OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(
                orderLineItem.getSeq(),
                MenuResponse.from((Menu) null),
                orderLineItem.getQuantity().getValue()
        );
    }

    public static List<OrderLineItemResponse> from(final List<OrderLineItem> orderLineItems, final List<Menu> menus) {
        final List<OrderLineItemResponse> responses = new ArrayList<>();
        for (int index = 0; index < orderLineItems.size(); index++) {
            responses.add(from(orderLineItems.get(index), menus.get(index)));
        }

        return responses;
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
