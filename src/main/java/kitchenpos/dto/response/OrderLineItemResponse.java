package kitchenpos.dto.response;

import kitchenpos.domain.order.OrderLineItem;

public class OrderLineItemResponse {

    private Long seq;
    private Long menuId;
    private long quantity;

    private OrderLineItemResponse() {
    }

    public OrderLineItemResponse(OrderLineItem orderLineItem) {
        this(orderLineItem.getSeq(), orderLineItem.getMenuId(), orderLineItem.getQuantity());
    }

    public OrderLineItemResponse(Long seq, Long menuId, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "OrderLineItemResponse{" +
                "seq=" + seq +
                ", menuId=" + menuId +
                ", quantity=" + quantity +
                '}';
    }
}
