package kitchenpos.order.ui.response;

import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.menu.ui.response.MenuResponse;

public class OrderLineItemResponse {
    private Long seq;
    private MenuResponse menu;
    private long quantity;

    public OrderLineItemResponse(Long seq, MenuResponse menu, long quantity) {
        this.seq = seq;
        this.menu = menu;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse from(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getSeq(), MenuResponse.from(orderLineItem.getMenu()), orderLineItem
                .getQuantity());
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
