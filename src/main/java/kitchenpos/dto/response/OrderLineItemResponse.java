package kitchenpos.dto.response;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemResponse {

    private Long seq;
    private OrderMenuResponse orderMenu;
    private long quantity;

    public OrderLineItemResponse(final Long seq, final OrderMenuResponse orderMenu, final long quantity) {
        this.seq = seq;
        this.orderMenu = orderMenu;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse from(final OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getSeq(), OrderMenuResponse.from(orderLineItem.getOrderMenu()),
                orderLineItem.getQuantity());
    }

    public Long getSeq() {
        return seq;
    }

    public OrderMenuResponse getOrderMenu() {
        return orderMenu;
    }

    public long getQuantity() {
        return quantity;
    }
}
