package kitchenpos.Order.domain.dto.response;

import kitchenpos.Order.domain.OrderLineItem;

public class OrderLineItemResponse {

    private Long seq;
    private long quantity;

    private OrderLineItemResponse(Long seq, long quantity) {
        this.seq = seq;
        this.quantity = quantity;
    }

    protected OrderLineItemResponse() {
    }

    public Long getSeq() {
        return seq;
    }

    public long getQuantity() {
        return quantity;
    }

    public static OrderLineItemResponse toDTO(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getSeq(), orderLineItem.getQuantity());
    }
}
