package kitchenpos.dto.response;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemResponse {

    private Long seq;
    private OrderedMenuResponse menu;
    private long quantity;

    public OrderLineItemResponse() {
    }

    private OrderLineItemResponse(Long seq, OrderedMenuResponse menu, long quantity) {
        this.seq = seq;
        this.menu = menu;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse from(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(
                orderLineItem.getSeq(),
                OrderedMenuResponse.from(orderLineItem.getOrderedMenu()),
                orderLineItem.getQuantity()
        );
    }

    public Long getSeq() {
        return seq;
    }

    public OrderedMenuResponse getMenu() {
        return menu;
    }

    public long getQuantity() {
        return quantity;
    }
}
