package kitchenpos.order.application.response;

import kitchenpos.menu.application.response.MenuHistoryResponse;
import kitchenpos.menu.domain.MenuHistory;
import kitchenpos.order.domain.OrderLineItem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderLineItemHistoryResponse {

    private long seq;
    private MenuHistoryResponse menu;
    private long quantity;

    public OrderLineItemHistoryResponse(final long seq, final MenuHistoryResponse menu, final long quantity) {
        this.seq = seq;
        this.menu = menu;
        this.quantity = quantity;
    }

    public static OrderLineItemHistoryResponse from(final OrderLineItem orderLineItem, final MenuHistory menuHistory) {
        return new OrderLineItemHistoryResponse(
                orderLineItem.getSeq(),
                MenuHistoryResponse.from(menuHistory),
                orderLineItem.getQuantity().getValue()
        );
    }

    public static List<OrderLineItemHistoryResponse> from(final List<OrderLineItem> orderLineItems, final List<MenuHistory> menuHistories) {
        final List<OrderLineItemHistoryResponse> responses = new ArrayList<>();
        for (int index = 0; index < orderLineItems.size(); index++) {
            responses.add(from(orderLineItems.get(index), menuHistories.get(index)));
        }

        return responses;
    }

    public static List<OrderLineItemHistoryResponse> from(final List<OrderLineItem> orderLineItems, final MenuHistory menuHistory) {
        return orderLineItems.stream()
                .map(orderLineItem -> OrderLineItemHistoryResponse.from(orderLineItem, menuHistory))
                .collect(Collectors.toList());
    }

    public long getSeq() {
        return seq;
    }

    public MenuHistoryResponse getMenu() {
        return menu;
    }

    public long getQuantity() {
        return quantity;
    }
}
