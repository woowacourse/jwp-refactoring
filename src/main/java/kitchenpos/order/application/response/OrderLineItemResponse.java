package kitchenpos.order.application.response;

import kitchenpos.menu.application.response.MenuResponse;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;

import java.util.ArrayList;
import java.util.List;

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

    public static List<OrderLineItemResponse> from(final OrderLineItems orderLineItems, final List<Menu> menus) {
        final List<OrderLineItemResponse> responses = new ArrayList<>();
        for (int index = 0; index < orderLineItems.getOrderLineItems().size(); index++) {
            responses.add(from(orderLineItems.getOrderLineItems().get(index), menus.get(index)));
        }

        return responses;
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
